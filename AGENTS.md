M7 - Utilizing Coding Agent

Milestone Goal
This project is a Java Chess Game. The goal of this Milestone is to add new playable views using my already implemented architecture without changing the logic or controller.

Project Architecture
Before making an changes, look at the exist project to understand:
- Main: Entry point of Application (Makes the board and starts the game)
- GameController: Handles the communication between the games logic and views
- BoardView: THe interface that all views must implement
- PlayerAction: The actions submitted by the players
- ConsoleView and EmojiView: Existing implementations of BoardView
- Mode, Engine, and Factory package: Store the existing game state, rules and logic

Don't assume any method names or package structures. If utilizing them inspect the actual code before implementing anything

Protected Files and Packages
You must NOT modify:
- model/
- engine/
- factory/
- GameController
- BoardView
- PlayerAction
- ConsoleView
- EmojiView
- pom.xml
DONT change any existing game rules, logic, or controller behavior to support new view

Inspect m7.md for for any additional rules I might have forgotten

New Views

Create two different views:
1. Swing View
Create a chess interface using Java Swing
Requirements:
- Dont modify any protected files
- Displays an 8x8 board
- Makes chess piece easy to differenciate
- Allows players to click a piece and a destination
- Display whose turn it in in any corner
- Display check, checkmate, and stalemate at end of game
- Display illegal move message if neccesary
- Include an Undo button that uses PlayerAction.undo()
- Handle pawn promotion
- Integrate with existing GameController and BoardView
- The start method must create and display the window and return. It must never call controller. runLoop()

2. Terminal Chess View
Create a playable terminal chessboard view
Requirements:
- Don't modify any protected files
- Reuse existing project functionality if possible
- Don't duplicate or reimplement chess rules
- Use existing controller and PlayerAction 
- Keep the implemantation readable

Testing
After implementing the views:
1. Compile the porject using ./mvnw -q compile
2. Fix the errors in newly generated code without modifying protecting files
5. Explain how to launch view
3. Inform me of any errors
4. Explain which files were created or changed




