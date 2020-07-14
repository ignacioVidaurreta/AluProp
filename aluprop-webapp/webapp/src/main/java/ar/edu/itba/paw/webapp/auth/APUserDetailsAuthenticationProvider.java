package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.JwtService;
import ar.edu.itba.paw.webapp.helper_classes.JwtTokenHandler;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class APUserDetailsAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private JwtTokenHandler tokenHandler;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    @Override
    public boolean supports(Class<?> authentication) {
        return APUsernamePasswordAuthToken.class.isAssignableFrom(authentication);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken token)
                                                throws AuthenticationException {
        if (!(token instanceof APUsernamePasswordAuthToken))
            throw new IllegalArgumentException("This is not an APUsernamePasswordAuthToken: rejecting auth");
        if (jwtService.isInBlacklist(((APUsernamePasswordAuthToken) token).getToken()))
            throw new IllegalArgumentException("Blacklisted token");
    }

    @Override
    protected UserDetails retrieveUser(String s, UsernamePasswordAuthenticationToken token)
                                    throws AuthenticationException {
        APUsernamePasswordAuthToken apToken = (APUsernamePasswordAuthToken) token;
        String jwt = apToken.getToken();
        Claims claims = tokenHandler.validateTokenString(jwt);
        String username = claims == null ? null : claims.getSubject();
        if (username == null) {
            throw new IllegalArgumentException("Token username is not present");
        }
        return userDetailsService.loadUserByUsername(username);
    }
}
