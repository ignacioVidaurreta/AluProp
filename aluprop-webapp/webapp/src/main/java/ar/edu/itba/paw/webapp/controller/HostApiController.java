package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.ProposalState;
import ar.edu.itba.paw.model.enums.UserProposalState;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.PropertyDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONObject;

@Path("host")
@Produces(MediaType.APPLICATION_JSON)
public class HostApiController {

    private final static Logger logger = LoggerFactory.getLogger(HostApiController.class);

    @Autowired
    UserService userService;
    @Autowired
    ProposalService proposalService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    ImageService imageService;

    @Path("/proposals")
    @GET
    public Response proposals(){
        User user = userService.getCurrentlyLoggedUser();
        Collection<Proposal> proposals = proposalService.getProposalsForOwnedProperties(user);

        Stream<Proposal> validProposals = proposals.stream().filter(proposal ->
                !proposal.getState().equals(ProposalState.PENDING)
                        && !proposal.getState().equals(ProposalState.DROPPED)
                        && !proposal.getState().equals(ProposalState.CANCELED)
        );

        return Response.ok(validProposals
                        .map((proposal -> {
                            final Property property =
                                    propertyService.getPropertyWithRelatedEntities(proposal.getProperty().getId());
                            return ProposalDto.withPropertyWithRelatedEntities(proposal, property);
                        }))
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
    @Path("/createProperty")
    public Response createProperty(@RequestBody final String requesBody){
        final ObjectMapper objectMapper = new ObjectMapper();
        Property createdProperty;
        final PropertyDto propertyDto;
        try {
            propertyDto = objectMapper.readValue(new JSONObject(requesBody).toString(), PropertyDto.class);
            createdProperty = propertyDto.toProperty(userService.getCurrentlyLoggedUser());
        }catch (IOException e){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        propertyService.create(createdProperty);

        return Response.status(Response.Status.CREATED).entity(propertyDto).build();

    }

    @Path("/changeStatus/{propertyId}")
    @POST
    public Response changeStatus(@PathParam("propertyId") long propertyId) {
        int statusCode = propertyService.changeStatus(propertyId);
        return Response.status(Response.Status.fromStatusCode(statusCode)).build();
    }

    @Path("/delete/{propertyId}")
    @POST
    public Response delete(@PathParam("propertyId") long propertyId){
        User currentUser = userService.getCurrentlyLoggedUser();
        int statusCode = propertyService.delete(propertyId, currentUser);
        return Response.status(Response.Status.fromStatusCode(statusCode)).build();
    }
}
