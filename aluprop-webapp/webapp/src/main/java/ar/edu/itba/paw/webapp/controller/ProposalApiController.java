package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import ar.edu.itba.paw.webapp.dto.UserProposalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("proposal")
@Produces(value = { MediaType.APPLICATION_JSON })
public class ProposalApiController {

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
    @Path("{proposalId}/userProposals")
    public Response userProposalsFromProposal(@PathParam("proposalId") long proposalId){
        Proposal proposal = proposalService.getWithRelatedEntities(proposalId);

        return Response.ok(proposal.getUserProposals().stream()
                            .map(UserProposalDto::fromUserProposal)
                            .collect(Collectors.toList()))
                        .build();

    }
}
