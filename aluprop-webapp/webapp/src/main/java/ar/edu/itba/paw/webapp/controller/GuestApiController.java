package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.UserProposalDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("guest")
@Produces(value = {MediaType.APPLICATION_JSON})
public class GuestApiController {

    @Autowired
    UserService userService;

    @GET
    @Path("/proposals")
    public Response getAllUserProposalsOfUser(){
        User user = userService.getCurrentlyLoggedUser();
        Collection<UserProposal> proposals =
                userService.getWithRelatedEntities(user.getId()).getUserProposals();
        return Response.ok(proposals.stream()
                .map(UserProposalDto::fromUserProposal)
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
}
