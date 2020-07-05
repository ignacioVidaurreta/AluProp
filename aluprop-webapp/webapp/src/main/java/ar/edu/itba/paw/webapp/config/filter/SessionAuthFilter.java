package ar.edu.itba.paw.webapp.config.filter;

import ar.edu.itba.paw.interfaces.service.JwtService;
import ar.edu.itba.paw.model.enums.Role;
import ar.edu.itba.paw.webapp.auth.APUsernamePasswordAuthToken;
import ar.edu.itba.paw.webapp.exception.IllegalAccessException;
import ar.edu.itba.paw.webapp.helperClasses.JwtTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

public class SessionAuthFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtTokenHandler tokenHandler;
    @Autowired
    private RequestMatcher hostMatcher;
    @Autowired
    private RequestMatcher guestMatcher;
    @Autowired
    private RequestMatcher anonymousMatcher;

    public SessionAuthFilter() {
        super("/**");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        APUsernamePasswordAuthToken token = tokenHandler.parseToken(request);
        if (token != null) {
            Authentication auth = getAuthenticationManager().authenticate(token);
            if (hostMatcher.matches(request)) return checkHost(auth);
            if (guestMatcher.matches(request)) return checkGuest(auth);
            return auth;
        }
        if(!anonymousMatcher.matches(request))
            throw new IllegalAccessException("Only users are allowed to this url");
        return new AnonymousAuthenticationToken("AP_ANONYMOUS",
                "ANONYMOUS",
                Collections.singletonList(new SimpleGrantedAuthority("NONE")));
    }

    private Authentication checkHost(Authentication auth) {
        if (!isHost(auth))
            throw new IllegalAccessException("Only hosts are allowed to this url");
        return auth;
    }

    private Authentication checkGuest(Authentication auth) {
        if (!isGuest(auth))
            throw new IllegalAccessException("Only guests are allowed to this url");
        return auth;
    }

    private boolean isHost(Authentication auth) {
        return isRole(auth, Role.ROLE_HOST.toString());
    }

    private boolean isGuest(Authentication auth) {
        return isRole(auth, Role.ROLE_GUEST.toString());
    }

    private boolean isRole(Authentication auth, String role) {
        return auth.getAuthorities().contains(new SimpleGrantedAuthority(role));
    }
}
