package ar.edu.itba.paw.webapp.config.handler;

import ar.edu.itba.paw.webapp.exception.AlreadyLoggedException;
import ar.edu.itba.paw.webapp.exception.InvalidLoginDataException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) {
        if(e instanceof AlreadyLoggedException)
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        else if (e instanceof InvalidLoginDataException)
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        else if (e instanceof BadCredentialsException)
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
