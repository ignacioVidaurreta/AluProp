package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;

import java.util.Collection;
import java.util.List;

public interface PropertyDao extends Dao<Property>{
	boolean showInterest(long propertyId, User user);
	boolean undoInterest(long propertyId, User user);

    Property getPropertyWithRelatedEntities(long id);
    Collection<Property> getPropertyByDescription(PageRequest pageRequest, String description);
    Property create(Property property);
    Collection<Property> getInterestsOfUser(long id);
    PageResponse<Property> getInterestsOfUserPaged(long id, PageRequest pageRequest);
    Long count();
    Collection<Property> advancedSearch(PageRequest pageRequest, String description, Integer propertyType, Integer neighborhood, Integer privacyLevel, Integer capacity, Float minPrice, Float maxPrice, Long[] rules, Long[] services);
}
