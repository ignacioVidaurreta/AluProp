package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.Paginator;
import ar.edu.itba.paw.interfaces.SearchableProperty;
import ar.edu.itba.paw.interfaces.WhereConditionBuilder;
import ar.edu.itba.paw.interfaces.dao.PropertyDao;
import ar.edu.itba.paw.interfaces.enums.SearchablePrivacyLevel;
import ar.edu.itba.paw.interfaces.enums.SearchablePropertyType;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.PropertyOrder;
import ar.edu.itba.paw.model.enums.PropertyType;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.criterion.MatchMode;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
@Transactional
public class APPropertyDao implements PropertyDao {

    private final static Logger logger = LoggerFactory.logger(PropertyDao.class);

    private final static String INTEREST_BY_PROP_AND_USER_QUERY = "FROM Interest i WHERE i.property.id = :propertyId AND i.user.id = :userId";
    private final static String GET_ALL_ACTIVE_QUERY = "FROM Property p WHERE p.availability = 'AVAILABLE'";
    private final static String GET_ALL_ACTIVE_ORDERED_QUERY = GET_ALL_ACTIVE_QUERY + " ORDER BY";

    @Autowired
    private WhereConditionBuilder conditionBuilder;
    @Autowired
    private Paginator paginator;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Property get(long id) {
        return entityManager.find(Property.class, id);
    }

