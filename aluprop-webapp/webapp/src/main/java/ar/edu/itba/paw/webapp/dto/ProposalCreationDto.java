package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ProposalCreationDto {

    private long[] inviteeIds;

    public long[] getInviteeIds() {
        return inviteeIds;
    }
}
