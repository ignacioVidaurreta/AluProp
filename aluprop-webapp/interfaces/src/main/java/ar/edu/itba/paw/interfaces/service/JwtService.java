package ar.edu.itba.paw.interfaces.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface JwtService {

    public boolean isInBlacklist(String token);
    public void addToBlacklist(String token, Timestamp expiry);
    public void removeExpired();
}
