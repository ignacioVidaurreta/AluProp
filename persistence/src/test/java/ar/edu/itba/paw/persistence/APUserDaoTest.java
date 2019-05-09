package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

@Sql("classpath:schema.sql")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes= TestConfig.class)
public class APUserDaoTest {

    private final static String NAME = "John";
    private final static String MAIL = "johnTester@gmail.com";
    @Autowired
    private DataSource ds;


    @Autowired
    private APUserDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void getUserTest(){
        User maybeUser = userDao.get(1);

        Assert.assertNotNull(maybeUser);
        Assert.assertEquals(1, maybeUser.getId());
        Assert.assertEquals(NAME, maybeUser.getName());
        Assert.assertEquals(MAIL, maybeUser.getEmail());

    }

    @Test
    public void getAllUsersTest(){
        int expectedRowCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "users");
        int realRowCount = userDao.getAll().size();

        Assert.assertEquals(expectedRowCount, realRowCount);
    }




}
