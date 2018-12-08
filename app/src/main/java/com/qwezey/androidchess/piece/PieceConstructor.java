package piece;

import board.Board;
import board.Square;
import chess.Color;

/**
 * Functional interface for creating a new piece
 * @author Ammaar Muhammad Iqbal
 */
public interface PieceConstructor {
    public Piece construct(Board board, Square square, Color color);
}
