package edu.sfsu.csc413.chess;

import edu.sfsu.csc413.chess.model.*;
import edu.sfsu.csc413.chess.view.*;

/**
 * Entry point.
 *
 * <p>At M0 this does nothing but prove the toolchain works. It grows into the
 * real launcher as the engine appears underneath it.
 */
public final class Main {

    public static void main(String[] args) {
        Board board = new Board();

        PieceType[] backRank = {
            PieceType.ROOK,
            PieceType.KNIGHT,
            PieceType.BISHOP,
            PieceType.QUEEN,
            PieceType.KING,
            PieceType.BISHOP,
            PieceType.KNIGHT,
            PieceType.ROOK
        };

        for (int rank = 0; rank < Position.BOARD_SIZE; rank++){
            Piece whiteBack = new Piece(Color.WHITE, backRank[rank]);
            Position whitePosition = new Position(rank, 0);
            board.place(whitePosition, whiteBack);

            Piece whitePiece = new Piece(Color.WHITE, PieceType.PAWN);
            Position whitePawnPosition = new Position(rank, 1);
            board.place(whitePawnPosition, whitePiece);

            Piece blackBack = new Piece(Color.BLACK, backRank[rank]);
            Position blackPosition = new Position(rank, 7);
            board.place(blackPosition, blackBack);

            Piece blackPiece = new Piece(Color.BLACK, PieceType.PAWN);
            Position blackPawnPosition = new Position(rank, 6);
            board.place(blackPawnPosition, blackPiece);
        }
        System.out.println(new TextBoardRenderer(PieceGlyphs.LETTERS).render(board));
    }

    private Main() {

    }
}
