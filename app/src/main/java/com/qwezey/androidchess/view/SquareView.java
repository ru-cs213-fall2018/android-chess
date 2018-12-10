package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qwezey.androidchess.R;
import com.qwezey.androidchess.logic.piece.Bishop;
import com.qwezey.androidchess.logic.piece.King;
import com.qwezey.androidchess.logic.piece.Knight;
import com.qwezey.androidchess.logic.piece.Piece;
import com.qwezey.androidchess.logic.piece.Queen;
import com.qwezey.androidchess.logic.piece.Rook;

public class SquareView extends ViewGroup {

    Square square;

    public SquareView(Context context, Square square) {
        super(context);
        this.square = square;
        setWillNotDraw(false);
        if (square.getSquare().hasPiece()) {
            ImageView imageView = new ImageView(context);
            imageView.setImageDrawable(getPiece(square.getSquare().getPiece()));
            imageView.setOnLongClickListener(view -> {
                view.startDragAndDrop(null, new View.DragShadowBuilder(view), null, 0);
                return true;
            });
            imageView.setOnDragListener((view, dragEvent) -> {

                return true;
            });
            addView(imageView, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() > 0) getChildAt(0).layout(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(square.getPaint());
    }

    private VectorDrawableCompat getPiece(Piece piece) {

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

        return VectorDrawableCompat.create(getResources(), resId, null);
    }
}
