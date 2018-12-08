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
                Square s = grid[i][j];
                s.updateRect(canvas);
                s.draw(canvas);
            }
        }
    }

    private void initializeGrid() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate c = new Coordinate(i, j);
                int color = board.getSquare(c).getColor() == com.qwezey.androidchess.logic.chess.Color.White ? Color.WHITE : Color.GRAY;
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
}
