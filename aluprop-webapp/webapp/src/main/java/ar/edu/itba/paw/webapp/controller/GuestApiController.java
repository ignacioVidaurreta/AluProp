package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.UserProposalState;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Path("guest")
@Produces(value = {MediaType.APPLICATION_JSON})
public class GuestApiController {

    private static final Logger logger = LoggerFactory.getLogger(GuestApiController.class);

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProposalService proposalService;

    @GET
    @Path("/proposals")
    public Response getAllUserProposalsOfUser(){
        User user = userService.getCurrentlyLoggedUser();

        Collection<Proposal> proposals= proposalService.getAllProposalForUserId(user.getId());

        return Response.ok(proposals.stream()
                .map(ProposalDto::fromProposal)
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/interests")
    public Response getPropertiesInterests(){
        User user = userService.getCurrentlyLoggedUser();
        Collection<Property> properties = userService.getWithRelatedEntities(user.getId()).getInterestedProperties();

        return Response.ok(properties.stream()
                            .map(IndexPropertyDto::fromProperty)
                            .collect(Collectors.toList()))
                        .build();
    }

    @GET
    @Path("/interested/{propertyId}")
    public Response isInterestedInProperty(@PathParam(value = "propertyId") long propertyId){
        User currentUser = userService.getCurrentlyLoggedUser();
        Collection<User> interestedUsers = propertyService.getPropertyWithRelatedEntities(propertyId)
                .getInterestedUsers();
        boolean isInterested = interestedUsers.stream().anyMatch(user -> user.equals(currentUser));
        return Response.ok(isInterested).build();
    }

    @GET
    @Path("/{proposalId}/userInfo")
    public Response userInfo(@PathParam(value = "proposalId") long proposalId) {
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);
        User currentUser = userService.getCurrentlyLoggedUser();
        User creator = proposal.getCreator();

        boolean isInvited = !proposal.getUsersWithoutCreator(creator.getId()).contains(currentUser)
                            && !proposal.getProperty().getOwner().equals(currentUser);

        boolean hasReplied = false;
        try {
            Collection<UserProposal> userProposals = proposal.getUserProposals();
            hasReplied = userProposals.stream().filter(up -> up.getUser().equals(currentUser))
                    .findFirst().get().getState() != UserProposalState.ACCEPTED;
        }catch (NoSuchElementException e){
            logger.error(String.format(
                    "User with id %d doesn't have a user proposal in proposal with ID %d",
                    currentUser.getId(),
                    proposalId
            ));
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        float budget = proposal.budget();

        return Response.ok(ProposalUserInfoDto.fromData(isInvited, hasReplied, budget)).build();
    }

}
