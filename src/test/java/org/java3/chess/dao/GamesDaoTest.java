package org.java3.chess.dao;

import org.java3.chess.model.Game;
import org.java3.chess.model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;

public class GamesDaoTest {

    private static EntityManagerFactory factory;
    private static EntityManager manager;
    private static GamesDao gamesDao;
    private static final List<User> allCreatedUsers = new ArrayList<>();
    private static final List<Game> allCreatedGames = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() {
        factory = Persistence.createEntityManagerFactory("TestDatabase");
        manager = factory.createEntityManager();
        gamesDao = new GamesDao(manager);

        manager.getTransaction().begin();
        for (int i = 0; i < 12; i++) {
            for (String prefix : List.of("login", "test")) {
                User user = new User(prefix + i, "password" + i);
                allCreatedUsers.add(user);
                manager.persist(user);
            }
        }
        for (User white : allCreatedUsers.subList(0, 20)) {
            for (User black : allCreatedUsers.subList(0, 20)) {
                if (!white.equals(black)) {
                    Game game = new Game(white, black);
                    allCreatedGames.add(game);
                    manager.persist(game);
                }
            }
        }
        manager.getTransaction().commit();
    }

    @AfterClass
    public static void afterClass() {
        if (manager != null) {
            manager.close();
        }
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    public void findByUser() {
    }

    @Test
    public void findByUserAndOpponentLoginInput() {
    }
}