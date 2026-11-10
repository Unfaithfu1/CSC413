package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.engine.GameStatus;
import edu.sfsu.csc413.chess.engine.MoveGenerator;
import edu.sfsu.csc413.chess.model.Move;

/**
 * Connects the player's decisions to the game: carry out what was asked, then
 * ask the view to redraw.
 *
 * <p>This is the C in MVC. It is the only class that knows about both the
 * {@link Game} and the {@link BoardView}. It holds no game state of its own:
 * the model owns the position, the view owns the display, and the controller
 * owns only the conversation between them.
 *
 * <p>When you are done, this class mentions no {@code Scanner}, no
 * {@code System.out}, no window, and none of the words "undo", "moves", or
 * "quit". Those belong to the view and to {@link PlayerAction#parse}.
 */
public class GameController {

    private final Game game;
    private final BoardView view;

    public GameController(Game game, BoardView view) {
        this.game = game;
        this.view = view;
    }

    /** The game being played, so a view can read it in order to draw it. */
    public Game game() {
        return game;
    }

    /**
     * Greets the player, draws the opening position, and hands control to the
     * view with {@link BoardView#start}.
     */
    public void start() {
        view.showMessage("Welcome to my Chess Game!");
        view.render(game);
        view.start(this);
    }

    /**
     * The ask-and-handle loop, for views that can wait for input.
     *
     * <p>Asks the view for the next action and handles it, until the player
     * quits or the game is over. A console view calls this from its
     * {@code start}; an event-driven view never calls it.
     */
    public void runLoop() {
        // If the game isn't over we keep asking the players for moves
        while (!game.isGameOver()) {
            // Ask the view for the next action
            PlayerAction action = view.nextAction(game);
            // If the action is quit
            if (action instanceof PlayerAction.Quit) {
                // We show a message and close the view
                view.showMessage("Goodbye.");
                view.close();
                return;
            }
            // We handle any other action and render the game
            handle(action);
            view.render(game);
        }
        // If the game is Checkmate we show a message with the winning side
        if (MoveGenerator.status(game.board(), game.sideToMove()) == GameStatus.CHECKMATE) {
            view.showMessage("Checkmate. " + game.sideToMove().opposite() + " wins.");
        } else {
            // Else we show a message with the game status
            view.showMessage(MoveGenerator.status(game.board(), game.sideToMove()) + " Game over.");
        }
        // We close the view after the game finishes
        view.close();
    }

    /**
     * Carries out one player action and redraws.
     *
     * <p>Every route into the game goes through here: {@link #runLoop()} calls
     * it, and so will a button's click handler in M7. The handout lists what
     * each of the four actions does and what the player is told.
     */
    public void handle(PlayerAction action) {
        // Checks if the action is a move
        if (action instanceof PlayerAction.Move move) {
            // Find any move that matches the notation to play
            var legalMove = game.findLegalMove(move.notation());
            // If the move is a promotion we ask the player for what piece they want
            if (legalMove.isEmpty() && move.notation().length() == 4) {
                var promotions = game.legalMoves().stream().filter(candidate -> candidate.isPromotion()&& candidate.toString().startsWith(move.notation())).toList();
                // If there are any promotions
                if (!promotions.isEmpty()) {
                    // Ask the player for what piece they want
                    var choice = view.promotionChoice(game);
                    legalMove = promotions.stream().filter(candidate -> candidate.promotesTo() == choice).findFirst();
                }
            }
            // If the move is legal we play it
            if (legalMove.isPresent()) {
                Move selectedMove = legalMove.get();
                game.play(selectedMove);
                // Show a message so the player knows what move was played
                view.showMessage("Played " + selectedMove + ".");
            } else {
                // Show a message if the move isn't legal so the player knows
                view.showMessage("Not a legal move: '" + move.notation() + "'.");
            }    
        } else if (action instanceof PlayerAction.Undo) {
            // Call the undoLastMove to check if there is a move to undo
            if (game.undoLastMove().isPresent()) {
                // If there was a move we show to player so they know it was undone
                view.showMessage("Undid last move.");
            } else {
                // If there was no move to undo we show to player so they know that there was no history
                view.showMessage("Nothing to undo.");
                // Close the view since there is no history to undo
                view.close();
                return;
            }
        // If the action is to show moves we render the game so the player can see the moves
        } else if (action instanceof PlayerAction.ShowMoves) {
            view.render(game);
        // If the action is not recognized we throw an exception so the players know that its an invalid action
        } else {
            throw new IllegalArgumentException("Unknown action: " + action);
        }
    }
}
