package org.java3.chess.dao;

import org.java3.chess.model.Challenge;
import org.java3.chess.model.User;

import javax.persistence.EntityManager;
import java.util.List;

public class ChallengesDao {

    private EntityManager manager;

    public ChallengesDao(EntityManager manager) {
        this.manager = manager;
    }

    public List<Challenge> findIncomingChallenges(User user, int offset, int limit) {
        return manager.createQuery(
                "from Challenge where to = :user order by timestamp desc", Challenge.class)
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Challenge> findIncomingChallengesByOpponentLoginInput(User user, String input, int offset, int limit) {
        return manager.createQuery(
                "from Challenge c where to = :user and c.from.login like concat('%', :input, '%') " +
                        "order by timestamp desc", Challenge.class)
                .setParameter("input", input)
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
