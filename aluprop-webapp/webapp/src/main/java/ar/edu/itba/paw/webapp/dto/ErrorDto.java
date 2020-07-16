package ar.edu.itba.paw.webapp.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.Collection;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ErrorDto {
    private ErrorDto() { }

    private ArrayList<String> errors;

    public static ErrorDto fromErrors(Collection<String> errors){
        ErrorDto ret = new ErrorDto();
        ret.errors = new ArrayList<>(errors);

        return ret;
    }

    public ArrayList<String> getErrors() {
        return errors;
    }
}
