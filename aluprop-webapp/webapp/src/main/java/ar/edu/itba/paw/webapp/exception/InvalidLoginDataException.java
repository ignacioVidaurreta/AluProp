package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginDataException extends AuthenticationException {

    public InvalidLoginDataException(String s) {
        super(s);
    }
}
