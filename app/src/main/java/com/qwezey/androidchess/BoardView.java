package com.qwezey.androidchess;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.qwezey.androidchess.board.Board;
import com.qwezey.androidchess.board.Coordinate;

public class BoardView extends View {

    private Board board;
    private Square[][] grid;
    private Canvas canvas;

    public BoardView(Context context, Board board) {
        super(context);
        this.board = board;
        this.grid = new Square[8][8];
        initializeGrid();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Coordinate c = getCoordinate(event.getX(), event.getY(), canvas.getWidth(), canvas.getHeight());
            getSquare(c).getPaint().setColor(Color.YELLOW);
            invalidate();
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate c = new Coordinate(i, j);
                Square s = getSquare(c);
                s.updateRect();
                canvas.drawRect(s.getRect(), s.getPaint());
            }
        }
    }

    private void initializeGrid() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate c = new Coordinate(i, j);
                int color = board.getSquare(c).getColor() == com.qwezey.androidchess.chess.Color.White ? Color.WHITE : Color.GRAY;
                grid[i][j] = new Square(c, color);
            }
        }
    }

    private Square getSquare(Coordinate c) {
        return grid[c.getX()][c.getY()];
    }

    private Coordinate getCoordinate(float x, float y, int containerWidth, int containerHeight) {
        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int newX = (int) x / rectWidth;
        int newY = 7 - ((int) y / rectHeight);
        return new Coordinate(newX, newY);
    }

    private class Square {

        private Paint paint;
        private Rect rect;
        private Coordinate c;

        public Square(Coordinate c, int color) {
            rect = new Rect();
            paint = new Paint();
            paint.setColor(color);
            this.c = c;
        }

        public void updateRect() {

            int rectWidth = canvas.getWidth() / 8;
            int rectHeight = canvas.getHeight() / 8;
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
    }
}
