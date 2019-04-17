package ar.edu.itba.paw.persistence;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import ar.edu.itba.paw.interfaces.PropertyDao;
import ar.edu.itba.paw.model.Property;

@Repository
public class APPropertyJdbcDao implements PropertyDao {

    private static final String GET_ALL_MATCHING_STRING = "SELECT * FROM properties WHERE description LIKE ? OR area LIKE ? OR caption LIKE ?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM properties";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM properties WHERE id = ?";
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<Property> ROW_MAPPER = (rs, rowNum) -> new Property(rs.getInt("id"),
            rs.getString("caption"), rs.getString("description"), rs.getString("image"), rs.getString("area"));
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public APPropertyJdbcDao(DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("interests").usingGeneratedKeyColumns("id");
    }

    @Override
    public Property get(int id) {
        final List<Property> list = jdbcTemplate.query(FIND_BY_ID_QUERY, ROW_MAPPER, id);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Collection<Property> getAll() {
        final List<Property> list = jdbcTemplate.query(SELECT_ALL_QUERY, ROW_MAPPER);
        return list;
    }

    @Override
    public boolean showInterest(int propertyId, String email, String description) {
        final int rowsAffected = jdbcInsert
                .execute(generateArguementsForInterestCreation(propertyId, email, description));
        if (rowsAffected == 1)
            return true;
        return false;
    }

    private Map<String, Object> generateArguementsForInterestCreation(int propertyId, String email,
            String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("propertyId", propertyId);
        args.put("email", email);
        args.put("description", description);
        return args;
    }

    @Override
    public Collection<Property> getAllContainingString(String searchString) {
        String sqlContainString = "%" + searchString + "%";
        final List<Property> list = jdbcTemplate.query(
                GET_ALL_MATCHING_STRING, ROW_MAPPER, sqlContainString, sqlContainString, sqlContainString);
        return list;
    }

}
