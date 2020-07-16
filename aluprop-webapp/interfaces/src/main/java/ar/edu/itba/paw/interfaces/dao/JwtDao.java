package ar.edu.itba.paw.interfaces.dao;

import java.sql.Timestamp;

public interface JwtDao {
    boolean isInBlacklist(String token);
    void addToBlacklist(String token, Timestamp expiry);
    void removeExpired();
}
