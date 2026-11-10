package edu.sfsu.csc413.chess.model;

public record Position(int file, int rank){
    public static final int BOARD_SIZE = 8;

    public static Position parse(String algebraic) {
        // Get the file and rank from the notation
        char file = algebraic.charAt(0);
        char rank = algebraic.charAt(1);
        // Return the position with 0-based indexing
        return new Position((char) file - 'a', (char) rank - '1');
    }

    public Position offsetOrNull(int fileDelta, int rankDelta){
        // Get the new file and rank by adding the deltas
        int newfile = file + fileDelta;
        int newRank = rank + rankDelta;
        // If the new position is out of bounds return null
        if (!isOnBoard(newfile, newRank)){
            return null;
        }
        // Otherwise return the new position
        else return new Position(newfile, newRank);
    }

    public static boolean isOnBoard(int file, int rank) {
        return file >= 0 && file < BOARD_SIZE && rank >= 0 && rank < BOARD_SIZE;
    }

    public Position {
        if (!isOnBoard(file, rank)) {
            throw new IllegalArgumentException(
                    "Position off board: file=" + file + ", rank=" + rank);
        }
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + file) + (char) ('1' + rank);
    }

}