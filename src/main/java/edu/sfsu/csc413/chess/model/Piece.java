package edu.sfsu.csc413.chess.model;


public class Piece{

    private Color color;
    private PieceType type;

    public Piece(Color color, PieceType type){
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

    @Override
    public String toString() {return String.valueOf(symbol());} 
}
