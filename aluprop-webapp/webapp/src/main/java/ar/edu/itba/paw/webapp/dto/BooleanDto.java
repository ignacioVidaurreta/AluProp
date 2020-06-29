package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class BooleanDto {
    private BooleanDto() { }

    private Boolean value;

    public static BooleanDto fromBoolean(Boolean bool){
        BooleanDto ret = new BooleanDto();
        ret.value = bool;

        return ret;
    }

    public Boolean getValue(){
        return value;
    }

}
