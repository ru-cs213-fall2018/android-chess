package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModel;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.game.Game;
import com.qwezey.androidchess.logic.game.Player;
import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.view.SquareViewState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * State for the BoardView
 */
public class AppStateViewModel extends ViewModel {

    private SquareViewState[][] grid;
    private Game game;
    private List<Coordinate> recording;

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

    /**
     * @return The other player
     */
    public Player getOtherPlayer() {
        return getGame().getOtherPlayer();
    }

    /**
     * Call when move is made to update the game
     *
     * @param from The start origin of the move
     * @param to   The destination of the move
     * @return The result of the move
     * CHECK_MATE if the other player is now in check mate,
     * DRAW if the other player is now in stale mate,
     * PROMOTION if the player moved a pawn to the end,
     * CONTINUE if nothing special happened and it's the next player's turn
     */
    public Game.Result madeMove(Coordinate from, Coordinate to) {
        getRecording().add(from);
        getRecording().add(to);
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
     * @return The recording, a list of even numbered items that make pairs.
     * In each pair, the first coordinate is the from and the second coordinate is the to
     */
    public List<Coordinate> getRecording() {
        if (recording == null) recording = new ArrayList<>();
        return recording;
    }

    /**
     * Resets the game
     */
    public void resetGame() {
        this.grid = null;
        this.game = null;
        this.recording = null;
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
