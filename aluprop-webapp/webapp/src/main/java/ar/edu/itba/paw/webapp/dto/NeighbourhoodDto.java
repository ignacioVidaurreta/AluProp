package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.Neighbourhood;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class NeighbourhoodDto {

    private long id;
    private String name;

    public Neighbourhood toNeighbourhood() {
        return new Neighbourhood(id, name);
    }

    public static NeighbourhoodDto fromNeighbourhood(Neighbourhood neighbourhood) {
        final NeighbourhoodDto ret = new NeighbourhoodDto();
        ret.id = neighbourhood.getId();
        ret.name = neighbourhood.getName();
        return ret;
    }
}
