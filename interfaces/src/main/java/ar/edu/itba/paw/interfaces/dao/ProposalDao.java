package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;

import java.util.Collection;

public interface ProposalDao {
    Proposal create(Proposal proposal, long[] userIds);
    void delete(long id);
    Proposal get(long id);
    Proposal getWithRelatedEntities(long id);
    Collection<Proposal> getAllProposalForUserId(long id);

    void setAccept(long userId, long proposalId);

    long setDecline(long userId, long proposalId);

    Collection<Proposal> getProposalsForOwnedProperties(long id);
}
