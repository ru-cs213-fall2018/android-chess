package com.qwezey.androidchess.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.graphics.drawable.VectorDrawableCompat;

/**
 * A square in the board view
 */
public class Square {

    private com.qwezey.androidchess.logic.board.Square square;
    private Paint paint;
    private Rect rect;

    /**
     * @param square The corresponding square
     */
    public Square(com.qwezey.androidchess.logic.board.Square square) {
        this.square = square;
        rect = new Rect();
        paint = new Paint();
        setOriginalColor();
    }

    /**
     * Draw this square on the board
     *
     * @param canvas          The canvas to draw on
     * @param containerWidth  The width of the container to draw on
     * @param containerHeight The height of the container to draw on
     * @param piece           The chess piece to draw, can be null
     */
    public void draw(Canvas canvas, int containerWidth, int containerHeight, VectorDrawableCompat piece) {

        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int left = square.getCoordinate().getX() * rectWidth;
        int top = (7 - square.getCoordinate().getY()) * rectHeight;
        int right = left + rectWidth;
        int bottom = top + rectHeight;

        rect.set(left, top, right, bottom);

        canvas.drawRect(getRect(), getPaint());

        Paint blk = new Paint(Color.BLACK);
        canvas.drawLine(left, top, right, top, blk);
        canvas.drawLine(left, top, left, bottom, blk);
        canvas.drawLine(right, top, right, bottom, blk);
        canvas.drawLine(left, bottom, right, bottom, blk);

        if (piece != null) {
            piece.setBounds(rect);
            piece.draw(canvas);
        }
    }

    /**
     * @return The corresponding logic square
     */
    public com.qwezey.androidchess.logic.board.Square getSquare() {
        return square;
    }

    /**
     * @return The rectangle on this square
     */
    public Rect getRect() {
        return rect;
    }

    /**
     * @return The paint of this square
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * Sets the square to the original color
     */
    public void setOriginalColor() {
        boolean isWhite = square.getColor() == com.qwezey.androidchess.logic.chess.Color.White;
        getPaint().setColor(isWhite ?
                Color.rgb(243, 209, 168) :
                Color.rgb(216, 131, 62));
    }

    /**
     * Sets the square to a color that indicates a piece can move here
     */
    public void setCanMoveColor() {
        getPaint().setColor(Color.rgb(98, 140, 182));
    }

    /**
     * Indicates the square is a valid selection
     */
    public void setValidSelectionColor() {
        getPaint().setColor(Color.rgb(22, 139, 21));
    }

    /**
     * Indicates the square is in invalid selection
     */
    public void setInvalidSelectionColor() {
        getPaint().setColor(Color.rgb(235, 50, 43));
    }
}
