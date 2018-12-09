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

    /**
     * @param c     The coordinate of this square in the board view
     * @param color The initial color of the square
     */
    public Square(Coordinate c, int color) {
        rect = new Rect();
        paint = new Paint();
        paint.setColor(color);
        this.c = c;
    }

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

    public Rect getRect() {
        return rect;
    }

    public Paint getPaint() {
        return paint;
    }
}
