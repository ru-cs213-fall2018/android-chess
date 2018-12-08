package com.qwezey.androidchess.piece;

import com.qwezey.androidchess.board.Board;
import com.qwezey.androidchess.board.Square;
import com.qwezey.androidchess.chess.Color;

/**
 * Functional interface for creating a new piece
 * @author Ammaar Muhammad Iqbal
 */
public interface PieceConstructor {
    public Piece construct(Board board, Square square, Color color);
}
