package com.qwezey.androidchess.view;

import android.graphics.Color;
import android.graphics.Paint;

import com.qwezey.androidchess.logic.board.Square;
import com.qwezey.androidchess.logic.game.Player;
import com.qwezey.androidchess.logic.piece.Piece;

/**
 * A square in the board view
 */
public class SquareViewState {

    private Square square;
    private Paint paint;

    /**
     * @param square The corresponding square
     */
    public SquareViewState(com.qwezey.androidchess.logic.board.Square square) {
        this.square = square;
        paint = new Paint();
        setColor(SquareView.Color.ORIGINAL);
    }

    /**
     * @return The paint of this square
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @return The piece on this square
     */
    public Piece getPiece() {
        return square.getPiece();
    }

    /**
     * @return True if this square has a piece and false otherwise
     */
    public boolean hasPiece() {
        return square.hasPiece();
    }

    /**
     * @param player The player to check if
     * @return True if player's piece is on this square, false otherwise
     */
    public boolean hasPlayerPiece(Player player) {
        if (!hasPiece()) return false;
        return square.getPiece().getColor() == player.getColor();
    }

    /**
     * @param to The destination of this piece
     * @return True if can move the piece on this square to to
     */
    public boolean canMovePiece(SquareViewState to) {
        if (!square.hasPiece()) return false;
        Piece pieceToMove = square.getPiece();
        Square destination = to.square;
        return pieceToMove.canMove(destination) == null;
    }

    /**
     * @param to The destination for the piece on this square
     * @return True if the piece was moved to to, false otherwise
     */
    public boolean movePiece(SquareViewState to) {
        if (!canMovePiece(to)) return false;
        Piece pieceToMove = square.getPiece();
        Square destination = to.square;
        return pieceToMove.move(destination) == null;
    }

    /**
     * Sets the color of the square
     *
     * @param color The color to set to
     */
    public void setColor(SquareView.Color color) {

        int paintColor = 0;

        switch (color) {

            case ORIGINAL:
                boolean isWhite = square.getColor() == com.qwezey.androidchess.logic.chess.Color.White;
                paintColor = isWhite ?
                        Color.rgb(243, 209, 168) :
                        Color.rgb(216, 131, 62);
                break;

            case CAN_MOVE:
                paintColor = Color.rgb(98, 140, 182);
                break;

            case VALID_SELECTION:
                paintColor = Color.rgb(22, 139, 21);
                break;

            case INVALID_SELECTION:
                paintColor = Color.rgb(235, 50, 43);
                break;
        }

        getPaint().setColor(paintColor);
    }
}
