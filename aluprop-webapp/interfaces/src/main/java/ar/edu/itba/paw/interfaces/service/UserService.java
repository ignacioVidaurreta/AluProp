package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;

import java.util.List;
import java.util.Locale;

public interface UserService {
    User get(long id);
    User getWithRelatedEntities(long id);
    User getByEmail(String username);
    User getCurrentlyLoggedUser();
    Either<User, List<String>> CreateUser(User user, Locale loc, String url);
    User getUserWithRelatedEntitiesByEmail(String email);
    PageResponse<User> getUsersInterestedInProperty(long id, PageRequest pageRequest);
    PageResponse<Property> getCurrentUserInterests(PageRequest pageRequest);
    PageResponse<Proposal> getCurrentUserProposals(PageRequest pageRequest);
    PageResponse<Property> getCurrentUserProperties(PageRequest pageRequest);
    PageResponse<Proposal> getHostProposals(PageRequest pageRequest);
}
