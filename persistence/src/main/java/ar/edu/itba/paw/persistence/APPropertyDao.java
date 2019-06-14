package ar.edu.itba.paw.persistence;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.SearchableProperty;
import ar.edu.itba.paw.interfaces.WhereConditionBuilder;
import ar.edu.itba.paw.interfaces.dao.*;
import ar.edu.itba.paw.interfaces.enums.SearchablePrivacyLevel;
import ar.edu.itba.paw.interfaces.enums.SearchablePropertyType;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyType;
import ar.edu.itba.paw.persistence.utilities.QueryUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class APPropertyDao implements PropertyDao {

    private static final String ALWAYS_TRUE_STRING = "1 = 1 ";
    private final String GET_INTERESTS_OF_USER_QUERY = "FROM properties p WHERE EXISTS (FROM interests i WHERE i.property.id = p.id AND i.user.id = :userId)";
    private final String GET_PROPERTIES_OF_USER_QUERY = "FROM properties p WHERE p.owner.id = :ownerId";
    private final String GET_PROPERTY_BY_DESCRIPTION_QUERY = "FROM Property p WHERE p.description LIKE CONCAT('%',?1,'%')";
    private final String INTEREST_BY_PROP_AND_USER_QUERY = "FROM Interest i WHERE i.property.id = :propertyId AND i.user.id = :userId";
    private final String ALWAYS_TRUE_STRING_AND = "1 = 1 AND ";

    @Autowired
    WhereConditionBuilder conditionBuilder;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Property get(long id) {
        return entityManager.find(Property.class, id);
    }

    @Override
    public Collection<Property> getPropertyByDescription(PageRequest pageRequest, String description) {
        if(description.equals(""))
            return getAll(pageRequest);
        TypedQuery<Property> query = entityManager.createQuery(GET_PROPERTY_BY_DESCRIPTION_QUERY, Property.class);
        return QueryUtility.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public Collection<Property> getAll(PageRequest pageRequest) {
        TypedQuery<Property> query = entityManager.createQuery("FROM Property", Property.class);
        return QueryUtility.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public Collection<Property> advancedSearch(PageRequest pageRequest, SearchableProperty property) {
        StringBuilder searchString = new StringBuilder("FROM Property p WHERE ");
        buildCondition(property);
        searchString.append(conditionBuilder.buildAsStringBuilder());
        TypedQuery<Property> query = entityManager.createQuery(searchString.toString(), Property.class);
        addSearchParameters(property, query);
        return QueryUtility.makePagedQuery(query, pageRequest).getResultList();
    }

    private void addSearchParameters(SearchableProperty property, TypedQuery<Property> query) {
        if(searchableDescription(property))
            query.setParameter("description", property.getDescription());
        if(searchablePropertyType(property))
            query.setParameter("propertyType", propertyTypeFromSearchablePropertyType(property.getPropertyType()));
        if(searchableNeighbourhood(property))
            query.setParameter("neighbourhood", property.getNeighbourhoodId());
        if(searchablePrivacyLevel(property))
            query.setParameter("privacyLevel", property.getPrivacyLevel() != SearchablePrivacyLevel.INDIVIDUAL);
        if(searchableCapacity(property))
            query.setParameter("capacity", property.getCapacity());
        if(searchableMinPrice(property))
            query.setParameter("minPrice", property.getMinPrice());
        if(searchableMaxPrice(property))
            query.setParameter("maxPrice", property.getMaxPrice());
        for(int i = 0; i < property.getRuleIds().length; i++)
            query.setParameter("rule" + i, entityManager.find(Rule.class, property.getRuleIds()[i]));
        for(int i = 0; i < property.getServiceIds().length; i++)
            query.setParameter("service" + i, entityManager.find(Service.class, property.getServiceIds()[i]));
    }

    private void buildCondition(SearchableProperty property) {
        conditionBuilder.begin();
        if(searchableDescription(property))
            conditionBuilder.equalityCondition("p.description", ":description");
        if(searchablePropertyType(property))
            conditionBuilder.equalityCondition("p.propertyType", ":propertyType");
        if(searchableNeighbourhood(property))
            conditionBuilder.equalityCondition("p.neighbourhood.id", ":neighbourhood");
        if(searchablePrivacyLevel(property))
            conditionBuilder.equalityCondition("p.privacyLevel", ":privacyLevel");
        if(searchableMinPrice(property))
            conditionBuilder.greaterThanCondition("p.price", ":minPrice");
        if(searchableMaxPrice(property))
            conditionBuilder.lessThanCondition("p.price", ":maxPrice");
        if(searchableCapacity(property))
            conditionBuilder.equalityCondition("p.capacity", ":capacity");
        if(property.getServiceIds() != null && property.getServiceIds().length > 0)
            for(int i = 0; i < property.getServiceIds().length; i++)
                conditionBuilder.simpleInCondition(":service" + i, "p.services");
        if(property.getRuleIds() != null && property.getRuleIds().length > 0)
            for(int i = 0; i < property.getRuleIds().length; i++)
                conditionBuilder.simpleInCondition(":rule" + i, "p.rules");
    }

    private PropertyType propertyTypeFromSearchablePropertyType(SearchablePropertyType propertyType) {
        return PropertyType.valueOf(propertyType.getValue());
    }

    private boolean searchableMaxPrice(SearchableProperty property) {
        return property.getMaxPrice() > 0;
    }

    private boolean searchableMinPrice(SearchableProperty property) {
        return property.getMinPrice() > 0;
    }

    private boolean searchableCapacity(SearchableProperty property) {
        return property.getCapacity() > 0;
    }

    private boolean searchablePrivacyLevel(SearchableProperty property) {
        return property.getPrivacyLevel() != SearchablePrivacyLevel.NOT_APPLICABLE;
    }

    private boolean searchableNeighbourhood(SearchableProperty property) {
        return property.getNeighbourhoodId() != SearchableProperty.NOT_APPLICABLE_NEIGHBOURHOOD_ID;
    }

    private boolean searchablePropertyType(SearchableProperty property) {
        return property.getPropertyType() != SearchablePropertyType.NOT_APPLICABLE;
    }

    private boolean searchableDescription(SearchableProperty property) {
        return property.getDescription() != null && !property.getDescription().equals("");
    }

    @Override
    public void changeStatus(Property prop, long id) {
        Availability oldAvail = prop.getAvailability();
        Availability newAvail;
        switch(oldAvail){
            case AVAILABLE:
                newAvail=Availability.RENTED;
                break;
            case RENTED:
                newAvail=Availability.AVAILABLE;
                break;
            default:
                System.out.println("Error"); //TODO Remove
                return;
        }
        prop.setAvailability(newAvail);
        entityManager.merge(prop);
    }
    
    @Override
    @Transactional
    public boolean showInterest(long propertyId, User user) {
        Interest interest = getInterestByPropAndUser(propertyId, user);
        if(interest != null) return false;
        interest = new Interest(entityManager.find(Property.class, propertyId), user);
        entityManager.persist(interest);
        return interest.getId() > 0;
    }

    @Override
    @Transactional
    public boolean undoInterest(long propertyId, User user) {
        Interest interest = getInterestByPropAndUser(propertyId, user);
        if(interest != null) {
            entityManager.remove(interest);
            return true;
        }
        return false;
    }

    public Interest getInterestByPropAndUser(long propertyId, User user) {
        TypedQuery<Interest> query = entityManager.createQuery(INTEREST_BY_PROP_AND_USER_QUERY, Interest.class);
        query.setParameter("propertyId", propertyId);
        query.setParameter("userId", user.getId());
        Interest interest;
        try{
            interest = query.getSingleResult();
        } catch (NoResultException e){
            return null;
        }
        return interest;
    }

    @Transactional
    @Override
    public Property getPropertyWithRelatedEntities(long id) {
        Property property = entityManager.find(Property.class, id);
        if (property == null)
            return null;
        property.getRules().isEmpty();
        property.getServices().isEmpty();
        property.getImages().isEmpty();
        return property;
    }

    @Override
    @Transactional
    public Property create(Property property) {
        if(property != null)
            entityManager.persist(property);
        return property;
    }

    @Override
    public void delete(long id) {
        Property property = entityManager.find(Property.class, id);
        entityManager.remove(property);
    }

    @Transactional
    @Override
    public Collection<Property> getInterestsOfUser(long id) {
        User user = entityManager.find(User.class, id);
        user.getInterestedProperties().isEmpty();
        return user.getInterestedProperties();
    }

    @Transactional
    @Override
    public Collection<Property> getByOwnerId(long id) {
        User user = entityManager.find(User.class, id);
        user.getOwnedProperties().isEmpty();
        return user.getOwnedProperties();
    }

    @Override
    public Collection<Property> getInterestsOfUserPaged(long id, PageRequest pageRequest) {
        TypedQuery<Property> query = entityManager.createQuery(GET_INTERESTS_OF_USER_QUERY, Property.class);
        query.setParameter("userId", id);
        return QueryUtility.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Property p", Long.class).getSingleResult();
    }


}
