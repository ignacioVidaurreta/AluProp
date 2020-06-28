package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.PropertyDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Path("host")
@Produces(MediaType.APPLICATION_JSON)
public class HostApiController {

    @Autowired
    UserService userService;
    @Autowired
    ProposalService proposalService;

    @GET
    @Path("/proposals")
    public Response getProposals(){
        User user = userService.getCurrentlyLoggedUser();
        Collection<Proposal> proposals = proposalService.getProposalsForOwnedProperties(user);

        return Response.ok(proposals.stream()
                        .map(ProposalDto::fromProposal)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/properties")
    public Response getProperties(){
        User user = userService.getCurrentlyLoggedUser();
        Collection<Property> ownedProperties = userService.getWithRelatedEntities(user.getId()).getOwnedProperties();

        return Response.ok(ownedProperties.stream()
                            .map(IndexPropertyDto::fromProperty)
                            .collect(Collectors.toList()))
                .build();
    }
}
