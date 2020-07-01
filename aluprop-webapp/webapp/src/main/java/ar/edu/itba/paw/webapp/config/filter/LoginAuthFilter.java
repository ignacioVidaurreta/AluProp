package ar.edu.itba.paw.webapp.config.filter;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.exception.AlreadyLoggedException;
import ar.edu.itba.paw.webapp.exception.InvalidLoginDataException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            return login(request);
        }
        catch (IOException e) {
            throw new InvalidLoginDataException("Invalid login json");
        }
    }

    private Authentication login(HttpServletRequest request) throws IOException {
        User user = userService.getCurrentlyLoggedUser();
        if(user != null)
            throw new AlreadyLoggedException("User " + user.getEmail() + " is already logged in");
        LoginData data = parseLoginRequest(request);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(data.username,
                                                                                            data.password);
        setDetails(request, token);
        Authentication auth = getAuthenticationManager().authenticate(token);
        return auth;
    }

    private LoginData parseLoginRequest(HttpServletRequest request) throws IOException {
        String json = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, LoginData.class);
    }

    private static class LoginData {

        public String username;
        public String password;
    }
}
