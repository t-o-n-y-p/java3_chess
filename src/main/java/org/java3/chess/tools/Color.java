package org.java3.chess.tools;

public enum Color {

    WHITE("white"),
    BLACK("black");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
