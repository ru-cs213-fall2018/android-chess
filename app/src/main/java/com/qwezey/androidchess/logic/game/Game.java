package com.qwezey.androidchess.logic.game;

import com.qwezey.androidchess.logic.board.Board;
import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.board.Square;
import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.chess.Color;
import com.qwezey.androidchess.logic.piece.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Represents a game
 *
 * @author Ammaar Muhammad Iqbal
 */
public class Game {

    private Board board;
    private Player currentPlayer;
    private Player otherPlayer;
    private Coordinate lastDestination;
    private List<Consumer<Player>> currentPlayerListeners;

    public enum Result {
        CHECK_MATE,
        DRAW,
        PROMOTION,
        CONTINUE
    }

    /**
     * Create a new game
     *
     * @throws BadInputException If can't find player kings
     */
    public Game() throws BadInputException {
        board = new Board();
        currentPlayer = new Player(Color.White, (King) this.board.getSquare('e', 1).getPiece());
        otherPlayer = new Player(Color.Black, (King) this.board.getSquare('e', 8).getPiece());
        currentPlayerListeners = new ArrayList<>();
    }

    /**
     * Swaps players
     *
     * @param from The coordinate a piece has been moved from
     * @param to   The coordinate the piece moved to
     * @return CHECK_MATE if the other player is now in check mate,
     * DRAW if the other player is now in stale mate,
     * PROMOTION if the player moved a pawn to the end,
     * CONTINUE if nothing special happened and it's the next player's turn
     */
    public Result madeMove(Coordinate from, Coordinate to) {

        // Defining variables
        Square fromSquare = this.board.getSquare(from);
        Square toSquare = this.board.getSquare(to);
        boolean isFromPawn = fromSquare.getPiece() instanceof Pawn;
        boolean isWhite = this.currentPlayer.getColor() == Color.White;
        int toY = toSquare.getCoordinate().getY();
        boolean isToEnd = isWhite ? toY == 7 : toY == 0;
        boolean isFromPawnToEnd = isFromPawn && isToEnd;
        Result result = Result.CONTINUE;

        // Promote pawns at end
        if (isFromPawnToEnd) return Result.PROMOTION;

        // Current player wins if other player is in checkmate
        if (this.otherPlayer.getKing().isInCheckMate()) result = Result.CHECK_MATE;

        // Game is draw if other player is in stalemate
        if (this.otherPlayer.getKing().isInStaleMate()) result = Result.DRAW;

        // Let other player know if s/he is in check
        if (this.otherPlayer.getKing().isInCheck())
            System.out.println(("\n" + this.otherPlayer + " is in check"));

        lastDestination = to;
        swapPlayers();

        return result;
    }

    /**
     * @return True if player can undo last move, false otherwise
     */
    public boolean canUndoLastMove() {
        System.out.println(lastDestination == null);
        if (lastDestination == null) return false;
        Square square = board.getSquare(lastDestination);
        if (!square.hasPiece()) return false;
        return true;
    }

    /**
     * Undoes the last move
     *
     * @return True if successful, false otherwise
     */
    public boolean undoLastMove() {
        if (!canUndoLastMove()) return false;
        Piece piece = board.getSquare(lastDestination).getPiece();
        piece.goBack();
        swapPlayers();
        return true;
    }

    /**
     * @return The board associated with this game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return The current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return The other player
     */
    public Player getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * Swap players
     */
    private void swapPlayers() {

        // Swap players
        Player temp = this.currentPlayer;
        this.currentPlayer = this.otherPlayer;
        this.otherPlayer = temp;

        // Let all listeners know
        for (Consumer<Player> listener : currentPlayerListeners)
            listener.accept(currentPlayer);
    }

    /**
     * Add a listener that will be provided the current player
     *
     * @param listener The listener
     */
    public void addCurrentPlayerListener(Consumer<Player> listener) {
        listener.accept(currentPlayer);
        currentPlayerListeners.add(listener);
    }
}
