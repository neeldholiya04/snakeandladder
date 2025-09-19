package org.example.engine;

import org.example.board.Board;
import org.example.dice.DiceSet;
import org.example.player.BotPlayer;
import org.example.player.HumanPlayer;
import org.example.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameEngine {
    private static GameEngine instance;
    private final Scanner scanner = new Scanner(System.in);

    private GameEngine() {}

    public static GameEngine getInstance()
    {
        if (instance == null){
            synchronized (GameEngine.class) {
                if (instance == null){
                    instance = new GameEngine();
                }
            }
        }
        return instance;
    }

    public void start() {
        System.out.println("Welcome to Snake & Ladder");

        int n = askInt("Enter board size (n for n*n board, n>=2): ", 2, 20);
        int playersCount = askInt("Enter number of players (>=2): ", 2, 20);
        int maxDice = Math.max(1, playersCount / 6);
        if (maxDice == 0) maxDice = 1;
        int diceCount = askInt("Enter number of dice to use (1 to " + maxDice + "): ", 1, maxDice);

        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playersCount; i++) {
            System.out.print("Enter player type and name (H:Human/B:Bot Name): ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) input = "HPlayer-" + i;

            char type = Character.toUpperCase(input.charAt(0));
            String name = input.length() > 1 ? input.substring(1).trim() : "Player-" + i;

            Player p = (type == 'B') ? new BotPlayer("B" + i, name) : new HumanPlayer("P" + i, name);
            players.add(p);
        }

        DiceSet diceSet = DiceSet.getInstance(diceCount);
        Board board = Board.getInstance(n, players);

        runGame(board, diceSet, players);
    }

    private int askInt(String prompt, int min, int max) {
        int val;
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            try {
                val = Integer.parseInt(s);
                if (val >= min && val <= max) break;
            } catch (Exception ignored) {}
            System.out.println("Invalid input. Try again.");
        }
        return val;
    }

    private void runGame(Board board, DiceSet diceSet, List<Player> players) {
        boolean gameOver = false;
        int turn = 0;

        board.printBoard();

        while (!gameOver) {
            Player current = players.get(turn % players.size());

            System.out.println("=== TURN " + (turn + 1) + " ===");
            System.out.println(current.getName() + "'s turn");
            System.out.println("Current position: " + current.getPosition());

            int roll = handlePlayerTurn(current, diceSet);
            int newPos = board.movePlayer(current, roll);

            System.out.println("Moved to position: " + newPos);

            if (newPos == board.getLastCell()) {
                board.printBoard();
                System.out.println("\nGame Over! Winner: " + current.getName());
                System.out.println("\nFinal Positions:");
                players.forEach(p -> System.out.println(p.getName() + " → " + p.getPosition()));
                gameOver = true;
            } else {
                board.printBoard();

                Player nextPlayer = players.get((turn + 1) % players.size());

                if (current instanceof HumanPlayer && nextPlayer instanceof HumanPlayer) {
                    System.out.println("\nPress Enter to continue to " + nextPlayer.getName() + "'s turn...");
                } else if (current instanceof BotPlayer && nextPlayer instanceof BotPlayer) {
                    System.out.println("\nNext: " + nextPlayer.getName() + "'s turn...");
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ignored) {}
                } else {
                    System.out.println("\nNext: " + nextPlayer.getName() + "'s turn...");
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException ignored) {}
                }
            }

            turn++;
        }
    }

    private int handlePlayerTurn(Player current, DiceSet diceSet) {
        if (current instanceof HumanPlayer) {
            return handleHumanTurn((HumanPlayer) current, diceSet);
        } else if (current instanceof BotPlayer) {
            return handleBotTurn((BotPlayer) current, diceSet);
        } else {
            return current.takeTurn(diceSet);
        }
    }

    private int handleBotTurn(BotPlayer player, DiceSet diceSet) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}

        int roll = diceSet.roll();
        System.out.println(player.getName() + " (Bot) rolled: " + roll);

        return roll;
    }

    private int handleHumanTurn(HumanPlayer player, DiceSet diceSet) {

        System.out.println();
        System.out.print(player.getName() + ", press Enter to roll dice... ");

        waitForEnterKey();

        int roll = diceSet.roll();
        System.out.println(player.getName() + " rolled: " + roll);

        return roll;
    }

    private void waitForEnterKey() {
        try {
            while (true) {
                String input = scanner.nextLine();
                break;
            }
        } catch (Exception e) {
            System.out.println("Input error, continuing...");
        }
    }
}