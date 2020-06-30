package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.PropertyDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("host")
@Produces(MediaType.APPLICATION_JSON)
public class HostApiController {

    @Autowired
    UserService userService;
    @Autowired
    ProposalService proposalService;
    @Autowired
    PropertyService propertyService;

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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createProperty")
    public Response createProperty(Property property){
        Either<Property,Collection<String> > maybeProperty = propertyService.create(property);

        if(!maybeProperty.hasValue()){
            Response.status(Response.Status.CONFLICT).entity(ErrorDto.fromErrors(maybeProperty.alternative())).build();
        }

        return Response.status(Response.Status.CREATED).entity(PropertyDto.fromProperty(maybeProperty.value())).build();
    }
}
