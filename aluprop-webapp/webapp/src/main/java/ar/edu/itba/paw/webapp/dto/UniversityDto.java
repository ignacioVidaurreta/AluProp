package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.University;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UniversityDto {

    private UniversityDto() { }

    private long id;
    private String name;

    public static UniversityDto fromUniversity(University university) {
        UniversityDto ret = new UniversityDto();
        ret.id = university.getId();
        ret.name = university.getName();
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
