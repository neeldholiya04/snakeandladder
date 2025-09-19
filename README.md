Snake & Ladder (Java, Maven)

A simple, console-based implementation of the classic Snake & Ladder game written in Java. The program supports Human and Bot players, configurable board size, and 1..N dice per roll. The game prints a textual board, handles snakes and ladders, and announces the winner upon reaching the last cell.

Table of Contents
- Features
- Getting Started
  - Prerequisites
  - Build
  - Run
- How to Play (CLI)
- Design Overview
  - Key Classes
  - Game Flow
  - Board Generation
- UML Diagram
- Project Structure

Features
- Console-driven gameplay with prompts and validation.
- Human and Bot players; any mix is supported.
- Configurable board size (n x n), players count, and number of dice.
- Multiple dice are summed per roll.
- Randomly generated snakes and ladders on each run.
- Clear board printout showing cell numbers, snakes/ladders, and player positions.
- Thread-safe singleton patterns for GameEngine, Board, and DiceSet instances used per run.

Getting Started
Prerequisites
- Java 17+ (as commonly used in modern Maven projects)
- Maven 3.8+

Build
- From the repository root:
  mvn -q -DskipTests package

Run
- After building:
  mvn -q exec:java -Dexec.mainClass="org.example.Main"

Alternatively, run the compiled JAR if configured. The default pom.xml may build classes without an executable JAR. If so, use the exec plugin command above.

How to Play (CLI)
1) On start, you will be prompted for:
   - Board size n (creates an n x n board).
   - Number of players (>= 2).
   - Number of dice to use: 1..maxDice, where maxDice scales with player count.
2) For each player, enter their type and name in the format:
   - H Alice  -> Human player named "Alice"
   - B R2D2   -> Bot player named "R2D2"
   If you simply press Enter, a default human player is created.
3) On a human turn, press Enter to roll the dice. Bot turns roll automatically.
4) The board is re-printed each turn, showing:
   - Cell numbers laid out in a zig-zag typical of Snake & Ladder boards.
   - Snakes and ladders in the form S→to or L→to next to cell numbers.
   - Current players on the cell as their short IDs in brackets.
5) The first player to land on the last cell wins.

Design Overview
Key Classes
- org.example.Main: Entry point; starts the GameEngine.
- org.example.engine.GameEngine: Orchestrates input, turn order, rolling, and determining the winner.
- org.example.board.Board: Represents the board; owns snakes/ladders (IJump) and players; handles movement and prints the board.
- org.example.board.IJump: Interface implemented by Snake and Ladder.
- org.example.board.Snake, org.example.board.Ladder: Jumps from one cell to another.
- org.example.dice.Dice, org.example.dice.DiceSet: Single die and a set of dice that rolls to a sum.
- org.example.player.Player, HumanPlayer, BotPlayer: Player hierarchy; each has id, name, and position; turn handling differs for human vs bot.

Game Flow
- GameEngine asks for configuration, creates DiceSet and Board singletons and the list of players.
- Each turn:
  - Current player rolls (human waits for Enter; bot auto-rolls).
  - Player moves; Board applies a snake or ladder if present on the landing cell.
  - Board reprints.
  - If the player reaches the last cell, the game ends and prints final positions.

Board Generation
- The board is n x n. The last cell is n*n.
- A random number of jumps (snakes or ladders) is generated based on n.
- For each jump, a random start and end are selected; if start > end it becomes a snake, else a ladder.
- Jumps are stored by their starting cell; multiple jumps cannot start at the same cell.

UML Diagram
- PNG (rendered):
  ![Snake & Ladder Class Diagram](docs/SnakeAndLadderClassDiagram.png)
- PlantUML source:
  See docs/class-diagram.puml


Project Structure
- pom.xml                          Maven build file
- src/main/java/...                Source code
- docs/
  - class-diagram.puml             PlantUML source for the diagram
