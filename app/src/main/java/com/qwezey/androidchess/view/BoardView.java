package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

import com.qwezey.androidchess.logic.board.Board;
import com.qwezey.androidchess.logic.board.Coordinate;

public class BoardView extends View {

    private Board board;
    private GridViewModel grid;

    public BoardView(Context context, Board board, GridViewModel grid) {
        super(context);
        this.board = board;
        this.grid = grid;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Coordinate c = getCoordinate(event.getX(), event.getY(), getWidth(), getHeight());
            grid.getSquare(c, board).getPaint().setColor(Color.YELLOW);
            invalidate();
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Square s = grid.getSquare(new Coordinate(i, j), board);
                s.updateRect(canvas);
                s.draw(canvas);
            }
        }
    }

    private Coordinate getCoordinate(float x, float y, int containerWidth, int containerHeight) {
        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int newX = (int) x / rectWidth;
        int newY = 7 - ((int) y / rectHeight);
        return new Coordinate(newX, newY);
    }
}
