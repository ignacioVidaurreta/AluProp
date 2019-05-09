package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Property;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= TestConfig.class)
public class APPropertyDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private APPropertyDao propertyDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);


    }

    @Tgest
    public void getPropertyTest(){
        int propertyID = 1;
        Property maybeProperty;

        maybeProperty = propertyDao.get(propertyID);
        Assert.assertNotNull(maybeProperty);
        Assert.assertEquals(propertyID, maybeProperty.getId());
    }

    @Test
    public void getAllPropertiesTest(){
        int realRowCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "properties");
        int expectedRowCount = propertyDao.getAll().size();
        System.out.println(realRowCount);
        Assert.assertEquals(expectedRowCount, realRowCount);
    }

}
