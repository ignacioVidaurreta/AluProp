package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.JwtDao;
import ar.edu.itba.paw.model.Jwt;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;

@Repository
@Transactional
public class APJwtDao implements JwtDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isInBlacklist(String token) {
        return entityManager.find(Jwt.class, token) != null;
    }

    @Override
    public void addToBlacklist(String token, Timestamp expiry) {
        entityManager.persist(new Jwt(token, expiry));
    }

    @Override
    public void removeExpired() {
        entityManager.createQuery("delete from Jwt j where j.expiry < :now")
                    .setParameter("now", new Timestamp(System.currentTimeMillis()))
                    .executeUpdate();
    }
}
