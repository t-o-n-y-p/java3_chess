package org.java3.chess.dao;

import org.java3.chess.model.Game;
import org.java3.chess.model.User;

import javax.persistence.EntityManager;
import java.util.List;

public class GamesDao {

    private EntityManager manager;

    public GamesDao(EntityManager manager) {
        this.manager = manager;
    }

    public List<Game> findByUser(User user, int offset, int limit) {
        return manager.createQuery(
                "from Game where white = :user or black = :user " +
                        "order by case " +
                            "when isCompleted = true then 2" +
                            "when playerToMove = :user then 0 " +
                            "else 1 " +
                        "end, lastModifiedTimestamp desc", Game.class)
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Game> findByUserAndOpponentLoginInput(User user, String input, int offset, int limit) {
        return manager.createQuery(
                "from Game g where (white = :user and g.black.login like concat('%', :input, '%')) " +
                        "or (black = :user and g.white.login like concat('%', :input, '%')) " +
                        "order by case " +
                            "when isCompleted = true then 2" +
                            "when playerToMove = :user then 0 " +
                            "else 1 " +
                        "end, lastModifiedTimestamp desc", Game.class)
                .setParameter("input", input)
                .setParameter("user", user)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
