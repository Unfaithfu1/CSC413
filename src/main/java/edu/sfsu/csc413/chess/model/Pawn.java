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
        int direction = color() == Color.WHITE ? 1 : -1;
        Position preMove = new Position(from.file(), from.rank() + direction);

        if (preMove.rank() != 0 && preMove.rank() != 7) {
            switch (from.rank()) {
                case 1:
                    if (board.isEmpty(new Position(from.file(), from.rank() + (color() == Color.WHITE ? 1 : -1)))) {
                        moves.add(Move.quiet(from, new Position(from.file(), from.rank() + (color() == Color.WHITE ? 1 : -1)), this));
                        if (board.isEmpty(new Position(from.file(), from.rank() + (color() == Color.WHITE ? 2 : -2)))) {
                        moves.add(Move.quiet(from, new Position(from.file(), from.rank() + (color() == Color.WHITE ? 2 : -2)), this));
                        }
                    }
                case 6:
                    if (from.rank() == 6) {
                        if (board.isEmpty(new Position(from.file(), from.rank() + (color() == Color.BLACK ? 1 : -1)))) {
                            moves.add(Move.quiet(from, new Position(from.file(), from.rank() + (color() == Color.BLACK ? 1 : -1)), this));
                            if (board.isEmpty(new Position(from.file(), from.rank() + (color() == Color.BLACK ? 2 : -2)))) {
                            moves.add(Move.quiet(from, new Position(from.file(), from.rank() + (color() == Color.BLACK ? 2 : -2)), this));
                            }
                        }
                    }
                default:
                    System.out.println("Default case for pawn at: " + from);
                    if (board.isEmpty(new Position(from.file(), from.rank() + (color() == Color.WHITE ? 1 : -1)))) {
                        moves.add(Move.quiet(from, new Position(from.file(), from.rank() + (color() == Color.WHITE ? 1 : -1)), this));
                    }
            }
        }

        Position captureLeft = new Position(from.file() - 1, from.rank() + (color() == Color.WHITE ? 1 : -1));
        Position captureRight = new Position(from.file() + 1, from.rank() + (color() == Color.WHITE ? 1 : -1));
        if (board.pieceAt(captureLeft) != null && board.pieceAt(captureLeft).color() != color()) {
            moves.add(Move.capture(from, captureLeft, this, board.pieceAt(captureLeft)));
        }
        if (board.pieceAt(captureRight) != null && board.pieceAt(captureRight).color() != color()) {
            moves.add(Move.capture(from, captureRight, this, board.pieceAt(captureRight)));
        }

        if ((color() == Color.WHITE && from.rank() == 6) || (color() == Color.BLACK && from.rank() == 0)) {
            for (PieceType choices : PROMOTION_CHOICES) {
                moves.add(Move.promotion(from, new Position(from.file(), from.rank() + (color() == Color.WHITE ? 1 : -1)), null, null, choices));
            }
        }
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
        List<Move> attacks = new ArrayList<>();
        attacks.add(Move.capture(from, new Position(from.file() + 1, from.rank() + (color() == Color.WHITE ? 1 : -1)), this, null));
        attacks.add(Move.capture(from, new Position(from.file() - 1, from.rank() + (color() == Color.WHITE ? 1 : -1)), this, null));

        Piece targetPiece = board.pieceAt(target);
        if (targetPiece != null && targetPiece.color() == this.color()) {
            return false;
        }

        for (Move attack : attacks) {
            if (attack.to().equals(target)) {
                return true;
            }
        }
        return false;
    }
}
