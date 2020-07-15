package ar.edu.itba.paw.service;


import ar.edu.itba.paw.interfaces.Either;
import ar.edu.itba.paw.interfaces.dao.ProposalDao;
import ar.edu.itba.paw.interfaces.service.NotificationService;
import ar.edu.itba.paw.interfaces.service.PropertyService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Property;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Availability;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;


import java.net.HttpURLConnection;

import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class APProposalServiceTest {

    @InjectMocks
    private APProposalService proposalService = new APProposalService();

    @Mock
    private ProposalDao proposalDao;


    @Mock
    private UserService userService;

    @Mock
    private PropertyService propertyService;

    @Mock
    private MessageSource messageSource;

    @Mock
    private NotificationService notificationService;

    @Test
    public void createValidProposalCreateNewProposalTest(){
        long[] userIds = {};
        Proposal proposal = Factories.proposalCreator();
        User loggedUser = proposal.getCreator();

        Mockito.when(userService.getCurrentlyLoggedUser()).thenReturn(loggedUser);
        Mockito.when(proposalDao.create(any(Proposal.class), any(userIds.getClass()))).thenReturn(proposal);
        Mockito.when(propertyService.get(proposal.getProperty().getId())).thenReturn(proposal.getProperty());
        Mockito.when(proposalDao.findDuplicateProposal(any(Proposal.class), any(userIds.getClass()))).thenReturn(Long.valueOf(-1));
        Either<Proposal,String> maybeProposal = proposalService.createProposal(proposal.getProperty().getId(), userIds, null, null);

        Assert.assertNotNull(maybeProposal);
        Assert.assertTrue(maybeProposal.hasValue());
        Assert.assertEquals(proposal.getId(), maybeProposal.value().getId());


    }


    @Test
    public void createProposalWithNullPropertyReturnsErrorTest(){
        long invalidPropertyId = -1;
        Mockito.when(propertyService.get(invalidPropertyId)).thenReturn(null);

        Either<Proposal, String> maybeProposal = proposalService.createProposal(invalidPropertyId, new long[]{}, null, null);

        Assert.assertNotNull(maybeProposal);
        Assert.assertFalse(maybeProposal.hasValue());
        Assert.assertEquals("the given property does not exist", maybeProposal.alternative());
    }

    @Test
    public void createProposalWithMoreInterestedUsersThanCapacityReturnsError(){
        long propertyIdWithInvalidCapacity = 1;
        Property mockProperty = Factories.propertyCreatorCustomCapacity(1);
        long[] userIds = {1, 2, 4};
        Mockito.when(propertyService.get(propertyIdWithInvalidCapacity)).thenReturn(mockProperty);

        Either<Proposal, String> maybeProposal = proposalService.createProposal(propertyIdWithInvalidCapacity, userIds, null, null);

        Assert.assertNotNull(maybeProposal);
        Assert.assertFalse(maybeProposal.hasValue());
        Assert.assertEquals("the given proposal is invalid for this property", maybeProposal.alternative());
    }

    @Test
    public void createProposalForRentedPropertyReturnsError(){
        long rentedPropertyId = 1;
        Property mockProperty = Factories.propertyCreatorCustomAvailability(Availability.RENTED);
        Mockito.when(propertyService.get(rentedPropertyId)).thenReturn(mockProperty);

        Either<Proposal, String> maybeProposal = proposalService.createProposal(rentedPropertyId, new long[]{}, null, null);

        Assert.assertNotNull(maybeProposal);
        Assert.assertFalse(maybeProposal.hasValue());
        Assert.assertEquals("the given proposal is invalid for this property", maybeProposal.alternative());
    }

    @Test
    public void deleteReturnsHTTPErrorWhenCreatorDifferentToLoggedUserTest(){
        User loggedUser = Factories.userCreatorWithID(100);
        Proposal proposal = Factories.proposalCreator();

        Mockito.when(proposalDao.get(proposal.getId())).thenReturn(proposal);
        Mockito.when(userService.getCurrentlyLoggedUser()).thenReturn(loggedUser);

        int httpCode = proposalService.delete(proposal.getId(), null, null);

        Assert.assertEquals(HttpURLConnection.HTTP_NOT_FOUND, httpCode);
    }

    @Test
    public void deleteReturnsOKWhenLoggedUserEqualsProposalCreatorTest(){
        User loggedUser = Factories.userCreator();
        Proposal proposal = Factories.proposalCreator();

        Mockito.when(proposalDao.get(proposal.getId())).thenReturn(proposal);
        Mockito.when(userService.getCurrentlyLoggedUser()).thenReturn(loggedUser);
        Mockito.doNothing().when(proposalDao).dropProposal(proposal.getId());

        int httpCode = proposalService.delete(proposal.getId(), null, null);

        Assert.assertEquals(HttpURLConnection.HTTP_OK, httpCode);
    }


}

