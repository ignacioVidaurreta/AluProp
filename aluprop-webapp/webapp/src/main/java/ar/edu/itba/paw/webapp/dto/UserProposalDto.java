package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.UserProposalState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserProposalDto {

    private UserProposalDto() { }

    private long id;
    private UserProposalState state;

    public static UserProposalDto fromUserProposal(UserProposal userProposal) {
        UserProposalDto ret = new UserProposalDto();
        ret.id = userProposal.getId();
        ret.state = userProposal.getState();
        return ret;
    }

    public long getId() {
        return id;
    }

    public UserProposalState getState() {
        return state;
    }
}
