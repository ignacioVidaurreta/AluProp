package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.service.CareerService;
import ar.edu.itba.paw.interfaces.service.UniversityService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.helperClasses.JwtTokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;
import java.util.Locale;
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
