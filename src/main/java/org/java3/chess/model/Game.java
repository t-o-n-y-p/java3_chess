package org.java3.chess.model;

import org.java3.chess.tools.GameUtil;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "white_id", nullable = false)
    private Player white;

    @ManyToOne
    @JoinColumn(name = "black_id", nullable = false)
    private Player black;

    @ManyToOne
    @JoinColumn(name = "player_to_move_id", nullable = false)
    private Player playerToMove;

    @Column(nullable = false, length = 100)
    private String fen;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(length = 1000)
    private String description;

    @OneToMany
    @JoinColumn(name = "game_id")
    private List<Move> moves;

    public Game() {
    }

    public Game(Player white, Player black) {
        this.white = white;
        this.black = black;
        playerToMove = white;
        fen = GameUtil.STARTING_POSITION_FEN;
        isCompleted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getWhite() {
        return white;
    }

    public void setWhite(Player white) {
        this.white = white;
    }

    public Player getBlack() {
        return black;
    }

    public void setBlack(Player black) {
        this.black = black;
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(Player playerToMove) {
        this.playerToMove = playerToMove;
    }

    public String getFen() {
        return fen;
    }

    public void setFen(String fen) {
        this.fen = fen;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
