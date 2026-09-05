package edu.sfsu.csc413.chess.model;

public record Position(int file, int rank){
    public static final int BOARD_SIZE = 8;

    public static Position parse(String algebraic) {
        char file = algebraic.charAt(0);
        char rank = algebraic.charAt(1);
        return new Position((char) file - 'a', (char) rank - '1');
    }

    public Position offsetOrNull(int fileDelta, int rankDelta){
        int newfile = file + fileDelta;
        int newRank = rank + rankDelta;
        if (!isOnBoard(newfile, newRank)){
            return null;
        }
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