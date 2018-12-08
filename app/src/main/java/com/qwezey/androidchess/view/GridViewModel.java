package com.qwezey.androidchess.view;

import android.arch.lifecycle.ViewModel;
import android.graphics.Color;

import com.qwezey.androidchess.logic.board.Board;
import com.qwezey.androidchess.logic.board.Coordinate;

/**
 * State for the BoardView
 */
public class GridViewModel extends ViewModel {

    Square[][] grid;

    /**
     * @param c The coordinate where the square is
     * @param board The board that corresponds with this grid
     * @return The square that is at c
     */
    public Square getSquare(Coordinate c, Board board) {
        if (grid == null) initializeGrid(board);
        return grid[c.getX()][c.getY()];
    }

    /**
     * Initializes the grid
     * @param board The board that corresponds with this grid
     */
    private void initializeGrid(Board board) {
        grid = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate c = new Coordinate(i, j);
                int color = board.getSquare(c).getColor() ==
                        com.qwezey.androidchess.logic.chess.Color.White ?
                        Color.WHITE : Color.GRAY;
                grid[i][j] = new Square(c, color);
            }
        }
    }
}
