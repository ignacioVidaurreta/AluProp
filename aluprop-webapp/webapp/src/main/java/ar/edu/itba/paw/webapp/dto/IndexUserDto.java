package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class IndexUserDto {

    private IndexUserDto() { }

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
    private Role role;

    public static IndexUserDto fromUser(User user) {
        IndexUserDto ret = new IndexUserDto();
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

    public Role getRole() {
        return role;
    }
}
