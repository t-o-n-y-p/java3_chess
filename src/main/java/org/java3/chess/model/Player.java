package org.java3.chess.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "players")
public class Player {

    @Transient
    private final double DEFAULT_RATING = 1200.0;

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, scale = 1)
    private double rating = DEFAULT_RATING;

    @OneToMany
    @JoinColumn(name = "to_id")
    private List<Challenge> incomingChallenges;

    public Player() {
    }

    public Player(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
