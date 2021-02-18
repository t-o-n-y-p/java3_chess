package org.java3.chess.tools;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GameUtil {

    public static final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static final Map<String, String> CHESS_PIECES = Map.ofEntries(
            Map.entry(" ", ""),
            Map.entry("K", "♔"),
            Map.entry("Q", "♕"),
            Map.entry("R", "♖"),
            Map.entry("B", "♗"),
            Map.entry("N", "♘"),
            Map.entry("P", "♙"),
            Map.entry("k", "♚"),
            Map.entry("q", "♛"),
            Map.entry("r", "♜"),
            Map.entry("b", "♝"),
            Map.entry("n", "♞"),
            Map.entry("p", "♟")
    );

    public static List<List<String>> getBoard(String fen, Color color) {
        List<List<String>> whiteBoard = Arrays.stream(
                Pattern.compile("[1-8]")
                        .matcher(fen.split("\\s")[0])
                        .replaceAll(mr -> " ".repeat(Integer.parseInt(mr.group()))).split("/"))
                .map(r -> Arrays.stream(r.split("(?!^)"))
                        .map(CHESS_PIECES::get)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
        if (color == Color.WHITE) {
            return whiteBoard;
        }
        Collections.reverse(whiteBoard);
        return whiteBoard.stream().peek(Collections::reverse).collect(Collectors.toList());
    }

    public static int getHalfMovesSinceCaptureOrPawnMove(String fen) {
        return Integer.parseInt(fen.split("\\s")[4]);
    }

    public static int getNextMoveNumber(String fen) {
        return Integer.parseInt(fen.split("\\s")[5]);
    }

}
