package edu.sfsu.csc413.chess.model;
import java.util.List;
import java.util.ArrayList;

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        for (int rank = Position.BOARD_SIZE - 1; rank >= 0; rank--) {
            int emptyFile = 0;
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                Piece piece = board[file][rank];

                if (piece == null){
                    emptyFile++;
                } else {
                    if (emptyFile > 0){
                        result.append(emptyFile);
                        emptyFile = 0;
                    }

                    result.append(piece.symbol());
                }
            }

            if (emptyFile > 0){
                result.append(emptyFile);
            }

            if (rank > 0){
                result.append('/');
            }
        }
        return result.toString();
    }
}