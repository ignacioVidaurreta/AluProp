package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserDto {

    private UserDto() {}

    private long id;
    private String email;
    private String name;
    private String lastName;
    private Date birthDate;
    private Gender gender;
    private UniversityDto university;
    private CareerDto career;
    private String bio;
    private String contactNumber;
    private Collection<IndexPropertyDto> interestedProperties;
    private Role role;
    private Collection<UserProposalDto> userProposals;
    private Collection<IndexPropertyDto> ownedProperties;
    private Collection<NotificationDto> notifications;

    public static UserDto fromUser(User user) {
        UserDto ret = new UserDto();
        ret.id = user.getId();
        ret.email = user.getEmail();
        ret.name = user.getName();
        ret.lastName = user.getLastName();
        ret.birthDate = user.getBirthDate();
        ret.gender = user.getGender();
        ret.university = UniversityDto.fromUniversity(user.getUniversity());
        ret.career = CareerDto.fromCareer(user.getCareer());
        ret.bio = user.getBio();
        ret.contactNumber = user.getContactNumber();
        ret.role = user.getRole();
        ret.interestedProperties = user.getInterestedProperties()
                                        .stream()
                                        .map(IndexPropertyDto::fromProperty)
                                        .collect(Collectors.toList());
        ret.userProposals = user.getUserProposals()
                                .stream()
                                .map(UserProposalDto::fromUserProposal)
                                .collect(Collectors.toList());
        ret.ownedProperties = user.getOwnedProperties()
                                .stream()
                                .map(IndexPropertyDto::fromProperty)
                                .collect(Collectors.toList());
        ret.notifications = user.getNotifications()
                                .stream()
                                .map(NotificationDto::fromNotification)
                                .collect(Collectors.toList());
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public UniversityDto getUniversity() {
        return university;
    }

    public CareerDto getCareer() {
        return career;
    }

    public String getBio() {
        return bio;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Collection<IndexPropertyDto> getInterestedProperties() {
        return interestedProperties;
    }

    public Role getRole() {
        return role;
    }

    public Collection<UserProposalDto> getUserProposals() {
        return userProposals;
    }

    public Collection<IndexPropertyDto> getOwnedProperties() {
        return ownedProperties;
    }

    public Collection<NotificationDto> getNotifications() {
        return notifications;
    }
}
