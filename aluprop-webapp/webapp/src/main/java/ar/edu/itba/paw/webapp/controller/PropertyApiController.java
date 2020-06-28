package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.SearchableProperty;
import ar.edu.itba.paw.interfaces.service.NeighbourhoodService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.RuleService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.beanParams.PropertySearchRequest;
import ar.edu.itba.paw.webapp.dto.IndexPropertyDto;
import ar.edu.itba.paw.webapp.dto.NeighbourhoodDto;
import ar.edu.itba.paw.webapp.dto.ProposalDto;
import ar.edu.itba.paw.webapp.dto.RuleDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("property")
@Produces(value = { MediaType.APPLICATION_JSON })
public class PropertyApiController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private NeighbourhoodService neighbourhoodService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private UserService userService;

    @GET
    public Response index(@BeanParam PropertySearchRequest propertySearchRequest,
                          @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                          @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Property> properties = propertyService.getAll(new PageRequest(pageNumber, pageSize),
                                                                propertySearchRequest.getOrderBy());
        PageResponse<IndexPropertyDto> response = new PageResponse<>(properties.getPageNumber(),
                                                                    properties.getPageSize(),
                                                                    properties.getTotalItems(),
                                                                    properties.getResponseData()
                                                                            .stream()
                                                                            .map(IndexPropertyDto::fromProperty)
                                                                            .collect(Collectors.toList()));
        return Response.ok(response).build();
    }

    @Path("search")
    @GET
    public Response search(@BeanParam PropertySearchRequest propertySearchRequest,
                           @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                           @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        SearchableProperty property = new SearchableProperty.Builder()
                .withCapacity(propertySearchRequest.getCapacity())
                .withDescription(propertySearchRequest.getDescription())
                .withMaxPrice(propertySearchRequest.getMaxPrice())
                .withMinPrice(propertySearchRequest.getMinPrice())
                .withNeighbourhoodId(propertySearchRequest.getNeighbourhoodId())
                .withPrivacyLevel(propertySearchRequest.getPrivacyLevelAsEnum())
                .withPropertyType(propertySearchRequest.getPropertyTypeAsEnum())
                .withRuleIds(propertySearchRequest.getRuleIds())
                .withServiceIds(propertySearchRequest.getServiceIds())
                .withPropertyOrder(propertySearchRequest.getOrderBy())
                .build();
        PageRequest request = new PageRequest(pageNumber, pageSize);
        PageResponse<Property> properties = propertyService.advancedSearch(request, property);
        Collection<IndexPropertyDto> response = properties.getResponseData().stream()
                                                                            .map(IndexPropertyDto::fromProperty)
                                                                            .collect(Collectors.toList());
        PageResponse<IndexPropertyDto> pageResponse = new PageResponse(pageNumber, pageSize, properties.getTotalItems(), response);
        return Response.ok(pageResponse).build();
    }

    @Path("/{propertyId}")
    @GET
    public Response property(@PathParam("propertyId") long propertyId) {
        return Response.ok(IndexPropertyDto.fromProperty(propertyService.get(propertyId))).build();
    }

    @Path("/neighbourhood")
    @GET
    public Response neighbourhoods() {
       return Response.ok(neighbourhoodService.getAll()
                                            .stream()
                                            .map(NeighbourhoodDto::fromNeighbourhood)
                                            .collect(Collectors.toList()))
                    .build();
    }

    @Path("/rule")
    @GET
    public Response rules() {
        return Response.ok(ruleService.getAll()
                                    .stream()
                                    .map(RuleDto::fromRule)
                                    .collect(Collectors.toList()))
                .build();
    }

    @Path("/{propertyId}/guest/interest")
    @POST
    public Response interest(@PathParam("propertyId") long propertyId) {
        User user = userService.getCurrentlyLoggedUser();
        return Response.ok(propertyService.showInterestOrReturnErrors(propertyId, user)).build();
    }

    @Path("/{propertyId}/guest/uninterested")
    @POST
    public Response uninterested(@PathParam("propertyId") long propertyId) {
        User user = userService.getCurrentlyLoggedUser();
        return Response.ok(propertyService.undoInterestOrReturnErrors(propertyId, user)).build();
    }
}