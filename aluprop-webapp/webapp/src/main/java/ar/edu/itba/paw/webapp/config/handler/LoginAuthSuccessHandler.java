package ar.edu.itba.paw.webapp.config.handler;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.dto.IndexUserDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.helperClasses.JwtTokenHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;
    @Autowired

    private JwtTokenHandler tokenHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
            Authentication authentication) throws IOException {
        User user = userService.getByEmail(authentication.getName());
        httpServletResponse.addHeader("X-TOKEN", tokenHandler.createToken(user));
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), IndexUserDto.fromUser(user));
    }
}
