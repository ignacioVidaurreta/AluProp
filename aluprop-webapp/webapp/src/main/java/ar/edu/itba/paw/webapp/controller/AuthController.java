package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.helperClasses.JwtTokenHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.sql.Date;
import java.util.List;
import java.util.Locale;

public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenHandler tokenHandler;

    @POST
    @Path("/signup")
    public Response signup(SignUpForm signUpForm,
                           Locale loc,
                           final BindingResult errors,
                           HttpServletRequest request) {
        if (errors.hasErrors())
            return Response.status(Response.Status.BAD_REQUEST).build();
        Either<User, List<String>> maybeUser = userService.CreateUser(buildUserFromForm(signUpForm),
                loc,
                getBaseUrl(request.getRequestURL().toString(),
                        "/user/signUp"));
        if(!maybeUser.hasValue()) {
            logger.debug("NOT A UNIQUE EMAIL");
            return Response.status(Response.Status.BAD_REQUEST).entity("Username already exists").build();
        }
        return Response.ok(maybeUser.value()).header("X-TOKEN", tokenHandler.createToken(maybeUser.value())).build();
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
                .withContactNumber(form.getPhoneNumber())
                .withRole(Role.valueOf(form.getRole()))
                .build();
    }

    private String getBaseUrl(String url, String currentEndpoint) {
        return url.substring(0, url.indexOf(currentEndpoint));
    }
}
