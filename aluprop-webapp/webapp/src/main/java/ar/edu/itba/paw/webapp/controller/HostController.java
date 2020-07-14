package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.model.enums.ProposalState;
import ar.edu.itba.paw.webapp.form.FilteredSearchForm;
import ar.edu.itba.paw.webapp.form.PropertyCreationForm;
import ar.edu.itba.paw.webapp.form.ProposalForm;
import ar.edu.itba.paw.webapp.helper_classes.ModelAndViewPopulator;
import ar.edu.itba.paw.webapp.helper_classes.StatusCodeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("host/")
public class    HostController {

    @Autowired
    private ModelAndViewPopulator navigationUtility;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private StatusCodeParser statusCodeParser;

    @RequestMapping(value = "/decline/{proposalId}", method = RequestMethod.POST )
    public ModelAndView hostDecline(@PathVariable(value = "proposalId") int proposalId,
                                    @Valid @ModelAttribute("proposalForm") ProposalForm form) {
        final ModelAndView mav = new ModelAndView("redirect:/proposal/" + proposalId);
        int statusCode = proposalService.setState(proposalId, ProposalState.DECLINED);
        statusCodeParser.parseStatusCode(statusCode, mav);
        return mav;
    }

    @RequestMapping(value = "/accept/{proposalId}", method = RequestMethod.POST )
    public ModelAndView hostAccept(@PathVariable(value = "proposalId") int proposalId,
                                   @Valid @ModelAttribute("proposalForm") ProposalForm form) {
        final ModelAndView mav = new ModelAndView("redirect:/proposal/" + proposalId);
        final int statusCode = proposalService.setState(proposalId, ProposalState.ACCEPTED);
        statusCodeParser.parseStatusCode(statusCode, mav);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create(@ModelAttribute("propertyCreationForm") final PropertyCreationForm form,
                               @ModelAttribute("filteredSearchForm") FilteredSearchForm searchForm) {
        return navigationUtility.mavWithNavigationAttributes("createProperty");
    }
}
