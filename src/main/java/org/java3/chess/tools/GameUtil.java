package org.java3.chess.tools;

import xyz.niflheim.stockfish.StockfishClient;
import xyz.niflheim.stockfish.engine.enums.Option;
import xyz.niflheim.stockfish.engine.enums.Query;
import xyz.niflheim.stockfish.engine.enums.QueryType;
import xyz.niflheim.stockfish.engine.enums.Variant;
import xyz.niflheim.stockfish.exceptions.StockfishInitException;

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

    private static final StockfishClient client;

    static {
        try {
            client = new StockfishClient.Builder()
                    .setInstances(4)
                    .setOption(Option.Threads, 4)
                    .setVariant(Variant.BMI2)
                    .build();
        } catch (StockfishInitException e) {
            throw new RuntimeException("StockfishInitException: " + e.getMessage());
        }
    }

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

    @SuppressWarnings("unchecked")
    public static List<String> getLegalMoves(String fen) throws ExecutionException, InterruptedException {
        return (List<String>) client.submit(
                new Query.Builder(QueryType.Legal_Moves)
                        .setFen(fen)
                        .build(),
                result -> Arrays.asList(result.split("\\s"))
        ).get();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getCheckers(String fen) throws ExecutionException, InterruptedException {
        return (List<String>) client.submit(
                new Query.Builder(QueryType.Checkers)
                        .setFen(fen)
                        .build(),
                result -> Arrays.asList(result.split("\\s"))
        ).get();
    }

    public static String makeMove(String fen, String move) throws ExecutionException, InterruptedException {
        return (String) client.submit(
                new Query.Builder(QueryType.Make_Move)
                        .setFen(fen)
                        .setMove(move)
                        .build(),
                result -> result
        ).get();
    }
}
