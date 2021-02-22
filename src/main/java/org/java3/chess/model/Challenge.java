package org.java3.chess.model;

import org.java3.chess.tools.Color;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "challenges")
public class Challenge {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(optional = false)
    private User from;

    @ManyToOne(optional = false)
    private User to;

    @Column(name = "target_color", nullable = false, length = 5)
    private String targetColor;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Challenge() {
    }

    public Challenge(User from, User to, Color targetColor) {
        this.from = from;
        this.to = to;
        this.targetColor = targetColor.getValue();
        timestamp = Instant.now().atZone(ZoneId.of("GMT")).toLocalDateTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(String targetColor) {
        this.targetColor = targetColor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
