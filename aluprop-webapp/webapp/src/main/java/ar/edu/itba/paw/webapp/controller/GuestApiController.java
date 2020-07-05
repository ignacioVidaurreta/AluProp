package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("guest")
@Produces(value = {MediaType.APPLICATION_JSON})
public class GuestApiController {

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
                .map((proposal)-> {
                    final Property property = propertyService.getPropertyWithRelatedEntities(proposal.getProperty().getId());
                    return ProposalDto.withPropertyWithRelatedEntities(proposal, property);
                })
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
        boolean hasReplied;

        Optional<Boolean> maybeHasReplied = checkUserHasReplied(currentUser, proposal);
        if(!maybeHasReplied.isPresent())
            return Response.ok(ProposalUserInfoDto.fromData(false, false, -1f)).build();

        hasReplied = maybeHasReplied.get();
        float budget = proposal.budget();

        return Response.ok(ProposalUserInfoDto.fromData(true, hasReplied, budget)).build();
    }

    private Optional<Boolean> checkUserHasReplied(User currentUser, Proposal proposal){
        Collection<UserProposal> userProposals = proposal.getUserProposals();
        Optional<UserProposal> maybeUserProposal = userProposals.stream()
                .filter(up -> up.getUser().equals(currentUser))
                .findFirst();
        return maybeUserProposal.map(userProposal -> userProposal.getState() != UserProposalState.PENDING);
    }

    @Path("/{propertyId}/interested")
    @POST
    public Response interest(@PathParam("propertyId") long propertyId) {
        User user = userService.getCurrentlyLoggedUser();
        return Response.ok(propertyService.showInterestOrReturnErrors(propertyId, user)).build();
    }

    @Path("/{propertyId}/uninterested")
    @POST
    public Response uninterested(@PathParam("propertyId") long propertyId) {
        User user = userService.getCurrentlyLoggedUser();
        return Response.ok(propertyService.undoInterestOrReturnErrors(propertyId, user)).build();
    }

    @Path("proposal/{propertyId}")
    @POST
    public Response createProposal(@PathParam("propertyId") long propertyId, ProposalCreationDto proposalCreationDto) {
        Either<Proposal, String> maybeProposal = proposalService.createProposal(propertyId, proposalCreationDto.getInviteeIds());
        if (!maybeProposal.hasValue())
            return Response.status(Response.Status.BAD_REQUEST).entity(maybeProposal.alternative()).build();
        return Response.ok(maybeProposal.value()).build();
    }
}
