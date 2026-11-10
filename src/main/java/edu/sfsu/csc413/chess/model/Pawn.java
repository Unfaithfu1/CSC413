package edu.sfsu.csc413.chess.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The pawn — the piece that breaks every rule the others follow.
 *
 * <p>It is the only piece that moves in just one direction, the only one whose
 * capture differs from its move, the only one with a special first move, and
 * the only one that turns into something else. It is worth noticing that all of
 * that awkwardness is contained in this one file. No other class in the engine
 * knows that pawns are strange. That containment is the payoff of polymorphism:
 * the irregular case costs one class, not a special case in every method that
 * touches a piece.
 *
 * <p>En passant is not handled here. Like castling, it depends on the previous
 * move rather than on the current board, so it waits for Week 15 when
 * {@code Game} owns the move history.
 */
public class Pawn extends Piece {

    /**
     * What a pawn may become on reaching the far rank.
     */
    private static final PieceType[] PROMOTION_CHOICES = { PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT };

    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public List<Move> pseudoLegalMoves(Board board, Position from) {
        List<Move> moves = new ArrayList<>();
        // Determines the direction based on color
        int direction = color() == Color.WHITE ? 1 : -1;
        // Determines the next rank possible
        int nextRank = from.rank() + direction;
        // Helps tell if pawn has reached the other side
        int lastRank = color() == Color.WHITE ? Position.BOARD_SIZE - 1 : 0;
        int startingRank = color() == Color.WHITE ? 1 : Position.BOARD_SIZE - 2;

        // Returns moves if next rank for pawn is out of bounds
        if (nextRank < 0 || nextRank >= Position.BOARD_SIZE) {
            return moves;
        }

        // Check if the square in front is empty
        Position forward = new Position(from.file(), nextRank);
        if (board.isEmpty(forward)) {
            if (nextRank == lastRank) {
                // If the pawn has reached the other side, add the promotion move
                for (PieceType choice : PROMOTION_CHOICES) {
                    moves.add(Move.promotion(from, forward, this, null, choice));
                }
            } else {
                // The square in front is empty, so the pawn can move forward
                moves.add(Move.quiet(from, forward, this));
                // If the pawn is on its starting rank it can move two squares
                int PawnStart = color() == Color.WHITE ? 1 : Position.BOARD_SIZE - 2;
                // Check if the position two squares ahead is empty
                Position doubleStep = new Position(from.file(), PawnStart + 2 * direction);
                // If pawn is on its starting rank and square two ahead is empty add move
                if (from.rank() == startingRank && board.isEmpty(doubleStep)) {
                    moves.add(Move.quiet(from, doubleStep, this));
                }
            }
        }
        // Check for captures diagonally
        for (int fileOffset : new int[] {-1, 1}) {
            int targetFile = from.file() + fileOffset;
            if (targetFile < 0 || targetFile >= Position.BOARD_SIZE) {
                continue;
            }
            // Check the diagonal squares
            Position target = new Position(targetFile, nextRank);
            Piece captured = board.pieceAt(target);
            // Check if there is a piece of the opposite color
            if (captured != null && captured.color() != color()) {
                // If it can be promoted then add the promotion move
                if (nextRank == lastRank) {
                    for (PieceType choice : PROMOTION_CHOICES) {
                        moves.add(Move.promotion(from, target, this, captured, choice));
                        }
                } else {
                    // Otherwise add a capture move
                    moves.add(Move.capture(from, target, this, captured));
                }
            }
        }
        // Return all available moves for each pawn
        return moves;
    }

    /**
     * A pawn attacks the two squares diagonally ahead of it, whether or not
     * anything stands there.
     *
     * <p>This override exists because the inherited version answers "can this
     * piece move to that square", and for a pawn that is the wrong question.
     * An empty square in front of a pawn is a square the pawn can move to but
     * does <em>not</em> attack — which matters enormously for king safety: a
     * king may not be blocked from a square merely because a pawn could advance
     * onto it, but it certainly may not step onto a square a pawn guards.
     */
    @Override
    public boolean attacks(Board board, Position from, Position target) {
        int rankDirection = color() == Color.WHITE ? 1 : -1;
        // Check if the target position is one of the two diagonal squares
        Position rightCapture = from.offsetOrNull(1, rankDirection);
        Position leftCapture = from.offsetOrNull(-1, rankDirection);
        // Return true if the target position matches right of left capture.
        return target.equals(rightCapture) || target.equals(leftCapture);
    }
}