package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.webapp.dto.IndexUserDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.dto.UserProposalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("proposal")
@Produces(value = { MediaType.APPLICATION_JSON })
public class ProposalApiController {
    private final static Logger logger = LoggerFactory.getLogger(ProposalApiController.class);

    @Autowired
    private ProposalService proposalService;
    @Autowired
    private PropertyService propertyService;

    @GET
    @Path("/{proposalId}")
    public Response proposal(@PathParam("proposalId") long proposalId) {
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);
        if(proposal == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        final Property property = propertyService.getPropertyWithRelatedEntities(proposal.getProperty().getId());
        return Response.ok(ProposalDto.withPropertyWithRelatedEntities(proposal, property)).build();
    }

    @GET
    @Path("/{proposalId}/userProposals")
    public Response userProposalsFromProposal(@PathParam("proposalId") long proposalId){
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);

        
        // Only show user proposals that don't include the creator
        Stream<UserProposal> userProposalStream = proposal.getUserProposals().stream().map(userProposal -> {
            if(userProposal.getUser().equals(proposal.getCreator()))
                return null;
            return userProposal;
        }).filter(Objects::nonNull);

        return Response.ok(userProposalStream
                            .map(UserProposalDto::fromUserProposal)
                            .collect(Collectors.toList()))
                .build();

    }

    @GET
    @Path("/{proposalId}/creatorUserProposal")
    public Response userProposalOfCreator(@PathParam("proposalId") long proposalId){
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);
        if(proposal == null){
            logger.error("Invalid Proposal with id " + proposalId);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        User creator = proposal.getCreator();
        return Response.ok(UserProposalDto.fromCreator(creator)).build();
    }

    @GET
    @Path("/{proposalId}/userProposal/{userProposalId}/user")
    public Response userIdFromUserProposal(@PathParam("proposalId") long proposalId,
                                     @PathParam("userProposalId") long userProposalId){
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);
        Optional<UserProposal> maybeUserProposal = proposal.getUserProposals().stream()
                                    .filter(up -> up.getId() == userProposalId)
                                    .findFirst();
        if(!maybeUserProposal.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(maybeUserProposal.get().getUser().getId()).build();
    }

}
