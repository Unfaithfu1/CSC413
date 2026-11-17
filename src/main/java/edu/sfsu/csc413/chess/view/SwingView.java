package edu.sfsu.csc413.chess.view;

import edu.sfsu.csc413.chess.engine.Game;
import edu.sfsu.csc413.chess.engine.GameStatus;
import edu.sfsu.csc413.chess.model.Color;
import edu.sfsu.csc413.chess.model.Move;
import edu.sfsu.csc413.chess.model.Piece;
import edu.sfsu.csc413.chess.model.PieceType;
import edu.sfsu.csc413.chess.model.Position;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashSet;
import java.util.Set;

/**
 * A Swing chessboard that behaves like a standard event-driven view.
 *
 * <p>It keeps the game rules in the model and lets the controller decide if a
 * move is legal. The view only turns clicks into {@link PlayerAction.Move}
 * objects, and it refreshes the board based on the controller's state.
 */
public class SwingView implements BoardView {

    private GameController controller;
    private Game lastGame;
    private Position selectedSquare;
    private JFrame frame;
    private JPanel boardPanel;
    private final JLabel turnLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton undoButton = new JButton("Undo");
    private boolean initialized;

    @Override
    public void render(Game game) {
        lastGame = game;
        if (game == null) {
            return;
        }
        if (!initialized) {
            return;
        }

        turnLabel.setText(formatColor(game.sideToMove()) + " to move");
        statusLabel.setText(formatStatus(game.status()));
        boardPanel.removeAll();

        Set<Position> legalTargets = new HashSet<>();
        if (selectedSquare != null) {
            for (Move move : game.legalMoves()) {
                if (move.from().equals(selectedSquare)) {
                    legalTargets.add(move.to());
                }
            }
        }

        for (int row = 0; row < Position.BOARD_SIZE; row++) {
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                Position position = new Position(file, Position.BOARD_SIZE - 1 - row);
                if (game.sideToMove() == Color.BLACK) {
                    position = new Position(Position.BOARD_SIZE - 1 - file, row);
                } 
                Piece piece = game.board().pieceAt(position);
                JButton square = new JButton();
                square.setMargin(new Insets(0, 0, 0, 0));
                square.setFocusPainted(false);
                square.setPreferredSize(new Dimension(78, 78));
                square.setFont(new Font("Segoe UI Symbol", Font.BOLD, 38));
                square.setHorizontalAlignment(SwingConstants.CENTER);
                square.setVerticalAlignment(SwingConstants.CENTER);

                if (selectedSquare != null && selectedSquare.equals(position)) {
                    square.setBackground(new java.awt.Color(255, 215, 0));
                } else if (legalTargets.contains(position)) {
                    square.setBackground(new java.awt.Color(120, 205, 120));
                } else if ((row + file) % 2 == 0) {
                    square.setBackground(new java.awt.Color(240, 217, 181));
                } else {
                    square.setBackground(new java.awt.Color(181, 136, 99));
                }

                square.setBorder(BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60), 1));
                square.setText(piece == null ? "" : pieceGlyph(piece));
                square.setForeground(piece != null && piece.color() == Color.WHITE
                        ? new java.awt.Color(255, 255, 255)
                        : new java.awt.Color(10, 10, 10));
                if (piece != null && piece.color() == Color.BLACK) {
                    square.setOpaque(true);
                }
                final Position clicked = position;
                square.addActionListener(event -> handleSquareClick(clicked));
                boardPanel.add(square);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
        frame.pack();
    }

    @Override
    public void showMessage(String message) {
        messageLabel.setText(message);
    }

    @Override
    public void start(GameController controller) {
        this.controller = controller;
        if (!initialized) {
            initializeUi();
        }
        lastGame = controller.game();
        render(lastGame);
        frame.setVisible(true);
    }

    @Override
    public void close() {
        if (frame != null) {
            frame.dispose();
        }
    }

    private void initializeUi() {
        frame = new JFrame("Swing Chess");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topBar = new JPanel(new BorderLayout(8, 8));
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topBar.add(turnLabel, BorderLayout.WEST);
        undoButton.addActionListener(event -> handleUndo());
        topBar.add(undoButton, BorderLayout.EAST);
        root.add(topBar, BorderLayout.NORTH);

        boardPanel = new JPanel(new GridLayout(8, 8));
        boardPanel.setPreferredSize(new Dimension(640, 640));
        root.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new BorderLayout(8, 8));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomBar.add(statusLabel, BorderLayout.NORTH);
        bottomBar.add(messageLabel, BorderLayout.SOUTH);
        root.add(bottomBar, BorderLayout.SOUTH);

        frame.setContentPane(root);
        frame.pack();
        initialized = true;
    }

    private void handleUndo() {
        if (controller == null) {
            return;
        }
        controller.handle(new PlayerAction.Undo());
        selectedSquare = null;
        render(controller.game());
    }

    private void handleSquareClick(Position clickedPosition) {
        if (controller == null || controller.game() == null) {
            return;
        }

        Game game = controller.game();
        if (game.isGameOver()) {
            showMessage("Game is over. Press Undo to take back a move.");
            return;
        }

        Piece piece = game.board().pieceAt(clickedPosition);
        if (selectedSquare == null) {
            if (piece != null && piece.color() == game.sideToMove()) {
                selectedSquare = clickedPosition;
                showMessage("Selected " + clickedPosition + ".");
                render(game);
            } else {
                showMessage("Select one of your pieces first.");
            }
            return;
        }

        if (clickedPosition.equals(selectedSquare)) {
            selectedSquare = null;
            showMessage("Selection cleared.");
            render(game);
            return;
        }

        if (piece != null && piece.color() == game.sideToMove()) {
            selectedSquare = clickedPosition;
            showMessage("Selected " + clickedPosition + ".");
            render(game);
            return;
        }

        String notation = selectedSquare + clickedPosition.toString();
        if (needsPromotion(game, selectedSquare, clickedPosition)) {
            String promotion = askPromotion();
            if (promotion == null) {
                selectedSquare = null;
                showMessage("Move cancelled.");
                render(game);
                return;
            }
            notation += promotion;
        }

        controller.handle(new PlayerAction.Move(notation));
        selectedSquare = null;
        render(controller.game());
    }

    private String askPromotion() {
        Object[] options = {"Queen", "Rook", "Bishop", "Knight"};
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose a promotion",
                "Pawn promotion",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        return switch (choice) {
            case 1 -> "r";
            case 2 -> "b";
            case 3 -> "n";
            default -> "q";
        };
    }

    private static boolean needsPromotion(Game game, Position from, Position to) {
        Piece piece = game.board().pieceAt(from);
        return piece != null
                && piece.type() == PieceType.PAWN
                && to.rank() == game.sideToMove().promotionRank();
    }

    private static String formatColor(Color color) {
        return switch (color) {
            case WHITE -> "White";
            case BLACK -> "Black";
        };
    }

    private static String formatStatus(GameStatus status) {
        return switch (status) {
            case IN_PROGRESS -> "In progress";
            case CHECK -> "Check";
            case CHECKMATE -> "Checkmate";
            case STALEMATE -> "Stalemate";
        };
    }

    private static String pieceGlyph(Piece piece) {
        return switch (piece.type()) {
            case KING -> piece.color() == Color.WHITE ? "♔" : "♚";
            case QUEEN -> piece.color() == Color.WHITE ? "♕" : "♛";
            case ROOK -> piece.color() == Color.WHITE ? "♖" : "♜";
            case BISHOP -> piece.color() == Color.WHITE ? "♗" : "♝";
            case KNIGHT -> piece.color() == Color.WHITE ? "♘" : "♞";
            case PAWN -> piece.color() == Color.WHITE ? "♙" : "♟";
        };
    }
}
