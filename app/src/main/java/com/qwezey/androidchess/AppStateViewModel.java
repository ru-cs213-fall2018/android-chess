package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModel;
import android.graphics.Color;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.game.Game;
import com.qwezey.androidchess.view.Square;

/**
 * State for the BoardView
 */
public class AppStateViewModel extends ViewModel {

    private Square[][] grid;
    private Game game;

    /**
     * @param c The coordinate where the square is
     * @return The square that is at c
     */
    public Square getSquare(Coordinate c) {
        if (grid == null) initializeGrid();
        return grid[c.getX()][c.getY()];
    }

    /**
     * Initializes the grid
     */
    private void initializeGrid() {
        grid = new Square[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Coordinate c = new Coordinate(i, j);
                int color = getGame().getBoard().getSquare(c).getColor() ==
                        com.qwezey.androidchess.logic.chess.Color.White ?
                        Color.WHITE : Color.GRAY;
                grid[i][j] = new Square(c, color);
            }
        }
    }

    /**
     * @return The game
     */
    public Game getGame() {
        try {
            if (game == null) game = new Game();
        } catch (BadInputException e) {
        }
        return game;
    }
}
