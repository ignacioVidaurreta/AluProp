package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.CareerDao;
import ar.edu.itba.paw.model.Career;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import java.util.Collection;
import java.util.List;

@Repository
public class APCareerDao implements CareerDao {

    private RowMapper<Career> ROW_MAPPER = (rs, rownum)
        -> new Career(rs.getLong("id"), rs.getString("name"));
    private final JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public APCareerDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Career get(long id) {
        final TypedQuery<Career> query = entityManager.createQuery("from Career as c where c.id = :id", Career.class);
        query.setParameter("id", id);
        final List<Career> list = query.getResultList();
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Collection<Career> getAll() {
        return entityManager.createQuery("from Career", Career.class).getResultList();
    }
}
