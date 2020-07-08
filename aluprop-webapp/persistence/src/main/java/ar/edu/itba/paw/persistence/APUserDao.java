package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.Paginator;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Collection;
import java.util.stream.Collectors;

@Repository
@Transactional
public class APUserDao implements UserDao {

    @Autowired
    private Paginator paginator;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User get(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getWithRelatedEntities(long id){
        User user = entityManager.find(User.class, id);
        initializeRelatedEntities(user);
        return user;
    }

    @Override
    public User getByEmail(String email) {
        try {
            return entityManager.createQuery("FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        }
        catch(NoResultException e){
            return null; //No user with this email exists
        }
    }

    private void initializeRelatedEntities(User user) {
        user.getUserProposals().isEmpty();
        user.getInterestedProperties().isEmpty();
        user.getNotifications().isEmpty();
        user.getOwnedProperties().isEmpty();
        user.getUserProposals().forEach(up -> up.getProposal().getId());
    }

    @Override
    public Collection<User> getAll() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public User create(User user) {
        entityManager.persist(user);
        return user;
    }

    @Override
    public User getUserWithRelatedEntitiesByEmail(String email) {
        User user = getByEmail(email);
        if (user == null)
            return null;
        initializeRelatedEntities(user);
        return user;
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return !entityManager.createQuery("FROM User u WHERE u.email = :email")
                            .setParameter("email", email)
                            .getResultList()
                            .isEmpty();
    }

    @Override
    public Collection<User> getUsersInterestedInProperty(long id, PageRequest pageRequest) {
        TypedQuery<Interest> query = entityManager.createQuery("FROM Interest i WHERE i.property.id = :propertyId", Interest.class);
        query.setParameter("propertyId", id);
        Collection<Interest> interests = paginator.makePagedQuery(query, pageRequest).getResultList();
        return interests.stream().map(Interest::getUser).collect(Collectors.toList());
    }

    @Override
    public Long count() {
        return entityManager.createQuery("SELECT COUNT(u.id) FROM User u", Long.class).getSingleResult();
    }

    @Override
    public Long countUserInterests(long userId) {
        return entityManager.createQuery("SELECT COUNT(i.id) FROM Interest i WHERE i.user.id = :userId", Long.class)
                            .setParameter("userId", userId)
                            .getSingleResult();
    }

    @Override
    public Collection<Property> getUserInterests(PageRequest pageRequest, long userId) {
        TypedQuery<Interest> query = entityManager.createQuery("FROM Interest i WHERE i.user.id = :userId", Interest.class);
        query.setParameter("userId", userId);
        Collection<Interest> interests = paginator.makePagedQuery(query, pageRequest).getResultList();
        return interests.stream().map(Interest::getProperty).collect(Collectors.toList());
    }

    @Override
    public Long countUserProposals(long userId) {
        return countInvitedToProposals(userId) + countCreatedProposals(userId);
    }

    private Long countInvitedToProposals(long userId) {
        return entityManager.createQuery("SELECT COUNT(u.id) FROM UserProposal u WHERE u.user.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    private Long countCreatedProposals(long userId) {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Proposal p WHERE p.creator.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    // this function has to be a mess because of a design flaw in the fact that the creator of a proposal
    // is not included in the intermediate object UserProposal. This function returns created proposals
    // first and proposals to which the user has been invited to second.
    // The function is a mess because it has to determine whether to bring just created proposals, invited to
    // proposals or a bit of both.
    @Override
    public Collection<Proposal> getUserProposals(PageRequest pageRequest, long userId) {
        Collection<Proposal> ret;
        Long createdProposalCount = countCreatedProposals(userId);
        int left = pageRequest.getPageNumber() * pageRequest.getPageSize();
        int right = left + pageRequest.getPageSize();
        if (createdProposalCount >= right)
            ret = getCreatedProposals(pageRequest, userId);
        else if (createdProposalCount > left &&
            createdProposalCount + pageRequest.getPageSize() > right)
            ret = getMixedProposals(pageRequest, userId, createdProposalCount);
        else
            ret = getInvitedToProposals(pageRequest, userId, left - createdProposalCount);
        ret.forEach(p -> p.getUserProposals().isEmpty());
        return ret;
    }

    private Collection<Proposal> getCreatedProposals(PageRequest pageRequest, long userId) {
        TypedQuery<Proposal> query = entityManager.createQuery("FROM Proposal p WHERE p.creator.id = :userId", Proposal.class);
        query.setParameter("userId", userId);
        return  paginator.makePagedQuery(query, pageRequest).getResultList();
    }

    private Collection<Proposal> getInvitedToProposals(PageRequest pageRequest, long userId, Long overflow) {
        TypedQuery<UserProposal> query = entityManager.createQuery("FROM UserProposal p WHERE p.user.id = :userId", UserProposal.class);
        query.setParameter("userId", userId);
        query.setFirstResult(overflow.intValue());
        query.setMaxResults(pageRequest.getPageSize());
        return query.getResultList().stream().map(u -> u.getProposal()).collect(Collectors.toList());
    }

    private Collection<Proposal> getMixedProposals(PageRequest pageRequest, long userId, Long createdProposalCount) {
        int createdProposalsStart = pageRequest.getPageNumber() * pageRequest.getPageSize();
        Long invitedToProposalsEnd = (pageRequest.getPageNumber() + 1) * pageRequest.getPageSize() - createdProposalCount;
        TypedQuery<Proposal> query = entityManager.createQuery("FROM Proposal p WHERE p.creator.id = :userId", Proposal.class);
        query.setParameter("userId", userId);
        query.setFirstResult(createdProposalsStart);
        Collection<Proposal> created = query.getResultList();
        TypedQuery<UserProposal> invitedToQuery = entityManager.createQuery("FROM UserProposal p WHERE p.user.id = :userId", UserProposal.class);
        invitedToQuery.setParameter("userId", userId);
        invitedToQuery.setMaxResults(invitedToProposalsEnd.intValue());
        Collection<Proposal> invitedTo = invitedToQuery.getResultList().stream().map(u -> u.getProposal()).collect(Collectors.toList());
        created.addAll(invitedTo);
        return created;
    }

    @Override
    public Long countUserProperties(long userId) {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Property p WHERE p.owner.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
    }

    @Override
    public Collection<Property> getUserProperties(PageRequest pageRequest, long userId) {
        TypedQuery<Property> query = entityManager.createQuery("FROM Property p WHERE p.owner.id = :userId", Property.class);
        query.setParameter("userId", userId);
        return  paginator.makePagedQuery(query, pageRequest).getResultList();
    }

    @Override
    public Long countValidProposals(long userId) {
        return entityManager.createQuery("SELECT COUNT(p.id) FROM Proposal p WHERE p.state <> 'PENDING' AND " +
                                                                                    "p.state <> 'DROPPED' AND " +
                                                                                    "p.state <> 'CANCELED' AND " +
                                                                                    "p.property.id IN " +
                                            "(FROM Property pty WHERE pty.owner.id = :userId)", Long.class)
                                            .setParameter("userId", userId)
                                            .getSingleResult();
    }

    @Override
    public Collection<Proposal> getValidHostProposals(PageRequest pageRequest, long userId) {
        TypedQuery<Proposal> query = entityManager.createQuery("FROM Proposal p WHERE p.state <> 'PENDING' AND " +
                                                                                        "p.state <> 'DROPPED' AND " +
                                                                                        "p.state <> 'CANCELED' AND " +
                                                                                        "p.property.id IN " +
                                                                    "(FROM Property pty WHERE pty.owner.id = :userId)", Proposal.class);
        query.setParameter("userId", userId);
        Collection<Proposal> proposals =  paginator.makePagedQuery(query, pageRequest).getResultList();
        proposals.forEach(p -> p.getUserProposals().isEmpty());
        return proposals;
    }
}
