package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.dao.PropertyDao;
import ar.edu.itba.paw.interfaces.dao.ProposalDao;
import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Availability;
import ar.edu.itba.paw.model.enums.ProposalState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.util.Collection;
import java.util.Locale;


@Service
public class APProposalService implements ProposalService {

    private final static String DELETE_SUBJECT_CODE= "notifications.proposals.deleted.subject";
    private final static String DELETE_BODY_CODE = "notifications.proposals.deleted";
    private final static String DELETE_EMAIL_BODY_CODE = "notifications.proposals.deleted.email";

    private final static String SENT_SUBJECT_CODE= "notifications.proposals.sent.subject";
    private final static String SENT_BODY_CODE = "notifications.proposals.sent";
    private final static String SENT_EMAIL_BODY_CODE = "notifications.proposals.sent.email";

    private final static String SENT_HOST_SUBJECT_CODE= "notifications.proposals.hostProposal.subject";
    private final static String SENT_HOST_BODY_CODE= "notifications.proposals.hostProposal";
    private final static String SENT_HOST_EMAIL_BODY_CODE= "notifications.proposals.hostProposal.email";

    private final static String DECLINE_SUBJECT_CODE= "notifications.proposals.declined.subject";
    private final static String DECLINE_BODY_CODE = "notifications.proposals.declined";
    private final static String DECLINE_EMAIL_BODY_CODE = "notifications.proposals.declined.email";

    private final static String INVITATION_SUBJECT_CODE= "notifications.proposals.invitation.subject";
    private final static String INVITATION_BODY_CODE = "notifications.proposals.invitation";
    private final static String INVITATION_EMAIL_BODY_CODE = "notifications.proposals.invitation.email";

    @Autowired
    private ProposalDao proposalDao;
    @Autowired
    private PropertyDao propertyDao;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private PropertyService propertyService;
    @Autowired
    private MessageSource messageSource;

    @Override
    public Either<Proposal, String> createProposal(long propertyId, long[] userIds, String url, Locale loc) {
        Property property = propertyService.get(propertyId);
        if (property == null) return Either.alternativeFrom("the given property does not exist");
        if (!validProperty(property, userIds)) return Either.alternativeFrom("the given proposal is invalid for this property");

        Proposal proposal = createProposal(property, userIds);

        // If duplicated proposal, return the old proposal.
        long duplicateId = proposalDao.findDuplicateProposal(proposal, userIds);
        if(duplicateId != -1){
            return Either.valueFrom(proposalDao.getWithRelatedEntities(duplicateId));
        }

        Proposal result = proposalDao.create(proposal, userIds);
        sendNotifications(result, url, loc);
        return Either.valueFrom(result);
    }

    private boolean validProperty(Property property, long[] ids) {
        if (property.getCapacity() < ids.length) return false;
        if (property.getAvailability() == Availability.RENTED) return false;
        return true;
    }

    private Proposal createProposal(Property property, long[] ids) {
        return new Proposal.Builder()
                        .withCreator(userService.getCurrentlyLoggedUser())
                        .withProperty(property)
                        .withState(ids.length == 0 ? ProposalState.SENT : ProposalState.PENDING)
                        .build();
    }

    private void sendNotifications(Proposal result, String url, Locale loc) {
        User u = userService.getCurrentlyLoggedUser();
        if (result.getUsersWithoutCreator(u.getId()).size() > 0) {
            String title = messageSource.getMessage(INVITATION_SUBJECT_CODE, new String[] { result.getProperty().getDescription() }, loc);
            String body = messageSource.getMessage(INVITATION_EMAIL_BODY_CODE, new String[] {url + "/proposal/" + result.getId()}, loc);
            notificationService.sendNotifications(INVITATION_SUBJECT_CODE,
                                                INVITATION_BODY_CODE,
                                                title,
                                                body,
                                                "/proposal/" + result.getId(),
                                                result.getUsers(),
                                                u.getId());
        }
        else {
            String title = messageSource.getMessage(SENT_HOST_SUBJECT_CODE, new String[] { result.getProperty().getDescription() }, loc);
            String body = messageSource.getMessage(SENT_HOST_EMAIL_BODY_CODE, new String[] {url + "/proposal/" + result.getId()}, loc);
            notificationService.sendNotification(SENT_HOST_SUBJECT_CODE,
                                                SENT_HOST_BODY_CODE,
                                                title,
                                                body,
                                                "/proposal/" + result.getId(),
                                                result.getProperty().getOwner());
        }
    }

