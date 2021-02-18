package org.java3.chess.model;

import org.java3.chess.tools.Color;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    private Player from;

    @ManyToOne
    @JoinColumn(name = "to_id", nullable = false)
    private Player to;

    @Column(nullable = false, length = 5)
    private String color;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Challenge() {
    }

    public Challenge(Player from, Player to, Color color) {
        this.from = from;
        this.to = to;
        this.color = color.getValue();
        timestamp = Instant.now().atZone(ZoneId.of("GMT")).toLocalDateTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
