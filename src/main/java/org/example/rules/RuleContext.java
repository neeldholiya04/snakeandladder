package org.example.rules;

import org.example.board.Board;
import org.example.player.Player;
import org.example.dice.DiceSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class RuleContext {
    private final Board board;
    private final List<Player> players;
    private final Player currentPlayer;
    private final DiceSet diceSet;
    private final Map<String, Object> gameState;
    private int diceRoll;
    private int oldPosition;
    private int newPosition;
    private int turnNumber;

    public RuleContext(Board board, List<Player> players, Player currentPlayer, DiceSet diceSet) {
        this.board = board;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.diceSet = diceSet;
        this.gameState = new HashMap<>();
    }

    public Board getBoard() { return board; }
    public List<Player> getPlayers() { return players; }
    public Player getCurrentPlayer() { return currentPlayer; }
    public DiceSet getDiceSet() { return diceSet; }
    public Map<String, Object> getGameState() { return gameState; }

    public int getDiceRoll() { return diceRoll; }
    public void setDiceRoll(int diceRoll) { this.diceRoll = diceRoll; }

    public int getOldPosition() { return oldPosition; }
    public void setOldPosition(int oldPosition) { this.oldPosition = oldPosition; }

    public int getNewPosition() { return newPosition; }
    public void setNewPosition(int newPosition) { this.newPosition = newPosition; }

    public int getTurnNumber() { return turnNumber; }
    public void setTurnNumber(int turnNumber) { this.turnNumber = turnNumber; }

    public void putGameState(String key, Object value) { gameState.put(key, value); }
    public Object getGameState(String key) { return gameState.get(key); }
}