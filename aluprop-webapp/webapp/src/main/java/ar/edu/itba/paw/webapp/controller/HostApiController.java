package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.service.ImageService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.ProposalState;
import ar.edu.itba.paw.model.exceptions.IllegalPropertyStateException;
import ar.edu.itba.paw.webapp.dto.ImageDto;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.IndexProposalDto;
import ar.edu.itba.paw.webapp.dto.PropertyDto;
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
import java.util.stream.Collectors;

@Path("host")
@Produces(MediaType.APPLICATION_JSON)
public class HostApiController {

    private static final String NO_LANGUAGE_ERROR = "This endpoint requires a language";

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
    public Response proposals(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                              @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Proposal> proposals = userService.getHostProposals(new PageRequest(pageNumber, pageSize));
        PageResponse<IndexProposalDto> proposalDtos = new PageResponse<>(proposals,
                proposals.getResponseData()
                        .stream()
                        .map(IndexProposalDto::fromProposal)
                        .collect(Collectors.toList()));
        return Response.ok(proposalDtos).build();
    }

    @GET
    @Path("/properties")
    public Response getProperties(@QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                                  @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Property> properties = userService.getCurrentUserProperties(new PageRequest(pageNumber, pageSize));
        PageResponse<IndexPropertyDto> propertyDtos = new PageResponse<>(properties,
                                                    properties.getResponseData()
                                                            .stream()
                                                            .map(IndexPropertyDto::fromProperty)
                                                            .collect(Collectors.toList()));
        return Response.ok(propertyDtos).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/createProperty")
    public Response handlePropertyCreationErrors(final PropertyDto propertyDto) {
        try {
            Property property = createProperty(propertyDto);
            return Response.ok(PropertyDto.fromProperty(property)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

    }

    private Property createProperty(PropertyDto propertyDto) {
        createImages(propertyDto);
        long userId = userService.getCurrentlyLoggedUser().getId();
        Property property = propertyDto.toProperty(userService.getWithRelatedEntities(userId));
        Either<Property, Collection<String>> maybeProperty = propertyService.create(property);
        if (!maybeProperty.hasValue())
            throw new IllegalPropertyStateException(maybeProperty.alternative().toString());
        return property;
    }

    private void createImages(PropertyDto propertyDto) {
        long[] ids = new long[propertyDto.getImages().size()];
        int i = 0;
        for (ImageDto image : propertyDto.getImages()) {
            ids[i] = imageService.create(image.getImage());
            if (i == 0)
                propertyDto.setMainImageId(ids[0]);
            i++;
        }
        propertyDto.setImageIds(ids);
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

    @Path("{proposalId}/accept")
    @POST
    public Response accept(@PathParam("proposalId") long proposalId,
                           @Context HttpServletRequest request) {
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        String url = request.getRequestURL().toString();
        String host = url.substring(0, url.indexOf("/api/host/" + proposalId + "/accept"));
        return Response.status(proposalService.setState(proposalId, ProposalState.ACCEPTED, host, loc)).build();
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
        String host = url.substring(0, url.indexOf("/api/host/" + proposalId + "/decline"));
        return Response.status(proposalService.setState(proposalId, ProposalState.DECLINED, host, loc)).build();
    }
}
