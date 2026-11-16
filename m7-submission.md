Group: Alone
Tool: Github Copilot
Other View: terminal

## m7-run-1
Model Request: "Read AGENTS.md and follow its instructions. Inspect my existing project then implement a new Swing view described with the provided restrictions"

Review:
Reject PieceGlyph: PieceGlyph is already implemented in out PieceGlyph class in view there is not reason to have it be reimplemented in our Swing View
Change handleSquareClick: The handleSquareClick method is calling NeedsPromotion which is checking if a pawn can promote but we already have that implemented in our pawn.java so we should change it to implement the one in our pawn.java instead
Keep handleUndo: The handleUndo method is correctly calling our handleUndo branch in our GameController class while updating the UI aftering undoing

## m7-run-2
Model Request: "Read AGENTS.md and follow its instructions. Inspect my existing project then implement a new Swing view described with the provided restrictions"

"Reject PieceGlyph: PieceGlyph is already implemented in out PieceGlyph class in view there is not reason to have it be reimplemented in our Swing View
Change handleSquareClick: The handleSquareClick method is calling NeedsPromotion which is checking if a pawn can promote but we already have that implemented in our pawn.java so we should change it to implement the one in our pawn.java instead
Keep handleUndo: The handleUndo method is correctly calling our handleUndo branch in our GameController class while updating the UI aftering undoing"

Review:
Keep promotionChoice: Its now overriding the promotionChoice in BoardView which is allowed if we want to allow changing to other pieces other than queen
Keep formatColor|formatStatus: Its just for UI to tell the user whose turn it is and the status of the game doesn't reimplement any code and doesn't change any already implemented code.
Change render: Render is currently rendering all squares as a piece even if its an empty square. So when the game starts there are these black dots on each empty square.


