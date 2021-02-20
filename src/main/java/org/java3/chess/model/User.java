package org.java3.chess.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @Column(unique = true, nullable = false, length = 50)
    private String login;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, scale = 1)
    private double rating;

    @OneToMany
    @JoinColumn(name = "to_id")
    private List<Challenge> incomingChallenges;

    public User() {
    }

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        rating = 1200.0;
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

    public List<Challenge> getIncomingChallenges() {
        return incomingChallenges;
    }
}
