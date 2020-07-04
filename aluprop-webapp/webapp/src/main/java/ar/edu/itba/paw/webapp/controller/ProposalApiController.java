package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import ar.edu.itba.paw.webapp.dto.UserProposalDto;
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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("proposal")
@Produces(value = { MediaType.APPLICATION_JSON })
public class ProposalApiController {
    private final static Logger logger = LoggerFactory.getLogger(ProposalApiController.class);

    @Autowired
    private ProposalService proposalService;

    @GET
    @Path("/{proposalId}")
    public Response proposal(@PathParam("proposalId") long proposalId) {
        Proposal proposal = proposalService.get(proposalId);
        if(proposal == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(ProposalDto.fromProposal(proposalService.get(proposalId))).build();
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

}
