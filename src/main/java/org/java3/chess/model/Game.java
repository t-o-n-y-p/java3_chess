package org.java3.chess.model;

import org.java3.chess.tools.GameUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(optional = false)
    private User white;

    @ManyToOne(optional = false)
    private User black;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_to_move_id")
    private User playerToMove;

    @Column(nullable = false, length = 100)
    private String fen;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted;

    @Column(length = 1000)
    private String description;

    @Column(name = "legal_moves", nullable = false)
    private String legalMoves;

    @OneToMany
    @JoinColumn(name = "game_id")
    private List<Move> moves;

    public Game() {
    }

    public Game(User white, User black) {
        this.white = white;
        this.black = black;
        playerToMove = white;
        fen = GameUtil.STARTING_POSITION_FEN;
        legalMoves = GameUtil.STARTING_POSITION_LEGAL_MOVES;
        isCompleted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getWhite() {
        return white;
    }

    public void setWhite(User white) {
        this.white = white;
    }

    public User getBlack() {
        return black;
    }

    public void setBlack(User black) {
        this.black = black;
    }

    public User getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(User playerToMove) {
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

    public String getLegalMoves() {
        return legalMoves;
    }

    public void setLegalMoves(String legalMoves) {
        this.legalMoves = legalMoves;
    }

    public List<String> getPositionHistory() {
        if (moves == null) {
            return new ArrayList<>();
        }
        return moves.stream()
                .map(Move::getRepetitionInfo)
                .collect(Collectors.toList());
    }
}
