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
        // Get all possible moves availble for a piece
        List<Move> moves = pseudoLegalMoves(board, from);
        // If the move can target a piece we return true
        for (Move move : moves) {
            if (move.to().equals(target)) {
                return true;
            }
        }
        // If no moves can target a piece return false
        return false;
    }

    protected List<Move> slidingMoves(Board board, Position from, int[][] offsets){
        List<Move> moves = new ArrayList<>();
        // Loop through each offset
        for (int[] offset : offsets) {
            // Get the file and rank delta
            int fileDelta = offset[0];
            int rankDelta = offset[1];
            // Store the current position
            Position current = from;
            // Loop until we reach out of bounds or hit a piece
            while (true) {
                // If the next position is out of bounds we break out the loop
                if (!Position.isOnBoard(current.file() + fileDelta, current.rank() + rankDelta)) {
                    break;
                }
                // If its on the board we set current to the next position
                current = new Position(current.file() + fileDelta, current.rank() + rankDelta);
                // Get the piece at the new current location
                Piece targetPiece = board.pieceAt(current);
                // If the target piece is null we add a quiet move
                if (targetPiece == null) {
                    moves.add(Move.quiet(from, current, this));
                } else {
                    // If the target piece is a different color we add a capture
                    if (targetPiece.color() != this.color()) {
                        // We only add a move if the piece is the other color
                        moves.add(Move.capture(from, current, this, targetPiece));
                    }
                    // If we hit a piece we break out the loop
                    break;
                }
            }
        }
        // Return all moves possible for piece
        return moves;
    }

    protected List<Move> steppingMoves(Board board, Position from, int[][] offsets){
        List<Move> moves = new ArrayList<>();
        // Loop through each pieces available offsets
        for (int[] offset : offsets) {
            // Get the file and rank delta
            int fileDelta = offset[0];
            int rankDelta = offset[1];
            // If the next position is out of bounds we continue to the next offset
            if (!Position.isOnBoard(from.file() + fileDelta, from.rank() + rankDelta)) {
                continue;
            }
            // If its on the board we update current position
            Position current = new Position(from.file() + fileDelta, from.rank() + rankDelta);
            // Get the piece at the new current location
            Piece targetPiece = board.pieceAt(current);
            // If the target piece is null we add a quiet move
            if (targetPiece == null) {
                moves.add(Move.quiet(from, current, this));
                // If the target piece is the other color we add a capture
            } else if (targetPiece.color() != this.color()) {
                moves.add(Move.capture(from, current, this, targetPiece));
            }
        }
        // Return all moves possible for piece
        return moves;
    }


    @Override
    public String toString() {return String.valueOf(symbol());
    } 
}
