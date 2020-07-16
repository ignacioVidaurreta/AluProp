package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.UserProposalState;
import ar.edu.itba.paw.webapp.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("guest")
@Produces(value = {MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.APPLICATION_JSON})
public class GuestApiController {

    private static final String NO_LANGUAGE_ERROR = "This endpoint requires a language";

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProposalService proposalService;

    @GET
    @Path("/proposals")
    public Response getCurrentUserProposals(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                                            @QueryParam("pageSize") @DefaultValue("12") int pageSize){
        PageResponse<Proposal> proposals = userService.getCurrentUserProposals(new PageRequest(pageNumber, pageSize));
        PageResponse<IndexProposalDto> proposalDtos = new PageResponse<>(proposals,
                                                                proposals.getResponseData()
                                                                        .stream()
                                                                        .map(IndexProposalDto::fromProposal)
                                                                        .collect(Collectors.toList()));
        return Response.ok(proposalDtos).build();
    }

    @GET
    @Path("/interests")
    public Response getInterests(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                                 @QueryParam("pageSize") @DefaultValue("12") int pageSize){

        PageResponse<Property> properties = userService.getCurrentUserInterests(new PageRequest(pageNumber, pageSize));
        PageResponse<IndexPropertyDto> propertyDtos = new PageResponse<>(properties,
                                                                        properties.getResponseData()
                                                                        .stream()
                                                                        .map(IndexPropertyDto::fromProperty)
                                                                        .collect(Collectors.toList()));

        return Response.ok(propertyDtos).build();
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
        if (proposal == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        User currentUser = userService.getCurrentlyLoggedUser();
        boolean hasReplied;

        Optional<Boolean> maybeHasReplied = checkUserHasReplied(currentUser, proposal);
        if(!maybeHasReplied.isPresent() || proposal.getCreator().equals(currentUser))
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
        return Response.status(propertyService.showInterestOrReturnErrors(propertyId, user)).build();
    }

    @Path("/{propertyId}/uninterested")
    @POST
    public Response uninterested(@PathParam("propertyId") long propertyId) {
        User user = userService.getCurrentlyLoggedUser();
        return Response.status(propertyService.undoInterestOrReturnErrors(propertyId, user)).build();
    }

    @Path("proposal/{propertyId}")
    @POST
    public Response createProposal(@PathParam("propertyId") long propertyId,
                                   @RequestBody final String requestBody,
                                   @Context HttpServletRequest request) throws IOException {
        final ProposalCreationDto proposalCreationDto = new ObjectMapper().readValue(new JSONObject(requestBody).toString(),
                                                                                    ProposalCreationDto.class);
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        String url = request.getRequestURL().toString();
        String host = url.substring(0, url.indexOf("/api/guest/proposal/" + propertyId));
        Either<Proposal, String> maybeProposal = proposalService.createProposal(propertyId, proposalCreationDto.getInviteeIds(), host, loc);
        if (!maybeProposal.hasValue())
            return Response.status(Response.Status.BAD_REQUEST).entity(maybeProposal.alternative()).build();
        return Response.ok(ProposalDto.fromProposal(maybeProposal.value())).build();
    }

    @Path("{proposalId}/cancel")
    @POST
    public Response cancel(@PathParam("proposalId") long proposalId,
                           @Context HttpServletRequest request) {
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        String url = request.getRequestURL().toString();
        String host = url.substring(0, url.indexOf("/api/guest/" + proposalId + "/cancel"));
        return Response.status(proposalService.delete(proposalId, host, loc)).build();
    }

    @Path("{proposalId}/accept")
    @POST
    public Response accept(@PathParam("proposalId") long proposalId,
                           @Context HttpServletRequest request) {
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        String url = request.getRequestURL().toString();
        String host = url.substring(0, url.indexOf("/api/guest/" + proposalId + "/accept"));
        return Response.status(proposalService.setAcceptInvite(proposalId, host, loc)).build();
    }

    @Path("{proposalId}/decline")
    @POST
    public Response decline(@PathParam("proposalId") long proposalId,
                            @Context HttpServletRequest request) {
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        String url = request.getRequestURL().toString();
        String host = url.substring(0, url.indexOf("/api/guest/" + proposalId + "/decline"));
        return Response.status(proposalService.setDeclineInvite(proposalId, host, loc)).build();
    }
}
