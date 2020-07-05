package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

public class JwtBlacklistCleaner {

    private final long oneMonth = 2592000000L;

    @Autowired
    private JwtService jwtService;

    @Scheduled(fixedDelay = oneMonth)
    public void clean() {
        jwtService.removeExpired();
    }
}
