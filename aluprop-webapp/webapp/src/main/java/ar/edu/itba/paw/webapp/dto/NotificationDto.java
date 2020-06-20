package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.enums.NotificationState;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NotificationDto {

    private NotificationDto() { }

    private long id;
    private String subjectCode;
    private String textCode;
    private String link;
    private NotificationState state;
    private ProposalDto proposal;

    public static NotificationDto fromNotification(Notification notification) {
        NotificationDto ret = new NotificationDto();
        ret.id = notification.getId();
        ret.subjectCode = notification.getSubjectCode();
        ret.textCode = notification.getTextCode();
        ret.link = notification.getLink();
        ret.state = notification.getState();
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getTextCode() {
        return textCode;
    }

    public String getLink() {
        return link;
    }

    public NotificationState getState() {
        return state;
    }

    public ProposalDto getProposal() {
        return proposal;
    }

    public void setProposal(ProposalDto proposal) {
        this.proposal = proposal;
    }
}
