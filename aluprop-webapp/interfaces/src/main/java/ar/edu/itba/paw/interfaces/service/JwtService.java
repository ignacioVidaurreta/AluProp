package ar.edu.itba.paw.interfaces.service;

import java.sql.Timestamp;

public interface JwtService {
    boolean isInBlacklist(String token);
    void addToBlacklist(String token, Timestamp expiry);
    void removeExpired();
}
