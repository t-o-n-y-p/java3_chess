package org.java3.chess.dao;

import org.java3.chess.model.Color;
import org.java3.chess.model.Game;
import org.java3.chess.model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

public class GamesDaoTest {

    private static EntityManagerFactory factory;
    private static EntityManager manager;
    private static GamesDao gamesDao;
    private static final List<User> allCreatedUsers = new ArrayList<>();
    private static final List<Game> allCreatedGames = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
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
        Color currentColor = Color.WHITE;
        for (User white : allCreatedUsers.subList(0, 10)) {
            for (User black : allCreatedUsers.subList(0, 10)) {
                if (!white.equals(black)) {
                    Game game = new Game(white, black);
                    if (currentColor == Color.WHITE) {
                        currentColor = Color.BLACK;
                    } else {
                        game.setPlayerToMove(black);
                        currentColor = Color.WHITE;
                    }
                    allCreatedGames.add(game);
                    manager.persist(game);
                }
                sleep(1000);
            }
        }
        for (User player1 : allCreatedUsers.subList(0, 16)) {
            for (User player2 : allCreatedUsers.subList(0, 10)) {
                if (!player1.equals(player2)) {
                    Game game = new Game(player1, player2);
                    allCreatedGames.add(game);
                    manager.persist(game);
                    sleep(1000);
                    game = new Game(player2, player1);
                    game.setPlayerToMove(player1);
                    allCreatedGames.add(game);
                    manager.persist(game);
                }
                sleep(1000);
            }
        }
        for (User player1 : allCreatedUsers.subList(0, 20)) {
            for (User player2 : allCreatedUsers.subList(0, 10)) {
                if (!player1.equals(player2)) {
                    Game game = new Game(player1, player2);
                    game.setCompleted(true);
                    if (currentColor == Color.WHITE) {
                        currentColor = Color.BLACK;
                    } else {
                        game.setPlayerToMove(player2);
                        currentColor = Color.WHITE;
                    }
                    allCreatedGames.add(game);
                    manager.persist(game);
                }
                sleep(1000);
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
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void findByUser() {
        User noGames = allCreatedUsers.stream()
                .filter(u -> u.getLogin().matches("[^\\d]+[1][0-1]"))
                .findAny()
                .get();
        assertTrue(gamesDao.findByUser(noGames, 0, 1000).isEmpty());

        User player = allCreatedUsers.stream()
                .filter(u -> u.getLogin().matches("[^\\d]+[0-4]"))
                .findAny()
                .get();
        List<Game> actualResult = gamesDao.findByUser(player, 0, 1000);
        List<Game> expectedResult = allCreatedGames.stream()
                .filter(g -> g.getWhite().equals(player) || g.getBlack().equals(player))
                .sorted(Comparator.comparing(Game::isCompleted)
                        .thenComparing(g -> !g.getPlayerToMove().equals(player))
                        .thenComparing(Comparator.comparing(Game::getLastModifiedTimestamp).reversed()))
                .collect(Collectors.toList());
        assertEquals(expectedResult, actualResult);

    }

    @Test
    public void findByUserAndOpponentLoginInput() {
    }
}