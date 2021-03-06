package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.SearchableProperty;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.bean_params.PropertySearchRequest;
import ar.edu.itba.paw.webapp.dto.*;
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
    private ServiceService serviceService;

    @GET
    public Response index(@BeanParam PropertySearchRequest propertySearchRequest,
                          @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                          @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Property> properties = propertyService.getAll(new PageRequest(pageNumber, pageSize),
                                                                propertySearchRequest.getOrderBy());
        PageResponse<IndexPropertyDto> response = new PageResponse<>(properties, properties.getResponseData()
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
        PageResponse<IndexPropertyDto> pageResponse = new PageResponse(properties, response);
        return Response.ok(pageResponse).build();
    }

    @Path("/{propertyId}")
    @GET
    public Response property(@PathParam("propertyId") long propertyId) {
        Property property = propertyService.getPropertyWithRelatedEntities(propertyId);
        if( property == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(PropertyDto.fromProperty(property)).build();
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

    @Path("/service")
    @GET
    public Response service() {
        return Response.ok(serviceService.getAll()
                                        .stream()
                                        .map(ServiceDto::fromService)
                                        .collect(Collectors.toList()))
                .build();
    }


    @Path("/{propertyId}/interestedUsers")
    @GET
    public Response getInterestedUsersByPropertyId(@PathParam("propertyId") long propertyId){

        Collection<User> interestedUsers = propertyService.getPropertyWithRelatedEntities(propertyId).getInterestedUsers();
        return Response.ok(
                interestedUsers.stream()
                .map(IndexUserDto::fromUser)
                .collect(Collectors.toList()))
                .build();
    }
}