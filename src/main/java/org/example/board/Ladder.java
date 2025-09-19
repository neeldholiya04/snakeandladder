package org.example.board;

public class Ladder implements IJump {
    private final int start;
    private final int end;

    public Ladder(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public int from() { return start; }

    @Override
    public int to() { return end; }

    @Override
    public boolean isSnake() { return false; }
}