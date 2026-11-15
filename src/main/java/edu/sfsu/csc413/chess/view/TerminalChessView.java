package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.model.Color;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * A terminal chessboard that keeps the rules in the engine and just changes the
 * presentation. It is still a fully playable BoardView that drives from the
 * controller's runLoop.
 */
public class TerminalChessView extends ConsoleView {

    private final PrintStream terminalOut;
    private final Scanner terminalIn;
    private final Color boardPerspective;

    public TerminalChessView() {
        this(System.out, new Scanner(System.in), Color.WHITE);
    }

    public TerminalChessView(PrintStream out, Scanner in, Color perspective) {
        super(out, in, perspective);
        terminalOut = out;
        terminalIn = in;
        boardPerspective = perspective;
    }

    @Override
    public void render(Game game) {
        String[] boardLines = new TextBoardRenderer(PieceGlyphs.FIGURES)
                .render(game.board(), boardPerspective)
                .split("\\R");

        terminalOut.println();
        terminalOut.println("┌───────────────────┐");
        for (String line : boardLines) {
            terminalOut.println("│ " + line + " │");
        }
        terminalOut.println("└───────────────────┘");
        terminalOut.println(game.sideToMove() + " to move  |  " + game.status());
    }

    @Override
    public PlayerAction nextAction(Game game) {
        terminalOut.print("Enter move (e.g. e2e4, 'undo', 'moves', 'quit'): ");
        terminalOut.flush();
        return PlayerAction.parse(terminalIn.hasNextLine() ? terminalIn.nextLine() : "quit");
    }
}
