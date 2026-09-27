package edu.sfsu.csc413.chess.model;
import java.util.List;
import java.util.ArrayList;

public abstract class Piece{

    private Color color;
    private PieceType type;

    protected Piece(Color color, PieceType type){
        this.color = color;
        this.type = type;
    }

    public Color color(){
        return color;
    }

    public PieceType type(){
        return type;
    }

    public char symbol() {
        char letter = type.symbol();
        return color == color.WHITE ? letter : Character.toLowerCase(letter);
    }

    public abstract List<Move> pseudoLegalMoves(Board board, Position from);

    public boolean attacks(Board board, Position from, Position target){
        List<Move> moves = pseudoLegalMoves(board, from);
        for (Move move : moves) {
            if (move.to().equals(target)) {
                return true;
            }
        }
        return false;
    }

    protected List<Move> slidingMoves(Board board, Position from, int[][] directions){
        List<Move> moves = new ArrayList<>();
        for (int[] direction : directions) {
            int fileDelta = direction[0];
            int rankDelta = direction[1];
            Position current = from;
            while (true) {
                if (!Position.isOnBoard(current.file() + fileDelta, current.rank() + rankDelta)) {
                    break;
                }
                current = new Position(current.file() + fileDelta, current.rank() + rankDelta);
                Piece targetPiece = board.pieceAt(current);
                if (targetPiece == null) {
                    moves.add(Move.quiet(from, current, this));
                } else {
                    if (targetPiece.color() != this.color()) {
                        moves.add(Move.capture(from, current, this, targetPiece));
                    }
                    break;
                }
            }
        }
        return moves;
    }

    protected List<Move> steppingMoves(Board board, Position from, int[][] offsets){
        List<Move> moves = new ArrayList<>();
        for (int[] offset : offsets) {
            int fileDelta = offset[0];
            int rankDelta = offset[1];
            if (!Position.isOnBoard(from.file() + fileDelta, from.rank() + rankDelta)) {
                continue;
            }
            Position target = new Position(from.file() + fileDelta, from.rank() + rankDelta);
            Piece targetPiece = board.pieceAt(target);
            if (targetPiece == null) {
                moves.add(Move.quiet(from, target, this));
            } else if (targetPiece.color() != this.color()) {
                moves.add(Move.capture(from, target, this, targetPiece));
            }
        }
        return moves;
    }


    @Override
    public String toString() {return String.valueOf(symbol());
    } 
}