    @Override
    public int delete(long id, String url, Locale loc) {
        Proposal proposal = proposalDao.get(id);
        User u = userService.getCurrentlyLoggedUser();
        if(proposal.getCreator().getId() != u.getId())
            return HttpURLConnection.HTTP_NOT_FOUND;
        String title = messageSource.getMessage(DELETE_SUBJECT_CODE, new String[] { proposal.getProperty().getDescription() }, loc);
        String body = messageSource.getMessage(DELETE_EMAIL_BODY_CODE, new String[] { url + "/proposal/" + proposal.getId() }, loc);
        notificationService.sendNotifications(DELETE_SUBJECT_CODE,
                                            DELETE_BODY_CODE,
                                            title,
                                            body,
                                            "/proposal/" + proposal.getId(),
                                            proposal.getUsers(),
                                            u.getId());
        proposalDao.dropProposal(proposal.getId());
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    public Proposal get(long id) {
        return proposalDao.get(id);
    }

    @Override
    public Proposal getWithRelatedEntities(long id) {
        Proposal p = proposalDao.getWithRelatedEntities(id);
        User u = userService.getCurrentlyLoggedUser();
        if (isRelatedToProposal(p, u)) return null;
        return p;
    }

    private boolean isRelatedToProposal(Proposal p, User u) {
        return p.getCreator().getId() != u.getId() &&
                !userIsInvitedToProposal(u, p) &&
                p.getProperty().getOwner().getId() != u.getId();
    }

    @Override
    public Collection<Proposal> getAllProposalForUserId(long id){
        return proposalDao.getAllProposalForUserId(id);
    }

    @Override
    public int setAcceptInvite(long proposalId, String url, Locale loc) {
        User u = userService.getCurrentlyLoggedUser();
        Proposal proposal = proposalDao.get(proposalId);
        if (!userIsInvitedToProposal(u, proposal))
            return HttpURLConnection.HTTP_FORBIDDEN;
        proposalDao.setAcceptInvite(u.getId(), proposalId);
        sendProposalSentNotifications(u, proposalDao.get(proposalId), url, loc);
        return HttpURLConnection.HTTP_OK;
    }

    private void sendProposalSentNotifications(User u, Proposal proposal, String url, Locale loc) {
        if (proposal.isCompletelyAccepted(proposal.getCreator().getId())) {
            String title = messageSource.getMessage(SENT_SUBJECT_CODE, new String[] { proposal.getProperty().getDescription() }, loc);
            String body = messageSource.getMessage(SENT_EMAIL_BODY_CODE, new String[] { url + "/proposal/" + proposal.getId() }, loc);
            notificationService.sendNotifications(SENT_SUBJECT_CODE,
                                                SENT_BODY_CODE,
                                                title,
                                                body,
                                                "/proposal/" + proposal.getId(),
                                                proposal.getUsers(),
                                                u.getId());
            Property property = propertyDao.getPropertyWithRelatedEntities(proposal.getProperty().getId());
            title = messageSource.getMessage(SENT_HOST_SUBJECT_CODE, new String[] { proposal.getProperty().getDescription() }, loc);
            body = messageSource.getMessage(SENT_HOST_EMAIL_BODY_CODE, new String[] { url + "/proposal/" + proposal.getId() }, loc);
            notificationService.sendNotification(SENT_HOST_SUBJECT_CODE,
                                                SENT_HOST_BODY_CODE,
                                                title,
                                                body,
                                                "/proposal/" + proposal.getId(),
                                                property.getOwner());
            proposalDao.setState(proposal.getId(), ProposalState.SENT);
        }
    }

    private boolean userIsInvitedToProposal(User user, Proposal proposal){
        for (User invitedUser: proposal.getUsers())
            if (invitedUser.getId() == user.getId())
                return true;
        return false;
    }

    @Override
    public int setDeclineInvite(long proposalId, String url, Locale loc) {
        Proposal proposal = proposalDao.getWithRelatedEntities(proposalId);
        User currentUser = userService.getCurrentlyLoggedUser();
        if(proposal == null)
            return HttpURLConnection.HTTP_NOT_FOUND;
        if (!userIsInvitedToProposal(currentUser, proposal))
            return HttpURLConnection.HTTP_FORBIDDEN;
        delete(proposalId, url, loc);

        String title = messageSource.getMessage(DECLINE_SUBJECT_CODE, new String[] { proposal.getProperty().getDescription() }, loc);
        String body = messageSource.getMessage(DECLINE_EMAIL_BODY_CODE, new String[] { url + "/proposal/" + proposal.getId() }, loc);
        notificationService.sendNotifications(DECLINE_SUBJECT_CODE,
                                            DECLINE_BODY_CODE,
                                            title,
                                            body,
                                            "/proposal/" + proposal.getId(),
                                            proposal.getUsers(),
                                            currentUser.getId());
        proposalDao.setDeclineInvite(currentUser.getId(), proposalId);
        return HttpURLConnection.HTTP_OK;
    }

    @Override
    public int setState(long proposalId, ProposalState state, String url, Locale loc) {
        if (!userOwnsProposalProperty(proposalId))
            return HttpURLConnection.HTTP_FORBIDDEN;
        proposalDao.setState(proposalId, state);
        if (state == ProposalState.ACCEPTED || state == ProposalState.DECLINED) {
            Proposal proposal = proposalDao.get(proposalId);
            User currentUser = userService.getCurrentlyLoggedUser();
            String subjectCode = state == ProposalState.ACCEPTED?"notifications.proposals.accepted.subject":"notifications.proposals.declined.subject";
            String textCode = state == ProposalState.ACCEPTED?"notifications.proposals.accepted":"notifications.proposals.declined";
            String bodyCode = state == ProposalState.ACCEPTED?"notifications.proposals.accepted.email":"notifications.proposals.declined.email";
            String title = messageSource.getMessage(subjectCode, new String[] { proposal.getProperty().getDescription() }, loc);
            String body = messageSource.getMessage(bodyCode, new String[] { url + "/proposal/" + proposal.getId() }, loc);
            notificationService.sendNotifications(subjectCode,
                                                textCode,
                                                title,
                                                body,
                                                "/proposal/" + proposal.getId(),
                                                proposal.getUsers(),
                                                currentUser.getId());
            if (state == ProposalState.ACCEPTED)
                propertyDao.changeStatus(proposal.getProperty().getId());
        }
        return HttpURLConnection.HTTP_OK;
    }

    private boolean userOwnsProposalProperty(long proposalId){
        User u = userService.getCurrentlyLoggedUser();
        Proposal proposal = proposalDao.getWithRelatedEntities(proposalId);
        return (proposal != null && proposal.getProperty().getOwner().getId() == u.getId());
    }

    @Override
    public Collection<Proposal> getProposalsForOwnedProperties(User profileUser) {
        return proposalDao.getProposalsForOwnedProperties(profileUser.getId());
    }
}
