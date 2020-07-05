package ar.edu.itba.paw.webapp.config.filter;

import ar.edu.itba.paw.interfaces.service.JwtService;
import ar.edu.itba.paw.webapp.auth.APUsernamePasswordAuthToken;
import ar.edu.itba.paw.webapp.helperClasses.JwtTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;

public class LogoutAuthFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtTokenHandler tokenHandler;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        APUsernamePasswordAuthToken token = tokenHandler.parseToken(request);
        if (token != null) {
            Timestamp expiry = new Timestamp(tokenHandler.expiry(token.getToken()).getTime());
            jwtService.addToBlacklist(token.getToken(), expiry);
        }
        return new AnonymousAuthenticationToken("AP_ANONYMOUS",
                "ANONYMOUS",
                Collections.singletonList(new SimpleGrantedAuthority("NONE")));
    }
}
