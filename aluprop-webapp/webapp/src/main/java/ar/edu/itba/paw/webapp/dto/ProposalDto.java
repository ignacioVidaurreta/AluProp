package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.enums.ProposalState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProposalDto {

    public ProposalDto() { }

    private long id;
    private IndexPropertyDto property;
    private ProposalState state;

    public static ProposalDto fromProposal(Proposal proposal) {
        ProposalDto ret = new ProposalDto();
        ret.id = proposal.getId();
        ret.property = IndexPropertyDto.fromProperty(proposal.getProperty());
        ret.state = proposal.getState();
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
}
