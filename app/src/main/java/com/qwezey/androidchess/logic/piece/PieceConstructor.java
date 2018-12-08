package com.qwezey.androidchess.logic.piece;

import com.qwezey.androidchess.logic.board.Board;
import com.qwezey.androidchess.logic.board.Square;
import com.qwezey.androidchess.logic.chess.Color;

/**
 * Functional interface for creating a new piece
 * @author Ammaar Muhammad Iqbal
 */
public interface PieceConstructor {
    public Piece construct(Board board, Square square, Color color);
}
