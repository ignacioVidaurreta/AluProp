package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalAccessException extends AuthenticationException {

    public IllegalAccessException(String msg) {
        super(msg);
    }
}
