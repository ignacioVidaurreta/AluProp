package ar.edu.itba.paw.webapp.helperClasses;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.auth.APUsernamePasswordAuthToken;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenHandler {

    private final SecureRandom random = new SecureRandom();
    private final String secret = "aWFtdmVyeXNhZmUK"; // TODO: adjust for prod
    private final long oneMonth = 2592000000L;

    public String createToken(User user) {
        final Date currentDate = new Date();
        return Jwts.builder()
                .setClaims(Jwts.claims().setSubject(user.getEmail()))
                .setHeaderParam("salt", random.nextLong())
                .signWith(SignatureAlgorithm.HS512, secret)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + oneMonth)) // one month
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

    public Date expiry(String tokenString) {
        Jwt token = Jwts.parser().setSigningKey(secret).parse(tokenString);
        Claims claims = (Claims) token.getBody();
        return claims.getExpiration();
    }

    public APUsernamePasswordAuthToken parseToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return new APUsernamePasswordAuthToken(header.substring("Bearer ".length()));
    }
}
