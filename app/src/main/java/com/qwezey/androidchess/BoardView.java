package com.qwezey.androidchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import board.Coordinate;

public class BoardView extends View {

    Paint whitePaint;
    Paint blackPaint;

    public BoardView(Context context) {

        super(context);
        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);
        blackPaint = new Paint();
        blackPaint.setColor(Color.GRAY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("(" + event.getX() + ", " + event.getY() + ")");
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = canvas.getWidth();
        int height = canvas.getHeight();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rect rect = getRect(new Coordinate(i, j), width, height);
                boolean isEven = (i+j) % 2 == 1;
                canvas.drawRect(rect, isEven ? whitePaint : blackPaint);
            }
        }
    }

    private Rect getRect(Coordinate c, int containerWidth, int containerHeight) {

        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int left = c.getX() * rectWidth;
        int top = c.getY() * rectHeight;
        int right = left + rectWidth;
        int bottom = top + rectHeight;

        return new Rect(left, top, right, bottom);
    }
}
