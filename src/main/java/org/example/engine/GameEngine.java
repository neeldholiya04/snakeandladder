package org.example.engine;

import org.example.board.Board;
import org.example.dice.DiceSet;
import org.example.player.BotPlayer;
import org.example.player.HumanPlayer;
import org.example.player.Player;
import org.example.rules.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameEngine {
    private static GameEngine instance;
    private final Scanner scanner = new Scanner(System.in);
    private final RuleManager ruleManager;

    public GameEngine() {
        this.ruleManager = new RuleManager();
    }

    public void start() {
        System.out.println("Welcome to Snake & Ladder");

        int n = askInt("Enter board size (n for n*n board, n>=2): ", 2, 20);
        int playersCount = askInt("Enter number of players (>=2): ", 2, 20);
        int maxDice = Math.max(1, playersCount / 6);
        if (maxDice == 0) maxDice = 1;
        int diceCount = askInt("Enter number of dice to use (1 to " + maxDice + "): ", 1, maxDice);

        System.out.println("\nWould you like to configure game rules? (y/N): ");
        String configRules = scanner.nextLine().trim().toLowerCase();
        if ("y".equals(configRules) || "yes".equals(configRules)) {
            RuleConfigurator configurator = new RuleConfigurator(ruleManager, scanner);
            configurator.configureRules();
        }

        List<Player> players = setupPlayers(playersCount);

        DiceSet diceSet = DiceSet.getInstance(diceCount);
        Board board = new Board(n, players);

        RuleContext context = new RuleContext(board, players, null, diceSet);

        List<RuleResult> startResults = ruleManager.executeRules(RuleType.START_GAME, context);
        printRuleResults(startResults);

        runGame(board, diceSet, players, context);
    }

    private List<Player> setupPlayers(int playersCount) {
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
        return players;
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

    private void runGame(Board board, DiceSet diceSet, List<Player> players, RuleContext baseContext) {
        boolean gameOver = false;
        int turn = 0;

        board.printBoard();

        while (!gameOver) {
            Player current = players.get(turn % players.size());

            RuleContext context = new RuleContext(board, players, current, diceSet);
            context.setTurnNumber(turn + 1);
            context.setOldPosition(current.getPosition());

            System.out.println("=== TURN " + (turn + 1) + " ===");
            System.out.println(current.getName() + "'s turn");
            System.out.println("Current position: " + current.getPosition());

            List<RuleResult> turnStartResults = ruleManager.executeRules(RuleType.TURN_START, context);
            printRuleResults(turnStartResults);

            boolean extraTurn = false;

            int roll = handlePlayerTurnWithRules(current, context);
            context.setDiceRoll(roll);

            List<RuleResult> diceResults = ruleManager.executeRules(RuleType.DICE_ROLL, context);
            printRuleResults(diceResults);

            if (context.getGameState("extraTurn") != null &&
                    (Boolean) context.getGameState("extraTurn")) {
                extraTurn = true;
                context.putGameState("extraTurn", false);
            }

            List<RuleResult> movementResults = ruleManager.executeRules(RuleType.MOVEMENT, context);
            printRuleResults(movementResults);

            int newPos = context.getNewPosition() > 0 ?
                    context.getNewPosition() :
                    board.movePlayer(current, roll);

            context.setNewPosition(newPos);
            System.out.println("Moved to position: " + newPos);

            List<RuleResult> landingResults = ruleManager.executeRules(RuleType.LANDING, context);
            printRuleResults(landingResults);

            List<RuleResult> winningResults = ruleManager.executeRules(RuleType.WINNING, context);
            printRuleResults(winningResults);

            boolean gameEndedFromRules = winningResults.stream().anyMatch(RuleResult::isGameEnded);

            if (gameEndedFromRules) {
                board.printBoard();
                System.out.println("\nFinal Positions:");
                players.forEach(p -> System.out.println(p.getName() + " → " + p.getPosition()));
                gameOver = true;
            } else {
                List<RuleResult> turnEndResults = ruleManager.executeRules(RuleType.TURN_END, context);
                printRuleResults(turnEndResults);

                board.printBoard();

                if (!extraTurn) {
                    Player nextPlayer = players.get((turn + 1) % players.size());
                    handleTurnTransition(current, nextPlayer);
                    turn++;
                } else {
                    System.out.println(current.getName() + " gets an extra turn!");
                    handleTurnTransition(current, current);
                }
            }
        }
    }

    private int handlePlayerTurnWithRules(Player current, RuleContext context) {
        if (current instanceof HumanPlayer) {
            return handleHumanTurn((HumanPlayer) current, context.getDiceSet());
        } else if (current instanceof BotPlayer) {
            return handleBotTurn((BotPlayer) current, context.getDiceSet());
        } else {
            return current.takeTurn(context.getDiceSet());
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

    private void handleTurnTransition(Player current, Player next) {
        if (current instanceof HumanPlayer && next instanceof HumanPlayer && current != next) {
            System.out.println("\nPress Enter to continue to " + next.getName() + "'s turn...");
            waitForEnterKey();
        } else if (current instanceof BotPlayer && next instanceof BotPlayer && current != next) {
            System.out.println("\nNext: " + next.getName() + "'s turn...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException ignored) {}
        } else if (current != next) {
            System.out.println("\nNext: " + next.getName() + "'s turn...");
            try {
                Thread.sleep(800);
            } catch (InterruptedException ignored) {}
        }
    }

    private void printRuleResults(List<RuleResult> results) {
        for (RuleResult result : results) {
            if (result.isExecuted() && !result.getMessage().isEmpty()) {
                System.out.println(">>> " + result.getMessage());
            }
        }
    }

    private void waitForEnterKey() {
        try {
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Input error, continuing...");
        }
    }

    public RuleManager getRuleManager() {
        return ruleManager;
    }
}