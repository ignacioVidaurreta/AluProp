package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.UserProposalState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserProposalDto {

    private UserProposalDto() { }

    private long id;
    private UserProposalState state;
    private IndexUserDto user;

    public static UserProposalDto fromUserProposal(UserProposal userProposal) {
        UserProposalDto ret = new UserProposalDto();
        ret.id = userProposal.getId();
        ret.state = userProposal.getState();
        ret.user = IndexUserDto.fromUser(userProposal.getUser());
        return ret;
    }

    public static UserProposalDto fromCreator(User creator){
        UserProposalDto ret = new UserProposalDto();
        ret.id = -1;
        ret.state = null;
        ret.user = IndexUserDto.fromUser(creator);

        return ret;
    }

    public long getId() {
        return id;
    }

    public UserProposalState getState() {
        return state;
    }

    public IndexUserDto getUser(){
        return user;
    }
}
