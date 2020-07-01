package ar.edu.itba.paw.webapp.helperClasses;

import ar.edu.itba.paw.model.User;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;

@Component
public class JwtTokenHandler {

    private final SecureRandom random = new SecureRandom();
    private String secret = "iamverysafe";

    public String createToken(User user) {
        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(user.getEmail()))
                .setHeaderParam("salt", random.nextLong())
                .signWith(SignatureAlgorithm.HS512, secret) // TODO: adjust for prod
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 2628000000L)) // one month
                .compact();
    }

    public Claims validateTokenString(String tokenString) {
        Claims claims;
        try {
            Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
            claims = (Claims) token.getBody();
            Header header = token.getHeader();
            Date timeNow = new Date();

            if(claims.getIssuedAt() == null || claims.getIssuedAt().after(timeNow)) {
                throw new JwtException("Invalid issued at date");
            }
            if(claims.getExpiration() == null || claims.getExpiration().before(timeNow)) {
                throw new JwtException("Invalid expiration date");
            }
            if(header.get("salt") == null) {
                throw new JwtException("Missing salt in Jason Web Token");
            }

        } catch (ClassCastException | JwtException e) {
            claims = null;
        }
        return claims;
    }
}
