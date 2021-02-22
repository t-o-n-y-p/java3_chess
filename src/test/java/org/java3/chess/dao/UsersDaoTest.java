package org.java3.chess.dao;

import org.java3.chess.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
    }

    @Test
    public void findByLoginAndPassword() {
    }

    @Test
    public void findByRating() {
    }

    @Test
    public void findByRatingAndLoginInput() {
    }
}