package ar.edu.itba.paw.service;

import ar.edu.itba.paw.interfaces.dao.JwtDao;
import ar.edu.itba.paw.interfaces.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class APJwtService implements JwtService {

    @Autowired
    private JwtDao jwtDao;

    @Override
    public boolean isInBlacklist(String token) {
        return jwtDao.isInBlacklist(token);
    }

    @Override
    public void addToBlacklist(String token, Timestamp expiry) {
        jwtDao.addToBlacklist(token, expiry);
    }

    @Override
    public void removeExpired() {
        jwtDao.removeExpired();
    }


}
