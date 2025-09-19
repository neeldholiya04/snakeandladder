package org.example.board;

import org.example.player.Player;

import java.util.*;

public class Board {
    private static Board instance;
    private final int n;
    private final int size;
    private final int lastCell;
    private final Map<Integer, IJump> jumps = new HashMap<>();
    private final List<Player> players;
    private final Random rand = new Random();

    private Board(int n, List<Player> players) {
        this.n = n;
        this.size = n * n;
        this.lastCell = size;
        this.players = players;
        generateRandomJumps();
    }

    public static Board getInstance(int n, List<Player> players) {
        if (instance == null) {
            synchronized (Board.class) {
                if (instance == null) {
                    instance = new Board(n, players);
                }
            }
        }
        return instance;
    }

    private void generateRandomJumps() {
        int total = rand.nextInt(n) + 1;
        for (int i = 0; i < total; i++) {
            int start = rand.nextInt(lastCell - 1) + 1;
            int end = rand.nextInt(lastCell - 1) + 1;
            if (start == end || jumps.containsKey(start)) continue;
            IJump jump = (start > end) ? new Snake(start, end) : new Ladder(start, end);
            jumps.put(jump.from(), jump);
        }
    }

    public int movePlayer(Player player, int roll) {
        int pos = player.getPosition() + roll;
        if (pos > lastCell) pos = lastCell;

        IJump j = jumps.get(pos);
        if (j != null) {
            if (j.isSnake()) System.out.println("Snake:::::: " + player.getName() + " bitten, slides to " + j.to());
            else System.out.println("Ladder:::::: " + player.getName() + " climbs to " + j.to());
            pos = j.to();
        }

        player.setPosition(pos);
        return pos;
    }

    public void printBoard() {
        System.out.println("\n--- Board ---");
        for (int r = n; r >= 1; r--) {
            for (int c = 1; c <= n; c++) {
                int cell = (r % 2 == 0)
                        ? (r - 1) * n + (n - c + 1)
                        : (r - 1) * n + c;

                StringBuilder sb = new StringBuilder(String.format("%3d", cell));

                IJump j = jumps.get(cell);
                if (j != null) sb.append(j.isSnake() ? "S→" : "L→").append(j.to());

                List<String> occupants = new ArrayList<>();
                for (Player p : players) if (p.getPosition() == cell) occupants.add(p.getId());
                if (!occupants.isEmpty()) sb.append(occupants);

                System.out.print(String.format("%-12s", sb));
            }
            System.out.println();
        }
        System.out.println("-------------\n");
    }

    public int getLastCell() { return lastCell; }
}