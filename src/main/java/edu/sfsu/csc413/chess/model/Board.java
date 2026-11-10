package edu.sfsu.csc413.chess.model;
import java.util.List;
import java.util.ArrayList;
import edu.sfsu.csc413.chess.factory.*;

public class Board {
    private final Piece[][] board;

    // make a new board that is BOARD_SIZE file and rank(8 by 8)
    public Board(){
        board = new Piece[Position.BOARD_SIZE][Position.BOARD_SIZE];
    }

    // to get a piece we get a given location and return its file and rank location back
    public Piece pieceAt(Position position){
        return board[position.file()][position.rank()];
    }

    // For isEmpty we can use pieceAt and return if its null or not
    public boolean isEmpty(Position position){
        return pieceAt(position) == null;
    }

    // For place we get the given position in our board and place our piece
    public void place(Position position, Piece piece){
        board[position.file()][position.rank()] = piece;
    }

    // for positionOf it want to get the positions of all specific color
    public List<Position> positionsOf(Color color) {
        // Make a list to store the positions
        List<Position> positions = new ArrayList<>();
        // Loop around the whole board
        for (int file = 0; file < Position.BOARD_SIZE; file++){
            for (int rank = 0; rank < Position.BOARD_SIZE; rank++){
                Piece piece = board[file][rank];
                // If the piece is not null and matches the color we add it to the list
                if (piece != null && piece.color() == color){
                    positions.add(new Position(file, rank));
                }
            }
        }
        return positions;
    }

    public void apply(Move move) {
        // Get the piece at the position
        Piece piece = pieceAt(move.from());
        // Move the piece to the new position and handle promotion if needed
        Piece destinationPiece = move.isPromotion() ? PieceFactory.create(move.promotesTo(), piece.color()) : piece;
        // Place the piece at the new position
        place(move.to(), destinationPiece);
        // Set the old position to null 
        place(move.from(), null);
    }

    public void undo(Move move) {
        // Move the piece back to its old position
        place(move.from(), move.moved());
        // Restore a captured piece if it was captured
        place(move.to(), move.captured());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        // Loop through the ranks on the board
        for (int rank = Position.BOARD_SIZE - 1; rank >= 0; rank--) {
            int emptyFile = 0;
            // Loop through the files on the board
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                // Get the piece at each file and rank
                Piece piece = board[file][rank];
                // If no piece is found increment emptyFile
                if (piece == null){
                    emptyFile++;
                } else {
                    // If there are empty files before a piece 
                    if (emptyFile > 0){
                        // Append the number of empty files to result
                        result.append(emptyFile);
                        emptyFile = 0;
                    }
                    // Append the pieces symbol to results
                    result.append(piece.symbol());
                }
            }
            // If there are empty files at the end of rank
            if (emptyFile > 0){
                // Append the number of empty files to result
                result.append(emptyFile);
            }
            // Append a '/' to seperate the ranks
            if (rank > 0){
                result.append('/');
            }
        }
        // Return the result
        return result.toString();
    }

    public Position kingPosition(Color color) {
        // Loop through the board (file and rank)
        for (int file = 0; file < Position.BOARD_SIZE; file++) {
            for (int rank = 0; rank < Position.BOARD_SIZE; rank++) {
                // Get the piece at the current file and rank
                Piece piece = board[file][rank];
                // If the piece isn't null and matches the color and is a king
                if (piece != null && piece.color() == color && piece instanceof King) {
                    // Return the position of the king
                    return new Position(file, rank);
                }
            }
        }
        // If no king is found return null
        return null;
    }
}