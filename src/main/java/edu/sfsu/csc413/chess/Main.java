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

        // If the user wants Emoji view or Console view
        BoardView view;
        if (args.length > 0 && args[0].equals("emoji")) {
            view = new EmojiView();
        } else {
            view = new ConsoleView();
        }

        // Create a new game controller with the game and view
        GameController controller = new GameController(game, view);
        // Start the game loop
        controller.start();
    }

    private Main() {

    }
}
