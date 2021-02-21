package org.java3.chess.tools;

public enum Result {

    UNDEFINED("Play continues"),
    WHITE_WON_BY_CHECKMATE("White won by checkmate"),
    WHITE_WON_BY_RESIGNATION("White won by resignation"),
    BLACK_WON_BY_CHECKMATE("Black won by checkmate"),
    BLACK_WON_BY_RESIGNATION("Black won by resignation"),
    DRAW_BY_FIFTY_MOVE_RULE("Draw by fifty move rule"),
    DRAW_BY_STALEMATE("Draw by stalemate"),
    DRAW_BY_INSUFFICIENT_MATERIAL("Draw by insufficient material");

    private final String description;

    Result(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
