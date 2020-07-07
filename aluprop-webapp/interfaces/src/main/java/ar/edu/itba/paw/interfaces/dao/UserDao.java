package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;

import java.util.Collection;

public interface UserDao {
    Collection<User> getAll();
    User get(long id);
    User getWithRelatedEntities(long id);
    User getByEmail(String email);
    User getUserWithRelatedEntitiesByEmail(String email);
    User create(User user);
    boolean userExistsByEmail(String email);
    Collection<User> getUsersInterestedInProperty(long id, PageRequest pageRequest);
    Long count();
    Long countUserInterests(long currentUserId);
    Collection<Property> getUserInterests(PageRequest pageRequest, long currentUserId);
    Long countUserProposals(long userId);
    Collection<Proposal> getUserProposals(PageRequest pageRequest, long userId);
    Long countUserProperties(long userId);
    Collection<Property> getUserProperties(PageRequest pageRequest, long userId);

    Long countValidProposals(long userId);

    Collection<Proposal> getValidHostProposals(PageRequest pageRequest, long userId);
}
