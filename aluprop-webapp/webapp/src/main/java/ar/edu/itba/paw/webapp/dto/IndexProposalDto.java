package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.enums.ProposalState;

import java.util.Collection;
import java.util.stream.Collectors;

public class IndexProposalDto {

    private IndexProposalDto() { }

    private long id;
    private IndexPropertyDto property;
    private ProposalState state;
    private IndexUserDto creator;
    private Collection<UserProposalDto> userProposals;

    public static IndexProposalDto fromProposal(Proposal proposal) {
        IndexProposalDto ret = new IndexProposalDto();
        ret.id = proposal.getId();
        ret.property = IndexPropertyDto.fromProperty(proposal.getProperty());
        ret.state = proposal.getState();
        ret.creator = IndexUserDto.fromUser(proposal.getCreator());
        ret.userProposals = proposal.getUserProposals()
                                    .stream()
                                    .map(UserProposalDto::fromUserProposal)
                                    .collect(Collectors.toList());
        return ret;
    }

    public long getId() {
        return id;
    }

    public IndexPropertyDto getProperty() {
        return property;
    }

    public ProposalState getState() {
        return state;
    }

    public IndexUserDto getCreator() {
        return creator;
    }

    public Collection<UserProposalDto> getUserProposals() {
        return userProposals;
    }
}
