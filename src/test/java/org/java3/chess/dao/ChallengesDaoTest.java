package org.java3.chess.dao;

import org.java3.chess.model.Challenge;
import org.java3.chess.model.Color;
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

public class ChallengesDaoTest {

    private static EntityManagerFactory factory;
    private static EntityManager manager;
    private static ChallengesDao challengesDao;
    private static final List<User> allCreatedUsers = new ArrayList<>();
    private static final List<Challenge> allCreatedChallenges = new ArrayList<>();

    @BeforeClass
    public static void beforeClass() throws InterruptedException {
        factory = Persistence.createEntityManagerFactory("TestDatabase");
        manager = factory.createEntityManager();
        challengesDao = new ChallengesDao(manager);

        manager.getTransaction().begin();
        for (int i = 0; i < 12; i++) {
            for (String prefix : List.of("login", "test")) {
                User user = new User(prefix + i, "password" + i);
                allCreatedUsers.add(user);
                manager.persist(user);
            }
        }
        Color currentColor = Color.WHITE;
        for (User from : allCreatedUsers) {
            for (User to : allCreatedUsers.subList(0, 20)) {
                if (!from.equals(to)) {
                    Challenge challenge = new Challenge(from, to, currentColor);
                    allCreatedChallenges.add(challenge);
                    manager.persist(challenge);
                    if (currentColor == Color.WHITE) {
                        currentColor = Color.BLACK;
                    } else {
                        currentColor = Color.WHITE;
                    }
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
    public void findIncomingChallenges() {
        User noChallenges = allCreatedUsers.stream()
                .filter(u -> u.getLogin().matches("[^\\d]+[1][0-1]"))
                .findAny()
                .get();
        assertTrue(challengesDao.findIncomingChallenges(noChallenges, 0, 1000).isEmpty());

        User to = allCreatedUsers.stream()
                .filter(u -> !u.getLogin().matches("[^\\d]+[1][0-1]"))
                .findAny()
                .get();
        List<Challenge> actualResult = challengesDao.findIncomingChallenges(to, 0, 1000);
        List<Challenge> expectedResult = allCreatedChallenges.stream()
                .filter(c -> c.getTo().equals(to))
                .sorted(Comparator.comparing(Challenge::getTimestamp).reversed())
                .collect(Collectors.toList());
        assertEquals(expectedResult, actualResult);

        List<Challenge> result1 = challengesDao.findIncomingChallenges(to, 0, 5);
        assertEquals(5, result1.size());
        List<Challenge> result2 = challengesDao.findIncomingChallenges(to, 10, 5);
        assertEquals(5, result2.size());
        result1.retainAll(result2);
        assertTrue(result1.isEmpty());

        assertTrue(challengesDao.findIncomingChallenges(to, 1000, 10).isEmpty());

        result1 = challengesDao.findIncomingChallenges(to, 0, 10);
        assertEquals(10, result1.size());
        result2 = challengesDao.findIncomingChallenges(to, 20, 10);
        assertEquals(3, result2.size());
        result1.retainAll(result2);
        assertTrue(result1.isEmpty());
    }

    @Test
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public void findIncomingChallengesByOpponentLoginInput() {
        User noChallenges = allCreatedUsers.stream()
                .filter(u -> u.getLogin().matches("[^\\d]+[1][0-1]"))
                .findAny()
                .get();
        assertTrue(
                challengesDao.findIncomingChallengesByOpponentLoginInput(noChallenges, "login",  0, 1000)
                        .isEmpty()
        );
        User toNegative = allCreatedUsers.stream()
                .filter(u -> !u.getLogin().matches("[^\\d]+[1][0-1]"))
                .findAny()
                .get();
        assertTrue(
                challengesDao.findIncomingChallengesByOpponentLoginInput(toNegative, "login_test",  0, 1000)
                        .isEmpty()
        );

        for (String input: List.of("login", "test")) {
            User to = allCreatedUsers.stream()
                    .filter(u -> !u.getLogin().matches("[^\\d]+[1][0-1]") && !u.getLogin().contains(input))
                    .findAny()
                    .get();
            List<Challenge> actualResult = challengesDao.findIncomingChallengesByOpponentLoginInput(
                    to, input, 0, 1000
            );
            List<Challenge> expectedResult = allCreatedChallenges.stream()
                    .filter(c -> c.getTo().equals(to) && c.getFrom().getLogin().contains(input))
                    .sorted(Comparator.comparing(Challenge::getTimestamp).reversed())
                    .collect(Collectors.toList());
            assertEquals(expectedResult, actualResult);

            List<Challenge> result1 = challengesDao.findIncomingChallengesByOpponentLoginInput(to, input, 0, 3);
            assertEquals(3, result1.size());
            List<Challenge> result2 = challengesDao.findIncomingChallengesByOpponentLoginInput(to, input, 6, 3);
            assertEquals(3, result2.size());
            result1.retainAll(result2);
            assertTrue(result1.isEmpty());

            assertTrue(challengesDao.findIncomingChallengesByOpponentLoginInput(to, input, 1000, 10).isEmpty());

            result1 = challengesDao.findIncomingChallengesByOpponentLoginInput(to, input, 0, 5);
            assertEquals(5, result1.size());
            result2 = challengesDao.findIncomingChallengesByOpponentLoginInput(to, input, 10, 5);
            assertEquals(2, result2.size());
            result1.retainAll(result2);
            assertTrue(result1.isEmpty());
        }

    }
}