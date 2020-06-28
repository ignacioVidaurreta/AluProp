package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.CareerService;
import ar.edu.itba.paw.interfaces.service.ProposalService;
import ar.edu.itba.paw.interfaces.service.UniversityService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.Proposal;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.UserProposal;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.dto.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
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
    @Autowired
    private ProposalService proposalService;

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
    @Path("/proposals")
    /*
     * If the user is a GUEST then return the proposals they are a part of
     * If the user is a HOST get the proposals of their properties.
     */
    public Response getProposals(){
        User user = userService.getCurrentlyLoggedUser();

        if(user == null){
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if(user.getRole() == Role.ROLE_GUEST){
            return getAllUserProposals(user);
        }else if(user.getRole() == Role.ROLE_HOST){
            return getAllProposals(user);
        }else{
            return Response.status(Response.Status.FORBIDDEN).build();
        }
    }

    private Response getAllUserProposals(User user){
        Collection<UserProposal> proposals =
                userService.getWithRelatedEntities(user.getId()).getUserProposals();
        return Response.ok(proposals.stream()
                            .map(UserProposalDto::fromUserProposal)
                            .collect(Collectors.toList()))
                .build();
    }

    private Response getAllProposals(User user){
        Collection<Proposal> proposals = proposalService.getProposalsForOwnedProperties(user);

        return Response.ok(proposals.stream()
                            .map(ProposalDto::fromProposal)
                            .collect(Collectors.toList()))
                .build();
    }
}
