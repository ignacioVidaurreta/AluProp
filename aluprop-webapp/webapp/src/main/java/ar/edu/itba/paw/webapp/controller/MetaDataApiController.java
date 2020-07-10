package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CareerService;
import ar.edu.itba.paw.interfaces.service.UniversityService;
import ar.edu.itba.paw.webapp.dto.CareerDto;
import ar.edu.itba.paw.webapp.dto.UniversityDto;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("data")
@Produces(value = {MediaType.APPLICATION_JSON})
public class MetaDataApiController {

    @Autowired
    private UniversityService universityService;
    @Autowired
    private CareerService careerService;


    @GET
    @Path("/university")
    public Response university() {
        return Response.ok(universityService.getAll()
                .stream()
                .map(UniversityDto::fromUniversity)
                .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/career")
    public Response career() {
        return Response.ok(careerService.getAll()
                .stream()
                .map(CareerDto::fromCareer)
                .collect(Collectors.toList()))
                .build();
    }

}
