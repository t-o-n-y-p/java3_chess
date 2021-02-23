package org.java3.chess.dao;

import org.java3.chess.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.Assert.*;

public class UsersDaoTest {

    private EntityManagerFactory factory;
    private EntityManager manager;
    private UsersDao usersDao;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory("TestDatabase");
        manager = factory.createEntityManager();
        usersDao = new UsersDao(manager);

        manager.getTransaction().begin();
        for (int i = 0; i < 120; i++) {
            User user = new User("login" + i, "password" + i);
            user.setRating(2800 - i * 15);
            manager.persist(user);
        }
        manager.getTransaction().commit();
    }

    @After
    public void tearDown() {
        if (manager != null) {
            manager.close();
        }
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    public void findByLogin() {
        assertNull(usersDao.findByLogin("non existing user"));

        User found = usersDao.findByLogin("login100");
        assertNotNull(found);
        assertEquals("login100", found.getLogin());
    }

    @Test
    public void findByLoginAndPassword() {
        assertNull(usersDao.findByLoginAndPassword("some user", "password23"));
        assertNull(usersDao.findByLoginAndPassword("login56", "pass56"));

        User found = usersDao.findByLoginAndPassword("login82", "password82");
        assertNotNull(found);
        assertEquals("login82", found.getLogin());
        assertEquals("password82", found.getPassword());
    }

    @Test
    public void findByRating() {
        assertTrue(usersDao.findByRating(3000, 50, 0, 100).isEmpty());
        assertTrue(usersDao.findByRating(800, 50, 0, 100).isEmpty());

        assertFalse(usersDao.findByRating(3000, 400, 0, 100).isEmpty());
        for (User c : usersDao.findByRating(3000, 400, 0, 100)) {
            assertTrue(c.getRating() >= 2600 && c.getRating() <= 3400);
        }
        assertFalse(usersDao.findByRating(800, 400, 0, 100).isEmpty());
        for (User c : usersDao.findByRating(800, 400, 0, 100)) {
            assertTrue(c.getRating() >= 400 && c.getRating() <= 1200);
        }
        assertFalse(usersDao.findByRating(1900, 50, 0, 100).isEmpty());
        for (User c : usersDao.findByRating(1900, 50, 0, 100)) {
            assertTrue(c.getRating() >= 1850 && c.getRating() <= 1950);
        }

        List<User> result1 = usersDao.findByRating(1900, 400, 0, 10);
        assertEquals(10, result1.size());
        List<User> result2 = usersDao.findByRating(1900, 400, 40, 10);
        assertEquals(10, result2.size());
        result1.retainAll(result2);
        assertTrue(result1.isEmpty());

        assertTrue(usersDao.findByRating(1900, 400, 1000, 10).isEmpty());

        result1 = usersDao.findByRating(1900, 400, 0, 20);
        assertEquals(20, result1.size());
        result2 = usersDao.findByRating(1900, 400, 40, 20);
        assertEquals(13, result2.size());
        result1.retainAll(result2);
        assertTrue(result1.isEmpty());
    }

    @Test
    public void findByRatingAndLoginInput() {
    }
}