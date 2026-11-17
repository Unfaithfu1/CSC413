## 1. Group: Alone
## 2. Tool: Github Copilot
## 3. Other View: terminal

## 4. Runs
### m7-run-1
Model Request: "Read AGENTS.md and follow its instructions. Inspect my existing project then implement a new Swing view described with the provided restrictions"


### m7-run-2
Model Request: "Read AGENTS.md and follow its instructions. Inspect my existing project then change our Swing view described with the provided restrictions. SwingView.java is reimplementing code"


### m7-run-3
Model Request: "Read AGENTS.md and follow its instructions. Inspect my existing project then change Swing view described with the provided restrictions
Currently the Swing UI is showing black dots on empty spaces. Change it so it doesn't show those dots"




## 5. Rule Added
Reuse existing classes and methods instead of remaking the functionality. Read all of the available files of the project before implementing to ensure no duplicate code.

Reasoning: During the first prompt for the Coding Agent it reimplemented our pieceGlyph class into the SwingView which we should not do since its duplicating our code.

## 6. Review

File: SwingView.java
Lines: 46-108
Method: render
Decision: Keep
Reasoning: Render is now correctly showing empty spaces where applicable

File: SwingBoardView.java
Lines: 268-277
Method: pieceGlyph
Decision: Reject
Reasoning: PieceGlyph is already implemented in our PieceGlyph class so there is no reason for it to be reimplemented in our Swing View

File: SwingBoardView.java
Lines: 171-223
Method: handleSquareClick
Decision: Change
Reasoning: handleSquareClick is currently calling NeedsPromotion which is checking if a pawn can promot but we already have that implemented in the pawn class. The NeedsPromotion is just reimplementing it and handleSquareClick is calling it when it should just let the pawn piece handle promotions

## 7. Comparing views

### 8. Half Finished move
The terminal doesn't have a "half-finished move" as it requires both the piece to move and the location to move passed at the same time. The swing view on the other hand has "Position selectedSquare" which is storing the clicked square.

### 9. Where legalmoves
Both terminal and swing view rely on the controller to validate and handle the legalmoves. Swing still finds legalmoves in its own class to update the UI to tell the players what the available moves are in green.

### 10. Promotion
Terminal view has the pawn generate the promotion moves for the pawn. THe controller receives which move to play and Board.java updates the board with the promotion

Swing view gives the user a UI to choose what promotion to do. The controller finds the matching move and calls swings promotionChoice. Swing then returns the PieceType to promote the pawn and Game and Board updates the board with the promoted Piece.

### 11. Easier
I think swing view is easier as when testing you wouldn't need to type out the moves and just click. It also shows you the PieceGlyphs and allows you too see all the available moves that a piece can play letting you debug easier if any piece has a move it shouldn't. The terminal view code is easier to read and change but debugging requires more work.

### 12. Applied
The entire SwingView and TerminalView is from Copilot. It can be found in the SwingView.java and TerminalView.java class in src\main\java\edu\sfsu\csc413\chess\view

### 13. Debugger evidence

Breakpoint: SwingView.java, line 133 (initializeUI)

Predicted behavior: initializeUI gets called only from start(). It should first create the actual window the game will be put on. After looking up JFrame and JPanel the Panels should get the borderlayout and group them seperately. Then the method should let the words to be SanSerif in line 141. Then it adds specific borderlayouts to the 4 directions like an undo button and turn count. It should then create the actual chess piece starting from line 147. Then it looks like it should add more stuff to the bottom part of the border. Before initializing the window.

Observed Behavior: So it does create the initial window first before adding any UI. It seems like it actually creates the top border first from line 140 - 145 which I initially thought was the entire border around the window since I saw 'east' and 'west' but it looks like its just east and west on the 'topBar'. Then it does create the center and then the bottom so it looks like the Agent made it create the UI from top to bottom. Then it turns initialize to true so the next time start gets called it doesn't have to re initilize the UI.

### 14. By hand
I wanted to make it so the sides changed after each turn to match the 'sidetoMove'. Start() should be how the view is being created. I know initializeUI is only making the window popup I needed to look at render sinces it managing all the pieces. I'll be honest I don't really know why it worked but I saw that position/piece was the variable that was affecting alot of the render so I thought what if we change how we get position. Like how we did the toString and parse from Milestone 1 or 2 I decided to swap around the 1- to column and row depending on the color. Since the original window had white at all times and it was 1 - row I assumed that black would be 1 - column which seemed to work. 

