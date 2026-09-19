package edu.sfsu.csc413.chess.model;

public enum PieceType { 
    PAWN('P'), KNIGHT('N'), BISHOP('B'), ROOK('R'), QUEEN('Q'), KING('K');

    private final char symbol;

    PieceType(char symbol) {
        this.symbol = symbol;
    }

    public char symbol(){
        return symbol;
    }

    public static PieceType fromSymbol(char letter){
        // Ensure the letter is uppercase to match our enum variables
        letter = Character.toUpperCase(letter);
        // Loop through enum to find if the given letter matches
        for (PieceType type : PieceType.values()) {
            // If we find a match we return the type corresponding to the Symbol
            if (type.symbol == letter){
                return type;
            }
        }
        // If no matches are found we will return an error
        throw new IllegalArgumentException("Not a valid chess piece");
    }
}