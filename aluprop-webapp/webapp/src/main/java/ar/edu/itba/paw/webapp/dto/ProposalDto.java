package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.enums.ProposalState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProposalDto {

    public ProposalDto() { }

    private long id;
    private PropertyDto property;
    private ProposalState state;
    private IndexUserDto creator;


    public static ProposalDto fromProposal(Proposal proposal) {
        ProposalDto ret = new ProposalDto();
        ret.id = proposal.getId();
        ret.state = proposal.getState();
        ret.creator = IndexUserDto.fromUser(proposal.getCreator());

        return ret;
    }

    public static ProposalDto withPropertyWithRelatedEntities(Proposal proposal, Property property) {
        ProposalDto ret = fromProposal(proposal);
        ret.property = PropertyDto.fromProperty(property);
        return ret;
    }

    public long getId() {
        return id;
    }

    public PropertyDto getProperty() {
        return property;
    }

    public ProposalState getState() {
        return state;
    }

    public IndexUserDto getCreator(){
        return creator;
    }
}
