package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.MotionEvent;
import android.view.View;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.R;
import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.game.Player;
import com.qwezey.androidchess.logic.piece.Bishop;
import com.qwezey.androidchess.logic.piece.King;
import com.qwezey.androidchess.logic.piece.Knight;
import com.qwezey.androidchess.logic.piece.Piece;
import com.qwezey.androidchess.logic.piece.Queen;
import com.qwezey.androidchess.logic.piece.Rook;

import java.util.function.Consumer;

public class BoardView extends View {

    private AppStateViewModel appState;

    public BoardView(Context context, AppStateViewModel appState) {
        super(context);
        this.appState = appState;
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//            Player player = appState.getGame().getCurrentPlayer();
//            Coordinate c = getCoordinate(event.getX(), event.getY(), getWidth(), getHeight());
//            SquareViewState selectedSquare = appState.getSquareViewState(c);
//            Piece piece = appState.getGame().getBoard().getSquare(c).getPiece();
//            boolean isValid = piece != null && player.getColor() == piece.getColor();
//
//            BoardView.forEachCoordinate(o -> {
//                com.qwezey.androidchess.logic.board.Square s = appState.getGame().getBoard().getSquare(o);
//                SquareViewState displayedSquare = appState.getSquareViewState(o);
//                if (isValid && piece.canMove(s) == null)
//                    displayedSquare.setCanMoveColor();
//                else displayedSquare.setOriginalColor();
//            });
//
//
//            if (isValid) selectedSquare.setValidSelectionColor();
//            else selectedSquare.setInvalidSelectionColor();
//
//            invalidate();
//            return true;
//        }
//
//        return false;
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        BoardView.forEachCoordinate(c -> {
            SquareViewState s = appState.getSquareViewState(c);
//            s.draw(canvas, getWidth(), getHeight(), getPiece(c));
        });
    }

    private Coordinate getCoordinate(float x, float y, int containerWidth, int containerHeight) {
        int rectWidth = containerWidth / 8;
        int rectHeight = containerHeight / 8;
        int newX = (int) x / rectWidth;
        int newY = 7 - ((int) y / rectHeight);
        return new Coordinate(newX, newY);
    }

//    /**
//     * @param c The coordinate of the piece to get
//     * @return The vector image of the piece
//     */
//    private VectorDrawableCompat getPiece(Coordinate c) {
//
////        Piece piece = appState.getGame().getBoard().getSquare(c).getPiece();
////        if (piece == null) return null;
//        boolean isWhite = piece.getColor() == com.qwezey.androidchess.logic.chess.Color.White;
//
//        int resId;
//
//        if (piece instanceof King)
//            resId = isWhite ? R.drawable.white_king : R.drawable.black_king;
//        else if (piece instanceof Queen)
//            resId = isWhite ? R.drawable.white_queen : R.drawable.black_queen;
//        else if (piece instanceof Rook)
//            resId = isWhite ? R.drawable.white_rook : R.drawable.black_rook;
//        else if (piece instanceof Bishop)
//            resId = isWhite ? R.drawable.white_bishop : R.drawable.black_bishop;
//        else if (piece instanceof Knight)
//            resId = isWhite ? R.drawable.white_knight : R.drawable.black_knight;
//        else
//            resId = isWhite ? R.drawable.white_pawn : R.drawable.black_pawn;
//
//        return VectorDrawableCompat.create(getResources(), resId, null);
//    }

    /**
     * Loops through each coordinate on the board
     *
     * @param coordinateConsumer Gives every single coordinate
     */
    public static void forEachCoordinate(Consumer<Coordinate> coordinateConsumer) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                coordinateConsumer.accept(new Coordinate(i, j));
            }
        }
    }
}
