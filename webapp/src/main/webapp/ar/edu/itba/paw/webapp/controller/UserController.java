package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.service.CareerService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.UniversityService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.Utilities.UserUtility;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private CareerService careerService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping("/logIn")
    public ModelAndView login() {
        return new ModelAndView("logInForm");
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.GET )
    public ModelAndView signUp(@ModelAttribute("signUpForm") final SignUpForm form) {
        ModelAndView mav = new ModelAndView("signUpForm");
        mav.addObject("universities", universityService.getAll());
        mav.addObject("careers", careerService.getAll());
        return mav;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST )
    public ModelAndView register(@Valid @ModelAttribute("signUpForm") SignUpForm form, final BindingResult errors) {

        if(errors.hasErrors()){
            return signUp(form);
        }
        else if (!form.getRepeatPassword().equals(form.getPassword())){
            form.setRepeatPassword("");
            return signUp(form).addObject("passwordMatch", false);
        }
        userService.CreateUser(new User.Builder()
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
                                        .build());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile() {
        String email = UserUtility.getUsernameOfCurrentlyLoggedUser(SecurityContextHolder.getContext());
        User u = userService.getUserWithRelatedEntitiesByEmail(email);
        ModelAndView mav = new ModelAndView("profile").addObject("user", u);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("userRole", auth.getAuthorities());
        return mav;
    }

    @RequestMapping(value = "/interested/{propertyId}")
    public ModelAndView interested(@PathVariable long propertyId,
                                   @RequestParam("pageNumber") int pageNumber,
                                   @RequestParam("pageSize") int pageSize) {
        return new ModelAndView("interested")
                        .addObject("users",
                                    userService.getUsersInterestedInProperty(propertyId, new PageRequest(pageNumber, pageSize)));
    }

    @RequestMapping(value = "/interestEmail/{propertyId}")
    public ModelAndView interestEmail(@PathVariable long propertyId, @RequestParam String email) {
        User currentUser = UserUtility.getCurrentlyLoggedUser(SecurityContextHolder.getContext(), userService);
        Property property = propertyService.get(propertyId);
        String title = redactTitle(currentUser);
        String body = redactBody(currentUser, property);
        sendEmail(title, body, email);
        return new ModelAndView("interestEmailSuccess");
    }

    private String redactTitle(User user) {
        return "Mail de test de la funcionalidad de mail de aluprop";
    }

    private String redactBody(User user, Property property) {
        return "Solo te envío este mail para checkear que funciona el enviado de mail." +
                "Si te llega, sabés que anda y podés ponerte a trabajar en eso." +
                "De paso te informo que sos re crack, que tengas un buen resto del día.";
    }

    private void sendEmail(String title, String body, String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(title);
        message.setText(body);
        emailSender.send(message);
    }
}
