package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.SearchableProperty;
import ar.edu.itba.paw.model.Interest;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.PropertyOrder;

import java.util.Collection;

public interface PropertyDao {
    Property get(long id);
    Collection<Property> getAllActive(PageRequest pageRequest);
    Collection<Property> getAllActiveOrdered(PageRequest pageRequest, PropertyOrder propertyOrder);
	void showInterest(long propertyId, User user);
	boolean undoInterest(long propertyId, User user);
    Property getPropertyWithRelatedEntities(long id);
    Property create(Property property);
    void delete(long id);
    Collection<Property> getInterestsOfUser(long id);
    Long countAvailable();
    Collection<Property> advancedSearch(PageRequest pageRequest, SearchableProperty property);
    long totalItemsOfSearch(SearchableProperty property);
    void changeStatus(long propertyId);
    Interest getInterestByPropAndUser(long propertyId, User user);
}
