Snake & Ladder UML Diagrams

This folder contains the class diagram for the project, written in PlantUML.

Files
- class-diagram.puml — PlantUML source for the class diagram.

How to render
- Using IDE (recommended):
  - IntelliJ IDEA / VS Code: Install a PlantUML plugin. Open class-diagram.puml and use the plugin's preview to render.
- Using PlantUML server (no install):
  - Copy the contents of class-diagram.puml and paste into https://www.plantuml.com/plantuml/uml/ to view the diagram online.
- Using CLI:
  - Install Java and PlantUML. Then run:
    plantuml docs/class-diagram.puml
  - The output PNG/SVG will be generated alongside the file.

Scope covered
- org.example.Main
- org.example.engine.GameEngine
- org.example.board.Board, IJump, Snake, Ladder
- org.example.dice.Dice, DiceSet
- org.example.player.Player, HumanPlayer, BotPlayer

Notes
- Board and DiceSet are singletons in this implementation (modeled with notes in the diagram).
- Associations show ownership/usage; multiplicities are expressed with labels where meaningful.
