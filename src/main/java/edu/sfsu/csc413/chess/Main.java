package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.model.*;
import edu.sfsu.csc413.chess.factory.BoardFactory;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        Board board = BoardFactory.standard();

        for (int rank = Position.BOARD_SIZE - 1; rank >= 0; rank--) {
            for (int file = 0; file < Position.BOARD_SIZE; file++) {
                Piece piece = board.pieceAt(new Position(file, rank));

                if (piece == null) {
                    System.out.print(". ");
                } else {
                    System.out.print(piece + " ");
                }
            }

            System.out.println();
        }
        
    }

    private Main() {

    }
}
