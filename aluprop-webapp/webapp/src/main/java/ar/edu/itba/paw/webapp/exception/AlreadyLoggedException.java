package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.core.AuthenticationException;

public class AlreadyLoggedException extends AuthenticationException {

    public AlreadyLoggedException(String message) {
        super(message);
    }
}
