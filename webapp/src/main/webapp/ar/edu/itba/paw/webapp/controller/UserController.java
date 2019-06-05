package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.APJavaMailSender;
import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.model.exceptions.IllegalUserStateException;
import ar.edu.itba.paw.webapp.Utilities.UserUtility;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.form.FilteredSearchForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(PropertyController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private CareerService careerService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public APJavaMailSender emailSender;
    @Autowired
    public NeighbourhoodService neighbourhoodService;
    @Autowired
    public RuleService ruleService;
    @Autowired
    public ServiceService serviceService;

    @RequestMapping("/logIn")
    public ModelAndView login(@ModelAttribute FilteredSearchForm searchForm) {
        return new ModelAndView("logInForm");
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.GET )
    public ModelAndView signUp(@ModelAttribute("signUpForm") final SignUpForm form,
                               @ModelAttribute FilteredSearchForm searchForm) {
        User user = UserUtility.getCurrentlyLoggedUser(SecurityContextHolder.getContext(), userService);
        ModelAndView mav = new ModelAndView("signUpForm");
        mav.addObject("currentUser", user);
        mav.addObject("universities", universityService.getAll());
        mav.addObject("careers", careerService.getAll());
        mav.addObject("neighbourhoods", neighbourhoodService.getAll());
        mav.addObject("rules", ruleService.getAll());
        mav.addObject("services", serviceService.getAll());

        return mav;
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST )
    public ModelAndView register(@Valid @ModelAttribute("signUpForm") SignUpForm form,
                                 final BindingResult errors,
                                 @ModelAttribute FilteredSearchForm searchForm) {
        if(errors.hasErrors()){
            return signUp(form, searchForm);
        }
        else if (!form.getRepeatPassword().equals(form.getPassword())){
            form.setRepeatPassword("");
            return signUp(form, searchForm).addObject("passwordMatch", false);
        }

        try {
            Either<User, List<String>> maybeUser = userService.CreateUser(buildUserFromForm(form));
            if(!maybeUser.hasValue()){
                form.setEmail("");
                logger.error("NOT A UNIQUE EMAIL");
                return signUp(form, searchForm).addObject("uniqueEmail", false);
            }
            User user = maybeUser.value();
            String title = redactConfirmationTitle(user);
            String body = redactConfirmationBody();
            emailSender.sendEmailToSingleUser(title, body, user);
            logger.debug("Confirmation email sent to: " + user.getEmail());

            return new ModelAndView("redirect:/");
        }
        catch(IllegalUserStateException e) {
            return new ModelAndView("404");
        }
    }

    private String redactConfirmationTitle(User user) {
        return "Hola " + user.getName() + ", bienvenido a AluProp!";
    }

    private String redactConfirmationBody() {
        return "Muchas gracias por elegirnos!\n Ingresa a http://pawserver.it.itba.edu.ar/paw-2019a-5/ para ver las propiedades disponibles, mostrar tu interés y crear propuestas, o bien publicar tus propiedades!\nSaludos,\nEl equipo de AluProp.";
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

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(@ModelAttribute FilteredSearchForm searchForm) {
        String email = UserUtility.getUsernameOfCurrentlyLoggedUser(SecurityContextHolder.getContext());
        User u = userService.getUserWithRelatedEntitiesByEmail(email);
        if (u == null)
            return new ModelAndView("404");
        ModelAndView mav = new ModelAndView("profile").addObject("user", u);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Proposal> proposals = (List<Proposal>) proposalService.getAllProposalForUserId(u.getId());
        mav.addObject("currentUser", u);
        mav.addObject("userRole", auth.getAuthorities());
        mav.addObject("interests", propertyService.getInterestsOfUser(u.getId()));
        mav.addObject("neighbourhoods", neighbourhoodService.getAll());
        mav.addObject("rules", ruleService.getAll());
        mav.addObject("services", serviceService.getAll());
        mav.addObject("proposals", proposals);
        if (proposals != null)
            mav.addObject("proposalPropertyNames", generatePropertyNames(proposals));

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

    private List<String> generatePropertyNames(List<Proposal> list){
        List<String> result = new ArrayList<>();
        for (Proposal prop: list)
            result.add(propertyService.get(prop.getPropertyId()).getDescription());
        return result;
    }
}
