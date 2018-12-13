package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModel;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.game.Game;
import com.qwezey.androidchess.logic.game.Player;
import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.view.SquareViewState;

import java.util.function.Consumer;

/**
 * State for the BoardView
 */
public class AppStateViewModel extends ViewModel {

    private SquareViewState[][] grid;
    private Game game;

    /**
     * @param c The coordinate where the square is
     * @return The square that is at c
     */
    public SquareViewState getSquareViewState(Coordinate c) {
        return getGrid()[c.getX()][c.getY()];
    }

    /**
     * @return The current Player
     */
    public Player getCurrentPlayer() {
        return getGame().getCurrentPlayer();
    }

    public Game.Result madeMove(Coordinate from, Coordinate to) {
        return getGame().madeMove(from, to);
    }

    /**
     * @return True if player can undo last move, false otherwise
     */
    public boolean canUndoLastMove() {
        return getGame().canUndoLastMove();
    }

    /**
     * Undoes last move of other player
     *
     * @return True if successful, false otherwise
     */
    public boolean undoLastMove() {
        return getGame().undoLastMove();
    }

    /**
     * Add listener that will be proved with move results on each move
     *
     * @param listener The listener
     */
    public void addCurrentPlayerListener(Consumer<Player> listener) {
        getGame().addCurrentPlayerListener(listener);
    }

    /**
     * @return The square state grid
     */
    private SquareViewState[][] getGrid() {
        if (grid == null) initializeGrid();
        return grid;
    }

    /**
     * Initializes the grid
     */
    private void initializeGrid() {
        grid = new SquareViewState[8][8];
        BoardView.forEachCoordinate(c ->
                grid[c.getX()][c.getY()] = new SquareViewState(getGame().getBoard().getSquare(c))
        );
    }

    /**
     * @return The game
     */
    private Game getGame() {
        try {
            if (game == null) game = new Game();
        } catch (BadInputException e) {
        }
        return game;
    }
}
