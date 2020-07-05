package ar.edu.itba.paw.interfaces.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public interface JwtDao {

    public boolean isInBlacklist(String token);
    public void addToBlacklist(String token, Timestamp expiry);
    public void removeExpired();
}
