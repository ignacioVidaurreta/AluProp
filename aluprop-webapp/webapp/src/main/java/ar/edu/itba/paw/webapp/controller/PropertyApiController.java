package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.webapp.beanParams.PropertySearchRequest;
import dto.IndexPropertyDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("property")
@Produces(value = { MediaType.APPLICATION_JSON })
public class PropertyApiController {

    @Autowired
    private PropertyService propertyService;

    @GET
    public Response index(@BeanParam PropertySearchRequest propertySearchRequest,
                          @QueryParam("pageNumber") @DefaultValue("0") int pageNumber,
                          @QueryParam("pageSize") @DefaultValue("12") int pageSize) {
        PageResponse<Property> properties = propertyService.getAll(new PageRequest(pageNumber, pageSize),
                                                                propertySearchRequest.getOrderBy());
        PageResponse<IndexPropertyDto> response =
                new PageResponse<>(properties.getPageNumber(),
                                                properties.getPageSize(),
                                                properties.getTotalItems(),
                                                properties.getResponseData()
                                                        .stream()
                                                        .map(IndexPropertyDto::fromProperty)
                                                        .collect(Collectors.toList()));
        return Response.ok(response).build();
    }
}