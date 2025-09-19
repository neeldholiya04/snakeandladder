package org.example.board;

public class Snake implements IJump {
    private final int head;
    private final int tail;

    public Snake(int head, int tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public int from() { return head; }

    @Override
    public int to() { return tail; }

    @Override
    public boolean isSnake() { return true; }
}
