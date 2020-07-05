package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.UserProposalState;
import ar.edu.itba.paw.webapp.form.FilteredSearchForm;
import ar.edu.itba.paw.webapp.helperClasses.ModelAndViewPopulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

@Controller
@RequestMapping("/proposal")
public class ProposalController {

    @Autowired
    private ProposalService proposalService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private UserService userService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private NeighbourhoodService neighbourhoodService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ModelAndViewPopulator navigationUtility;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request, @PathVariable("id") long id, @ModelAttribute FilteredSearchForm searchForm) {
        String notificationId = request.getParameter("notificationId");
        if (notificationId != null)
            notificationService.markRead(Long.parseLong(notificationId));
        final ModelAndView mav = navigationUtility.mavWithNavigationAttributes();
        final User u = userService.getCurrentlyLoggedUser();
        final Proposal proposal = proposalService.getWithRelatedEntities(id);
        if (proposal == null) {
            mav.setViewName("404");
            return mav;
        }
        final Property property = propertyService.get(proposal.getProperty().getId());
        final User creator = userService.get(proposal.getCreator().getId());
        if (proposal.getCreator().getId() != u.getId() && !userIsInvitedToProposal(u, proposal) && property.getOwner().getId() != u.getId())
            return new ModelAndView("404").addObject("currentUser", u);
        if (userIsInvitedToProposal(u, proposal)){
            mav.addObject("isInvited", true);
            mav.addObject("hasReplied", userHasRepliedToProposal(u, proposal));
            mav.addObject("budget", proposal.budget());
        }

        mav.addObject("property", property);
        mav.addObject("proposal", proposal);
        mav.addObject("proposalUsers", proposal.getUsersWithoutCreator(creator.getId()));
        mav.addObject("creator", creator);
        mav.addObject("currentUser", u);
        mav.addObject("userStates", proposal.getUserStates(creator.getId()));
        addSearchObjectsToMav(mav);
        mav.setViewName("proposal");
        return mav;
    }

    private void addSearchObjectsToMav(ModelAndView mav){
        mav.addObject("neighbourhoods", neighbourhoodService.getAll());
        mav.addObject("rules", ruleService.getAll());
        mav.addObject("services", serviceService.getAll());
    }

    private boolean userIsInvitedToProposal(User user, Proposal proposal){
        for (User invitedUser: proposal.getUsers())
            if (invitedUser.getId() == user.getId())
                return true;
        return false;
    }

    private boolean userHasRepliedToProposal(User user, Proposal proposal){
        for (UserProposal userProp: proposal.getUserProposals()){
            if (userProp.getUser().getId() == user.getId() && userProp.getState() != UserProposalState.PENDING)
                return true;
        }
        return false;
    }
}
