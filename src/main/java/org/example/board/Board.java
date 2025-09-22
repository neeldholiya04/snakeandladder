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

    public Board(int n, List<Player> players) {
        this.n = n;
        this.size = n * n;
        this.lastCell = size;
        this.players = players;
        generateRandomJumps();
    }


    private void generateRandomJumps() {
        int total = rand.nextInt(n) + 1;
        Set<Integer> usedStartCells = new HashSet<>();
        Set<Integer> usedEndCells = new HashSet<>();

        int attempts = 0;
        int maxAttempts = total * 10;

        while (jumps.size() < total && attempts < maxAttempts) {
            attempts++;

            int start = rand.nextInt(lastCell - 2) + 2;

            int end = rand.nextInt(lastCell - 2) + 2;

            if (start == end) continue;

            if (usedStartCells.contains(start) || usedEndCells.contains(end)) continue;

            if (jumps.containsKey(start)) continue;

            if (Math.abs(start - end) < 2) continue;

            IJump jump = (start > end) ? new Snake(start, end) : new Ladder(start, end);

            jumps.put(jump.from(), jump);
            usedStartCells.add(start);
            usedEndCells.add(end);
        }
    }

    public int movePlayer(Player player, int roll) {
        return movePlayer(player, roll, new HashMap<>());
    }

    public int movePlayer(Player player, int roll, Map<String, Object> ruleContext) {
        int pos = player.getPosition() + roll;
        if (pos > lastCell) pos = lastCell;

        IJump j = jumps.get(pos);
        if (j != null) {
            boolean skipSnakes = ruleContext.containsKey("skipSnakes") && (Boolean) ruleContext.get("skipSnakes");
            boolean skipNextSnake = ruleContext.containsKey("skipNextSnake_" + player.getId()) &&
                    (Boolean) ruleContext.get("skipNextSnake_" + player.getId());

            if (j.isSnake() && (skipSnakes || skipNextSnake)) {
                if (skipNextSnake) {
                    ruleContext.put("skipNextSnake_" + player.getId(), false);
                }
                System.out.println("Snake avoided! " + player.getName() + " stays at position " + pos);
            } else {
                if (j.isSnake()) {
                    System.out.println("Snake bite! " + player.getName() + " slides from " + pos + " to " + j.to());
                } else {
                    System.out.println("Ladder climb! " + player.getName() + " climbs from " + pos + " to " + j.to());
                }
                pos = j.to();
            }
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
    public Map<Integer, IJump> getJumps() { return jumps; }
    public int getSize() { return size; }
    public int getN() { return n; }

    public boolean hasSnake(int position) {
        IJump jump = jumps.get(position);
        return jump != null && jump.isSnake();
    }

    public boolean hasLadder(int position) {
        IJump jump = jumps.get(position);
        return jump != null && !jump.isSnake();
    }

    public List<Player> getPlayersAt(int position) {
        List<Player> playersAtPosition = new ArrayList<>();
        for (Player player : players) {
            if (player.getPosition() == position) {
                playersAtPosition.add(player);
            }
        }
        return playersAtPosition;
    }
}