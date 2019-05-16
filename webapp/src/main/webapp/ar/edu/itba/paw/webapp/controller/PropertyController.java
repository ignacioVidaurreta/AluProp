package ar.edu.itba.paw.webapp.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.*;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.PageRequest;
import ar.edu.itba.paw.interfaces.PageResponse;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.enums.PropertyType;
import ar.edu.itba.paw.webapp.Utilities.StatusCodeUtility;
import ar.edu.itba.paw.webapp.Utilities.UserUtility;
import ar.edu.itba.paw.webapp.form.PropertyCreationForm;
import ar.edu.itba.paw.webapp.form.ProposalForm;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private NeighbourhoodService neighbourhoodService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView index(@RequestParam int pageNumber, int pageSize) {
        final ModelAndView mav = new ModelAndView("index");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("userRole", auth.getAuthorities());
        PageResponse<Property> response = propertyService.getAll(new PageRequest(pageNumber, pageSize));
        mav.addObject("properties", response.getResponseData());
        mav.addObject("currentPage", response.getPageNumber());
        mav.addObject("totalPages", response.getTotalPages());
        return mav;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public ModelAndView get(@ModelAttribute("proposalForm") final ProposalForm form, @PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("detailedProperty");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("userRole", auth.getAuthorities());
        mav.addObject("property", propertyService.getPropertyWithRelatedEntities(id));
        return mav;
    }

    @RequestMapping(value = "{id}/interest", method = RequestMethod.POST)
    public ModelAndView interest(@PathVariable(value = "id") int propertyId) {
        User user = UserUtility.getCurrentlyLoggedUser(SecurityContextHolder.getContext(), userService);
        final int code = propertyService.showInterestOrReturnErrors(propertyId, user);
        return StatusCodeUtility.parseStatusCode(code, "redirect:/" + propertyId);
    }

    @RequestMapping(value = "/host/create", method = RequestMethod.GET)
    public ModelAndView create(@ModelAttribute("propertyCreationForm") final PropertyCreationForm form) {
        return ModelAndViewWithPropertyCreationAttributes();
    }

    private ModelAndView create(Collection<String> errors) {
        ModelAndView mav = ModelAndViewWithPropertyCreationAttributes();
        mav.addObject("errors", errors);
        return mav;
    }

    private ModelAndView ModelAndViewWithPropertyCreationAttributes() {
        ModelAndView mav = new ModelAndView("createProperty");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        mav.addObject("userRole", auth.getAuthorities());
        mav.addObject("rules", ruleService.getAll());
        mav.addObject("services", serviceService.getAll());
        mav.addObject("neighbourhoods", neighbourhoodService.getAll());
        return mav;
    }

    @RequestMapping(value = "/host/create/uploadPictures", method = RequestMethod.POST)
    public @ResponseBody ModelAndView uploadPictures(@RequestParam("file") MultipartFile[] files, @ModelAttribute PropertyCreationForm form, final BindingResult errors) {
        long[] imageArray = new long[files.length];
        for (int i = 0; i < files.length; i++)
            imageArray[i] = imageService.create(files[i]);
        form.setMainImageId(imageArray[0]);
        form.setImageIds(imageArray);
        return create(form, errors);
    }

    @RequestMapping(value = "/host/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute PropertyCreationForm propertyForm, final BindingResult errors) {
        if (errors.hasErrors()){
            return create(propertyForm);
        }
        Either<Property, Collection<String>> propertyOrErrors = propertyService.create(
                new Property.Builder()
                    .withCaption(propertyForm.getCaption())
                    .withDescription(propertyForm.getDescription())
                    .withNeighbourhoodId(propertyForm.getNeighbourhoodId())
                    .withPrice(propertyForm.getPrice())
                    .withPropertyType(PropertyType.valueOf(propertyForm.getPropertyType()))
                    .withPrivacyLevel(propertyForm.getPrivacyLevel() > 0)
                    .withCapacity(propertyForm.getCapacity())
                    .withMainImageId(propertyForm.getMainImageId())
                    .withServices(generateObjects(propertyForm.getServiceIds(), Service::new))
                    .withRules(generateObjects(propertyForm.getRuleIds(), Rule::new))
                    .withImages(generateObjects(propertyForm.getImageIds(), Image::new))
                    .withOwnerId(UserUtility.getCurrentlyLoggedUser(SecurityContextHolder.getContext(), userService).getId())
                    .build()
        );
        if(propertyOrErrors.hasValue())
            return new ModelAndView("redirect:/" + propertyOrErrors.value().getId());
        else
            return create(propertyOrErrors.alternative());
    }

    private <T> Collection<T> generateObjects(long[] objectIds, LongFunction<T> function) {
        if(objectIds != null && objectIds.length > 0)
            return Arrays.stream(objectIds).mapToObj(function).collect(Collectors.toList());
        else
            return new LinkedList<>();
    }

    @RequestMapping(value = "/interestsOfUser/{userId}")
    public ModelAndView interestsOfUser(@PathVariable(value = "userId") long userId) {
        return new ModelAndView("interestsOfUser")
                    .addObject("interests", propertyService.getInterestsOfUser(userId));
    }
}