    @Override
    public Collection<Property> getAllActive(PageRequest pageRequest) {
        TypedQuery<Property> query = entityManager.createQuery(GET_ALL_ACTIVE_QUERY, Property.class);
        return paginator.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public Collection<Property> getAllActiveOrdered(PageRequest pageRequest, PropertyOrder propertyOrder) {
        TypedQuery<Property> query = entityManager.createQuery(GET_ALL_ACTIVE_ORDERED_QUERY + parseOrder(propertyOrder), Property.class);
        return  paginator.makePagedQuery(query, pageRequest).getResultList();
    }

    private String parseOrder(PropertyOrder propertyOrder) {
        switch (propertyOrder) {
            case PRICE:
                return " p.price";
            case PRICE_DESC:
                return " p.price DESC";
            case CAPACITY:
                return " p.capacity";
            case CAPACITY_DESC:
                return " p.capacity DESC";
            case BUDGET:
                return " p.price/p.capacity";
            case BUDGET_DESC:
                return " p.price/p.capacity DESC";
            case NEWEST:
            default:
                return " p.id DESC";
        }
    }

    @Override
    public Collection<Property> advancedSearch(PageRequest pageRequest, SearchableProperty property) {
        StringBuilder searchString = new StringBuilder("FROM Property p WHERE ");
        WhereConditionBuilder builder = buildCondition(property);
        searchString.append(builder.buildAsStringBuilder());
        if(searchString.toString().equals("FROM Property p WHERE "))
            return getAllActive(pageRequest);
        searchString.append("ORDER BY");
        searchString.append(parseOrder(property.getPropertyOrder()));
        TypedQuery<Property> query = entityManager.createQuery(searchString.toString(), Property.class);
        addSearchParameters(property, query);
        return paginator.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public long totalItemsOfSearch(SearchableProperty property) {
        StringBuilder searchString = new StringBuilder("SELECT COUNT(p.id) FROM Property p WHERE ");
        WhereConditionBuilder builder = buildCondition(property);
        searchString.append(builder.buildAsStringBuilder());
        if(searchString.toString().equals("SELECT COUNT(p.id) FROM Property p WHERE "))
            return countAvailable();
        TypedQuery<Long> query = entityManager.createQuery(searchString.toString(), Long.class);
        addSearchParameters(property, query);
        return query.getSingleResult();
    }

    private <T> void addSearchParameters(SearchableProperty property, TypedQuery<T> query) {
        if(searchableDescription(property))
            query.setParameter("description", MatchMode.ANYWHERE.toMatchString(property.getDescription().toLowerCase()));
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
        query.setParameter("availability", Availability.AVAILABLE);
    }

    private WhereConditionBuilder buildCondition(SearchableProperty property) {
        WhereConditionBuilder builder = conditionBuilder.begin();
        if(searchableDescription(property))
            builder = builder.descriptionCondition("lower(p.description)","lower(p.caption)", ":description");
        if(searchablePropertyType(property))
            builder = builder.equalityCondition("p.propertyType", ":propertyType");
        if(searchableNeighbourhood(property))
            builder = builder.equalityCondition("p.neighbourhood.id", ":neighbourhood");
        if(searchablePrivacyLevel(property))
            builder = builder.equalityCondition("p.privacyLevel", ":privacyLevel");
        builder = getBudgetConditions(property, builder);
        if(property.getServiceIds() != null && property.getServiceIds().length > 0)
            for(int i = 0; i < property.getServiceIds().length; i++)
                builder = builder.simpleInCondition(":service" + i, "p.services");
        if(property.getRuleIds() != null && property.getRuleIds().length > 0)
            for(int i = 0; i < property.getRuleIds().length; i++)
                builder = builder.simpleInCondition(":rule" + i, "p.rules");
        return builder.equalityCondition("p.availability", ":availability");
    }

    private WhereConditionBuilder getBudgetConditions(SearchableProperty property, WhereConditionBuilder builder) {
        if(searchableCapacity(property)) {
            if(searchableMinPrice(property)) {
                if(searchableMaxPrice(property)) {
                    builder = builder.lessOrEqualThanCondition("p.capacity", ":capacity");
                    builder = builder.lessOrEqualThanCondition("p.price/p.capacity",":maxPrice");
                    builder = builder.greaterOrEqualThanCondition("p.price/p.capacity",":minPrice");
                }
                else {
                    builder = builder.lessOrEqualThanCondition("p.capacity", ":capacity");
                    builder = builder.greaterOrEqualThanCondition("p.price/p.capacity",":minPrice");
                }
            }
            else {
                if(searchableMaxPrice(property)) {
                    builder = builder.lessOrEqualThanCondition("p.capacity", ":capacity");
                    builder = builder.lessOrEqualThanCondition("p.price/p.capacity",":maxPrice");
                }
                else {
                    builder = builder.lessOrEqualThanCondition("p.capacity", ":capacity");
                }
            }
        }
        else {
            if(searchableMinPrice(property)) {
                if(searchableMaxPrice(property)) {
                    builder = builder.lessOrEqualThanCondition("p.price/p.capacity",":maxPrice");
                }
                builder = builder.greaterOrEqualThanCondition("p.price/p.capacity",":minPrice");
            }
            else {
                if(searchableMaxPrice(property)) {
                    builder = builder.lessOrEqualThanCondition("p.price/p.capacity",":maxPrice");
                }
            }
        }
        return builder;
    }

    // transforms searchable property type into regular property type
    // receiving a NOT_APPLICABLE will return null
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
    public void changeStatus(long propertyId) {
        Property prop = entityManager.find(Property.class, propertyId);
        switch(prop.getAvailability()) {
            case AVAILABLE:
                prop.setAvailability(Availability.RENTED);
                break;
            case RENTED:
                prop.setAvailability(Availability.AVAILABLE);
                break;
            default:
                logger.warn("property without availability, property id: " + prop.getId());
                return;
        }
        entityManager.merge(prop);
    }
    
    @Override
    public void showInterest(long propertyId, User user) {
        Interest interest = getInterestByPropAndUser(propertyId, user);
        if(interest != null) return;
        interest = new Interest(entityManager.find(Property.class, propertyId), user);
        entityManager.persist(interest);
        return;
    }

    @Override
    public boolean undoInterest(long propertyId, User user) {
        Interest interest = getInterestByPropAndUser(propertyId, user);
        if(interest != null) {
            entityManager.remove(interest);
            return true;
        }
        return false;
    }

    @Override
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

    @Override
    public Property getPropertyWithRelatedEntities(long id) {
        Property property = entityManager.find(Property.class, id);
        if (property == null)
            return null;
        property.getRules().isEmpty();
        property.getServices().isEmpty();
        property.getImages().isEmpty();
        property.getInterestedUsers().isEmpty();
        property.getProposals().isEmpty();
        return property;
    }

    @Override
    public Property create(Property property) {
        if(property != null)
            entityManager.persist(property);
        return property;
    }

    @Override
    @Transactional
    public void delete(long id) {
        Property property = entityManager.find(Property.class, id);
        entityManager.remove(property);
    }

    @Override
    public Collection<Property> getInterestsOfUser(long id) {
        User user = entityManager.find(User.class, id);
        user.getInterestedProperties().isEmpty();
        return user.getInterestedProperties();
    }

    @Override
    public Long countAvailable() {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Property p WHERE p.availability = 'AVAILABLE'", Long.class).getSingleResult();
    }


}
