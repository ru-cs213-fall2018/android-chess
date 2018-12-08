package board;

import chess.Color;
import piece.Piece;

/**
 * Represents a square on the board
 * @author Ammaar Muhammad Iqbal
 */
public class Square {

    private Coordinate coordinate;
    private Color color;
    private Piece piece;

    /**
     * Creates a new square
     * @param coordinate Coordinate of the square on the grid
     * @param color Color of the square
     * @param piece Piece on the square
     */
    Square(Coordinate coordinate, Color color, Piece piece) {
        this.coordinate = coordinate;
        this.color = color;
        this.piece = piece;
    }

    /**
     * Creates a new square without a piece
     * @param coordinate Coordinate of the square on the grid
     * @param color Color of the square
     */
    Square(Coordinate coordinate, Color color) {
        this(coordinate, color,null);
    }

    /**
     * @return Coordinate of the square
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @return Color of this square
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return Piece on this square or null if no piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Set a piece on this square
     * @param piece The piece to add or null if no piece
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * Remove the piece from the square
     */
    public void removePiece() {
        this.piece = null;
    }

    /**
     * @return If this square has a piece on it
     */
    public boolean hasPiece() {
        return this.piece != null;
    }

    /**
     * @return ASCII representation of what the square looks like
     */
    @Override
    public String toString() {
        if (this.piece != null) return this.piece.toString();
        else if (this.color == color.Black) return "##";
        else return "  ";
    }
}
