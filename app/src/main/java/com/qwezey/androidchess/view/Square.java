package com.qwezey.androidchess.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.qwezey.androidchess.logic.board.Coordinate;

/**
 * A square in the board view
 */
public class Square {

    private Paint paint;
    private Rect rect;
    private Coordinate c;

    /**
     * @param c The coordinate of this square in the board view
     * @param color The initial color of the square
     */
    public Square(Coordinate c, int color) {
        rect = new Rect();
        paint = new Paint();
        paint.setColor(color);
        this.c = c;
    }

    public void updateRect(int containerWidth, int containerHeight) {

        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int left = c.getX() * rectWidth;
        int top = (7 - c.getY()) * rectHeight;
        int right = left + rectWidth;
        int bottom = top + rectHeight;

        rect.set(left, top, right, bottom);
    }

    public Rect getRect() {
        return rect;
    }

    public Paint getPaint() {
        return paint;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(getRect(), getPaint());
    }
}
