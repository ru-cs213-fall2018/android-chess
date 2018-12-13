package com.qwezey.androidchess.view;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.widget.ImageView;

import com.qwezey.androidchess.R;
import com.qwezey.androidchess.logic.piece.Bishop;
import com.qwezey.androidchess.logic.piece.King;
import com.qwezey.androidchess.logic.piece.Knight;
import com.qwezey.androidchess.logic.piece.Piece;
import com.qwezey.androidchess.logic.piece.Queen;
import com.qwezey.androidchess.logic.piece.Rook;

import java.util.HashMap;
import java.util.Map;

public class PieceViewProvider {

    Context context;
    Map<Piece, SquareView> squares;
    Map<Piece, ImageView> pieces;

    /**
     * @param context Context of the activity
     */
    public PieceViewProvider(Context context) {
        this.context = context;
        squares = new HashMap<>();
        pieces = new HashMap<>();
    }

    /**
     * Places piece on square and removes piece from another square it is on.
     * Sets the pieceView's visibility to visible
     *
     * @param piece  The corresponding piece to place
     * @param square The square to put piece on
     * @return The piece view corresponding with piece
     */
    public ImageView putPieceViewOnSquareView(Piece piece, SquareView square) {

        SquareView oldParent = squares.get(piece);
        ImageView pieceView = getPieceView(piece);
        if (square != oldParent) {
            if (oldParent != null) oldParent.removeAllViews();
            square.addView(pieceView);
            squares.put(piece, square);
        }

        return pieceView;
    }

    /**
     * @param piece The piece to get the associated view
     * @return The piece view associated with piece
     */
    private ImageView getPieceView(Piece piece) {
        if (pieces.containsKey(piece))
            return pieces.get(piece);
        else {
            ImageView pieceView = new ImageView(context);
            pieceView.setImageDrawable(getPieceDrawable(piece));
            pieces.put(piece, pieceView);
            return pieceView;
        }
    }

    /**
     * @param piece The corresponding piece
     * @return The drawable representation of piece
     */
    private VectorDrawableCompat getPieceDrawable(Piece piece) {

        if (piece == null) return null;
        boolean isWhite = piece.getColor() == com.qwezey.androidchess.logic.chess.Color.White;

        int resId;

        if (piece instanceof King)
            resId = isWhite ? R.drawable.white_king : R.drawable.black_king;
        else if (piece instanceof Queen)
            resId = isWhite ? R.drawable.white_queen : R.drawable.black_queen;
        else if (piece instanceof Rook)
            resId = isWhite ? R.drawable.white_rook : R.drawable.black_rook;
        else if (piece instanceof Bishop)
            resId = isWhite ? R.drawable.white_bishop : R.drawable.black_bishop;
        else if (piece instanceof Knight)
            resId = isWhite ? R.drawable.white_knight : R.drawable.black_knight;
        else
            resId = isWhite ? R.drawable.white_pawn : R.drawable.black_pawn;

        return VectorDrawableCompat.create(context.getResources(), resId, null);
    }
}
