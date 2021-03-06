package org.java3.chess.tools;

import org.java3.chess.model.Color;
import org.java3.chess.model.User;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class GameUtil {

    public static final String STARTING_POSITION_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    public static final String STARTING_POSITION_LEGAL_MOVES
            = "a2a3 b2b3 c2c3 d2d3 e2e3 f2f3 g2g3 h2h3 a2a4 b2b4 c2c4 d2d4 e2e4 f2f4 g2g4 h2h4 b1a3 b1c3 g1f3 g1h3";

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
                        .matcher(fen.replaceAll("[\\s].*", ""))
                        .replaceAll(mr -> " ".repeat(Integer.parseInt(mr.group())))
                        .split("/"))
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

    public static String getPositionFromFen(String fen) {
        return Arrays.toString(Arrays.copyOfRange(fen.split("\\s"), 0, 4));
    }

    private static boolean isDrawByInsufficientMaterial(String fen) {
        return fen.matches("([1-8/]*[KkBbNn]){2,3}[1-8/]*[\\s].*");
    }

    private static boolean isDrawByFiftyMoveRule(String fen) {
        return Integer.parseInt(fen.split("\\s")[4]) >= 100;
    }

    private static boolean whiteToMove(String fen) {
        return fen.contains("w");
    }

    public static Result getResult(String fen, String legalMoves, List<String> positionHistory)
            throws ExecutionException, InterruptedException {
        if (isDrawByInsufficientMaterial(fen)) {
            return Result.DRAW_BY_INSUFFICIENT_MATERIAL;
        } else if (isDrawByFiftyMoveRule(fen)) {
            return Result.DRAW_BY_FIFTY_MOVE_RULE;
        } else if (Collections.frequency(positionHistory, getPositionFromFen(fen)) >= 2) {
            return Result.DRAW_BY_REPETITION;
        } else if (!legalMoves.isBlank()) {
            return Result.UNDEFINED;
        } else if (StockfishUtil.getCheckers(fen).isBlank()) {
            return Result.DRAW_BY_STALEMATE;
        } else if (whiteToMove(fen)) {
            return Result.BLACK_WON_BY_CHECKMATE;
        }
        return Result.WHITE_WON_BY_CHECKMATE;
    }

    public static void updateRatings(User white, User black, Result result) {
        if (result == Result.UNDEFINED) {
            return;
        }
        double actualPointsForWhite = 0.0;
        if (result.getWinningSide() == Color.WHITE) {
            actualPointsForWhite = 1.0;
        } else if (result.getWinningSide() == null) {
            actualPointsForWhite = 0.5;
        }
        double expectedPointsForWhite = 1 / (1 + Math.pow(10.0, (black.getRating() - white.getRating()) / 400));
        double ratingDifference = 20 * (actualPointsForWhite - expectedPointsForWhite);
        white.setRating(white.getRating() + ratingDifference);
        black.setRating(black.getRating() - ratingDifference);
    }

}
