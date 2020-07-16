package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.helper_classes.JwtTokenHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

@Produces(value = {MediaType.APPLICATION_JSON})
@Consumes(value = {MediaType.APPLICATION_JSON})
@Path("auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private static final String NO_LANGUAGE_ERROR = "This endpoint requires a language";

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenHandler tokenHandler;

    @POST
    @Path("/logout")
    public Response logout() {
        return Response.ok().build();
    }

    @POST
    @Path("/signup")
    public Response signup(@RequestBody final String requestBody, @Context HttpServletRequest request) {
        SignUpForm signUpForm = signUpForm(requestBody);
        final String language = request.getHeader("Accept-Language");
        if (language == null)
            return Response.status(Response.Status.BAD_REQUEST).entity(NO_LANGUAGE_ERROR).build();
        Locale loc = new Locale(language);
        Either<User, List<String>> maybeUser = userService.CreateUser(buildUserFromForm(signUpForm),
                loc,
                getBaseUrl(request.getRequestURL().toString(),
                        "/api/auth/signup"));
        if(!maybeUser.hasValue()) {
            logger.debug("Invalid user creation");
            return Response.status(Response.Status.BAD_REQUEST).entity(maybeUser.alternative()).build();
        }
        return Response.ok(maybeUser.value()).header("X-TOKEN", tokenHandler.createToken(maybeUser.value())).build();
    }

    private SignUpForm signUpForm(String json) {
        try {
            logger.error(json);
            return new ObjectMapper().readValue(json, SignUpForm.class);
        }
        catch (IOException e) {
            throw new IllegalArgumentException(String.format("malformed json:  %s", e));
        }
    }

    private User buildUserFromForm(@ModelAttribute("signUpForm") @Valid SignUpForm form) {
        return new User.Builder()
                .withEmail(form.getEmail())
                .withGender(Gender.valueOf(form.getGender()))
                .withName(form.getName())
                .withLastName(form.getLastName())
                .withPasswordHash(passwordEncoder.encode(form.getPassword()))
                .withUniversityId(form.getUniversityId())
                .withCareerId(form.getCareerId())
                .withBio(form.getBio())
                .withBirthDate(Date.valueOf(form.getBirthDate()))
                .withContactNumber(form.getContactNumber())
                .withRole(Role.valueOf(form.getRole()))
                .build();
    }

    private String getBaseUrl(String url, String currentEndpoint) {
        return url.substring(0, url.indexOf(currentEndpoint));
    }
}
