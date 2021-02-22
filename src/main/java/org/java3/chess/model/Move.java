package org.java3.chess.model;

import org.java3.chess.tools.GameUtil;

import javax.persistence.*;

@Entity
@Table(name = "moves")
public class Move {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(optional = false)
    private Game game;

    @Column(name = "move_number", nullable = false)
    private int moveNumber;

    @Column(nullable = false, length = 5)
    private String value;

    @Column(name = "repetition_info", nullable = false, length = 100)
    private String repetitionInfo;

    public Move() {
    }

    public Move(Game game, String value) {
        this.game = game;
        this.value = value;
        repetitionInfo = GameUtil.getPositionFromFen(game.getFen());
        moveNumber = GameUtil.getNextMoveNumber(game.getFen());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRepetitionInfo() {
        return repetitionInfo;
    }

    public void setRepetitionInfo(String repetitionInfo) {
        this.repetitionInfo = repetitionInfo;
    }
}
