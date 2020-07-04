package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.enums.ProposalState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProposalWithPropCreatorDto {
    public ProposalWithPropCreatorDto() { }

    private long id;
    private PropertyWithCreatorDto property;
    private ProposalState state;
    private IndexUserDto creator;


    public static ProposalWithPropCreatorDto fromProposal(Proposal proposal) {
        ProposalWithPropCreatorDto ret = new ProposalWithPropCreatorDto();
        ret.id = proposal.getId();
        ret.property = PropertyWithCreatorDto.fromProperty(proposal.getProperty());
        ret.state = proposal.getState();
        ret.creator = IndexUserDto.fromUser(proposal.getCreator());

        return ret;
    }


    public long getId() {
        return id;
    }

    public PropertyWithCreatorDto getProperty() {
        return property;
    }

    public ProposalState getState() {
        return state;
    }

    public IndexUserDto getCreator(){
        return creator;
    }
}

