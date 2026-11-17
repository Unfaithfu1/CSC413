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

