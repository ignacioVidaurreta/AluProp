package ar.edu.itba.paw.webapp.dto;

public class ProposalUserInfoDto {
    private ProposalUserInfoDto() {}

    private boolean isInvited;
    private boolean hasReplied;
    private float budget;

    public static ProposalUserInfoDto fromData(boolean isInvited, boolean hasReplied, float budget){
        ProposalUserInfoDto ret = new ProposalUserInfoDto();
        ret.isInvited = isInvited;
        ret.hasReplied = hasReplied;
        ret.budget = budget;
        return ret;
    }

    public boolean getIsInvited() {
        return isInvited;
    }

    public boolean getHasReplied() {
        return hasReplied;
    }

    public float getBudget() {
        return budget;
    }
}
