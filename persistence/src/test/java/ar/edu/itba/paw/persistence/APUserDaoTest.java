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
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

@Sql("classpath:schema.sql")
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes= TestConfig.class)
public class APUserDaoTest {

    @Mock
    User userMock;

    @Autowired
    private DataSource ds;


    @Autowired
    private APUserDao userDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }


    @Test
    public void testCreation(){
        Mockito.when(userMock.getUsername()).thenReturn("Mocked_Guy77");
        Mockito.when(userMock.getId()).thenReturn((long) 2);

        //User maybeUser = userDao.create(userMock);
        /*
        User maybe_user = userDao.getByUsername("Mocked_Guy77");
        Assert.assertNotNull(maybe_user);
        */
        //Assert.assertNotNull(userDao);



    }
}
