package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Notification;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.webapp.dto.NotificationDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

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
    public Response unread(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                           @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        Collection<Notification> notifications = notificationService.getAllUnreadNotificationsForUser();
        Collection<NotificationDto> notificationDtos = notifications.stream()
                                                                    .map(n -> NotificationDto.withProposal(n, getProposalsForNotification(n)))
                                                                    .collect(Collectors.toList());
        return Response.ok(notificationDtos).build();
    }

    @GET
    @Path("/all")
    public Response all(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                        @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Notification> notifications =
                notificationService.getAllNotificationsForUser(new PageRequest(pageNumber, pageSize));
        PageResponse<NotificationDto> notificationDtos = new PageResponse<>(notifications,
                notifications.getResponseData().stream()
                        .map(n -> NotificationDto.withProposal(n, getProposalsForNotification(n)))
                        .collect(Collectors.toList()));
        return Response.ok(notificationDtos).build();
    }

    @POST
    @Path("/{notificationId}")
    public Response markRead(@PathParam("notificationId") long id) {
        notificationService.markRead(id);
        return Response.ok().build();
    }

    private Proposal getProposalsForNotification(Notification n){
        return proposalService.get(Integer.parseInt(n.getLink().split("proposal/")[1]));
    }
}
