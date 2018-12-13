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
    Map<Piece, ImageView> pieces;

    public PieceViewProvider(Context context) {
        this.context = context;
        pieces = new HashMap<>();
    }

    public ImageView getPieceView(Piece piece) {
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
