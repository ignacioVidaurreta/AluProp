package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ProposalState;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

public interface ProposalService {
    Either<Proposal, String> createProposal(long propertyId, long[] userIds, String url, Locale loc);
    int delete(long id, String url, Locale loc);
    Proposal get(long id);
    Proposal getWithRelatedEntities(long id);
    Collection<Proposal> getAllProposalForUserId(long id);
    int setAcceptInvite(long proposalId, String url, Locale loc);
    int setDeclineInvite(long proposalId, String ulr, Locale loc);
    int setState(long proposalId, ProposalState state, String url, Locale loc);
    Collection<Proposal> getProposalsForOwnedProperties(User profileUser);
}
