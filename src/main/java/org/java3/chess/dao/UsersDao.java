package org.java3.chess.dao;

import org.java3.chess.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

public class UsersDao {

    private EntityManager manager;

    public UsersDao(EntityManager manager) {
        this.manager = manager;
    }

    public User findByLogin(String login) {
        try {
            return manager.createQuery("from User where login = :login", User.class)
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (NoResultException notFound) {
            return null;
        }
    }

    public User findByLoginAndPassword(String login, String password) {
        try {
            return manager.createQuery("from User where login = :login and password = :password", User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException notFound) {
            return null;
        }
    }

    public List<User> findByRating(double rating, double threshold, int offset, int limit) {
        return manager.createQuery("from User where rating >= :start and rating <= :end", User.class)
                .setParameter("start", rating - threshold)
                .setParameter("end", rating + threshold)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<User> findByRatingAndLoginInput(String input, double rating, double threshold, int offset, int limit) {
        return manager.createQuery("from User where login like concat('%', :input, '%') " +
                "and rating >= :start and rating <= :end", User.class)
                .setParameter("input", input)
                .setParameter("start", rating - threshold)
                .setParameter("end", rating + threshold)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

}
