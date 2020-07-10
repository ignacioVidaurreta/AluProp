package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ProposalState;

import java.util.Collection;
import java.util.List;

public interface ProposalService {
    Either<Proposal, String> createProposal(long propertyId, long[] userIds);
    int delete(long id);
    Proposal get(long id);
    Proposal getWithRelatedEntities(long id);
    Collection<Proposal> getAllProposalForUserId(long id);
    int setAcceptInvite(long proposalId);
    int setDeclineInvite(long proposalId);
    int setState(long proposalId, ProposalState state);
    Collection<Proposal> getProposalsForOwnedProperties(User profileUser);
}
