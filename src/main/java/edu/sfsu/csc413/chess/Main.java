package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.engine.*;
import edu.sfsu.csc413.chess.view.*;
import java.util.List;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        // Create new game
        Game game = new Game();

        // Select the view from the command line.
        BoardView view;
        if (args.length > 0) {
            String choice = args[0].toLowerCase();
            switch (choice) {
                case "emoji" -> view = new EmojiView();
                case "swing" -> view = new SwingView();
                case "terminal" -> view = new TerminalChessView();
                case "console" -> view = new ConsoleView();
                default -> view = new TerminalChessView();
            }
        } else {
            view = new TerminalChessView();
        }

        // Create a new game controller with the game and view
        GameController controller = new GameController(game, view);
        // Start the game loop
        controller.start();
    }

    private Main() {

    }
}
