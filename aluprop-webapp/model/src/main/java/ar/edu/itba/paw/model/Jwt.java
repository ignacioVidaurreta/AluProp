package ar.edu.itba.paw.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "jwt")
public class Jwt {

    @Id
    @Column(name = "token", length = 1000, nullable = false)
    private String token;

    @Column(name = "expiry", nullable = false)
    private Timestamp expiry;

    /* package */ Jwt() { }

    public Jwt(String token, Timestamp expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }
}
