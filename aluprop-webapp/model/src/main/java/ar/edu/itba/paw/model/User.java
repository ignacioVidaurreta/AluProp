package ar.edu.itba.paw.model;

import ar.edu.itba.paw.model.enums.Gender;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.model.exceptions.IllegalUserStateException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.regex.Pattern;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "users_id_seq")
    @SequenceGenerator(sequenceName = "users_id_seq", name = "users_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Date birthDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String passwordHash;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "universityId")
    private University university;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "careerId")
    private Career career;

    @Column(length = 1000)
    private String bio;

    @Column(length = 25)
    private String contactNumber;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "interests",
                joinColumns = @JoinColumn(name = "userId"),
                inverseJoinColumns = @JoinColumn(name = "propertyId"))
    private Collection<Property> interestedProperties;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    @OrderBy("id DESC")
    private Collection<UserProposal> userProposals;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId")
    private Collection<Property> ownedProperties;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private Collection<Notification> notifications;

    /* package */ User() { }

    /* package */ User(long id) {
        this.id = id;
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

    public String getPasswordHash() {
        return passwordHash;
    }

    public University getUniversity() {
        return university;
    }

    public Career getCareer() {
        return career;
    }

    public String getBio() {
        return bio;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Collection<Property> getInterestedProperties() {
        return interestedProperties;
    }

    public Role getRole() {
        return role;
    }

    public String getFullName(){
        StringBuilder builder = new StringBuilder(name);
        builder.append(' ');
        builder.append(lastName);
        return builder.toString();
    }

    public Collection<UserProposal> getUserProposals() {
        return userProposals;
    }

    public Collection<Property> getOwnedProperties() {
        return ownedProperties;
    }

    public Collection<Notification> getNotifications() {
        return notifications;
    }

    public int getAge() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(birthDate);
        LocalDate date = LocalDate.of(cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH));
        LocalDate now = LocalDate.now();
        Period diff = Period.between(date, now); //difference between the dates is calculated
        return diff.getYears();
    }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ");
        stringBuilder.append(getFullName());
        stringBuilder.append("\n");
        stringBuilder.append("Email: ");
        stringBuilder.append(getEmail());
        stringBuilder.append("\n");
        stringBuilder.append("Id: ");
        stringBuilder.append(getId());
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof User))
            return false;
        User u = (User) o;
        return u.getId() == getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    public static class Builder {

        private final String EMAIL_REGEX = "^\\S+@\\S+$";

        private User user;

        public Builder() {
            this.user = new User();
        }

        public User build(){
            checkStateLegallity();
            initializeLists();
            return user;
        }

        private void checkStateLegallity() {
            if(!emailIsValid()) throw new IllegalUserStateException("email must be provided.");
            if(!nameIsValid()) throw new IllegalUserStateException("name must be provided.");
            if(!lastNameIsValid()) throw new IllegalUserStateException("last name must be provided.");
            if(!birthDateIsValid()) throw new IllegalUserStateException("birth date must be provided.");
            if(!genderIsValid()) throw new IllegalUserStateException("gender must be provided");
            if(!passwordHashIsValid()) throw new IllegalUserStateException("password must be provided");
            if(!bioIsValid()) throw new IllegalUserStateException("bio must be provided");
            if(!contactNumberIsValid()) throw new IllegalUserStateException("contact number must be provided");
            if(!roleIsValid()) throw new IllegalUserStateException("role must be provided");
        }

        private boolean emailIsValid() {
            return user.email != null && Pattern.compile(EMAIL_REGEX).matcher(user.email).matches();
        }

        private boolean nameIsValid() {
            return user.name != null && !user.name.equals("");
        }

        private boolean lastNameIsValid() {
            return user.lastName != null && !user.name.equals("");
        }

        private boolean birthDateIsValid() {
            return user.birthDate != null;
        }

        private boolean genderIsValid() {
            return user.gender != null;
        }

        private boolean passwordHashIsValid() {
            return user.passwordHash != null && !user.passwordHash.equals("");
        }

        private boolean universityIsValid() {
            return user.university != null;
        }

        private boolean careerIsValid() {
            return user.career != null;
        }

        private boolean bioIsValid() {
            return user.bio != null && !user.bio.equals("");
        }

        private boolean contactNumberIsValid() {
            return user.contactNumber != null && !user.contactNumber.equals("");
        }

        private boolean roleIsValid() {
            return user.role != null;
        }

        private void initializeLists() {
            if(user.interestedProperties == null) user.interestedProperties = new LinkedList<>();
        }

        public Builder fromUser(User u) {
            user.id = u.id;
            user.email = u.email;
            user.name = u.name;
            user.lastName = u.lastName;
            user.birthDate = u.birthDate;
            user.gender = u.gender;
            user.passwordHash = u.passwordHash;
            user.university = u.university;
            user.career = u.career;
            user.bio = u.bio;
            user.contactNumber = u.contactNumber;
            user.interestedProperties = u.interestedProperties;
            user.role = u.role;
            return this;
        }

        public Builder withId(long id) {
            user.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            user.email = email;
            return this;
        }

        public Builder withName(String name) {
            user.name = name;
            return this;
        }

        public Builder withLastName(String lastName) {
            user.lastName = lastName;
            return this;
        }

        public Builder withBirthDate(Date birthDate) {
            user.birthDate = birthDate;
            return this;
        }

        public Builder withGender(Gender gender) {
            user.gender = gender;
            return this;
        }

        public Builder withPasswordHash(String passwordHash) {
            user.passwordHash = passwordHash;
            return this;
        }

        public Builder withUniversity(University university) {
            user.university = university;
            return this;
        }

        public Builder withUniversityId(long universityId) {
            user.university = new University(universityId);
            if (universityId == -1)
                user.university = null;
            return this;
        }

        public Builder withCareerId(long careerId) {
            user.career = new Career(careerId);
            if (careerId == -1)
                user.career = null;
            return this;
        }

        public Builder withCareer(Career career) {
            user.career = career;
            return this;
        }

        public Builder withBio(String bio) {
            user.bio = bio;
            return this;
        }

        public Builder withContactNumber(String contactNumber) {
            user.contactNumber = contactNumber;
            return this;
        }

        public Builder withInterestedProperties(Collection<Property> interestedProperties) {
            user.interestedProperties = interestedProperties;
            return this;
        }

        public Builder withRole(Role role) {
            user.role = role;
            return this;
        }
    }
}
