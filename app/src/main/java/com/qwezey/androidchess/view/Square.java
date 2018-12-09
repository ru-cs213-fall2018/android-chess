package com.qwezey.androidchess.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.qwezey.androidchess.logic.board.Coordinate;

/**
 * A square in the board view
 */
public class Square {

    private Paint paint;
    private Rect rect;
    private Coordinate c;
    private int color;

    /**
     * @param c     The coordinate of this square in the board view
     * @param color The initial color of the square
     */
    public Square(Coordinate c, int color) {
        rect = new Rect();
        paint = new Paint();
        paint.setColor(color);
        this.c = c;
        this.color = color;
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
        int left = c.getX() * rectWidth;
        int top = (7 - c.getY()) * rectHeight;
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
        getPaint().setColor(color);
    }
}
