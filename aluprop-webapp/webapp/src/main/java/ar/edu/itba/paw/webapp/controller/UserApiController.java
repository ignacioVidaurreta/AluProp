package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CareerService;
import ar.edu.itba.paw.interfaces.service.UniversityService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("user")
@Produces(value = { MediaType.APPLICATION_JSON })
public class UserApiController {

    @Autowired
    private UserService userService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private CareerService careerService;

    @GET
    public Response currentUser() {
        return Response.ok(UserDto.fromUser(userService.getCurrentlyLoggedUser())).build();
    }


    @GET
    @Path("/{userId}")
    public Response user(@PathParam("userId") long userId) {
        return Response.ok(UserDto.fromUser(userService.getWithRelatedEntities(userId))).build();
    }

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

    @GET
    @Path("/loginStatus")
    public Response isLoggedIn(){
        return Response.ok(userService.getCurrentlyLoggedUser() != null).build();
    }
}
