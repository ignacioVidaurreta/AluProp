package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class APUsernamePasswordAuthToken extends UsernamePasswordAuthenticationToken {

    private final String token;

    public APUsernamePasswordAuthToken(String token) {
        super(null, null);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
