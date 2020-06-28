package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedList;

@Path("notification")
@Produces(value = { MediaType.APPLICATION_JSON })
public class NotificationApiController {

    private static final int NOTIFICATIONS_FOR_FIRST_PAGE = 5;
    private static final PageRequest NOTIFICATION_PAGE_REQUEST = new PageRequest(0, NOTIFICATIONS_FOR_FIRST_PAGE);

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProposalService proposalService;

    @GET
    public Response unread() {
        if(userService.getCurrentlyLoggedUser() == null) {
            System.out.println("No logged user 4 u");
            return  Response.status(Response.Status.FORBIDDEN).build();
        }
        Collection<Notification> notifications =
                notificationService.getAllUnreadNotificationsForUser(userService.getCurrentlyLoggedUser().getId(),
                                                                    NOTIFICATION_PAGE_REQUEST);
        return Response.ok(toDtos(notifications)).build();
    }

    private Collection<NotificationDto> toDtos(Collection<Notification> notifications) {
        Collection<NotificationDto> ret = new LinkedList<>();
        for (Notification n : notifications) {
            NotificationDto notificationDto = NotificationDto.fromNotification(n);
            notificationDto.setProposal(ProposalDto.fromProposal(getProposalsForNotification(n)));
            ret.add(notificationDto);
        }
        return ret;
    }

    private Proposal getProposalsForNotification(Notification n){
        return proposalService.get(Integer.parseInt(n.getLink().split("proposal/")[1]));
    }
}
