package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Career;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CareerDto {

    private CareerDto() { }

    private long id;
    private String name;

    public static CareerDto fromCareer(Career career) {
        CareerDto ret = new CareerDto();
        if(career == null)
            return ret;
        ret.id = career.getId();
        ret.name = career.getName();
        return ret;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
