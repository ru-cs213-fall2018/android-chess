package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
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

/**
 * @author Ammaar Muhammad Iqbal
 */
public class SquareView extends ViewGroup {

    private Square square;
    private ImageView pieceView;

    /**
     * @param context The context of the view
     * @param square  Corresponding square
     */
    public SquareView(Context context, Square square) {
        super(context);
        this.square = square;
        setWillNotDraw(false);

        // Add image view
        pieceView = new ImageView(context);
        pieceView.setImageDrawable(getPieceDrawable(square.getSquare().getPiece()));
        pieceView.setOnLongClickListener(view -> {
            view.startDragAndDrop(null, new BiggerDragShadowBuilder(view), this, 0);
            return true;
        });
        addView(pieceView, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (getChildCount() > 0) getChildAt(0).layout(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(square.getPaint());
    }

    /**
     * Hides the piece on this square
     */
    public void hidePiece() {
        pieceView.setVisibility(INVISIBLE);
    }

    /**
     * Shows the piece in this square
     */
    public void showPiece() {
        pieceView.setVisibility(VISIBLE);
    }

    /**
     * @return True if this has a piece on it, false otherwise
     */
    public boolean hasPiece() {
        return square.getSquare().hasPiece();
    }

    /**
     * Checks if you can move the piece on this to to
     *
     * @param to The square view to move the piece to
     * @return true if it can move, false otherwise
     */
    public boolean canMovePiece(SquareView to) {
        com.qwezey.androidchess.logic.board.Square fromSquare = square.getSquare();
        com.qwezey.androidchess.logic.board.Square toSquare = to.square.getSquare();
        return fromSquare.hasPiece() && fromSquare.getPiece().canMove(toSquare) == null;
    }

    /**
     * Moves the piece on this square to to
     *
     * @param to The square to move the piece to
     * @return true if moved, false otherwise
     */
    public boolean movePiece(SquareView to) {
        if (!canMovePiece(to)) return false;
        com.qwezey.androidchess.logic.board.Square fromSquare = square.getSquare();
        com.qwezey.androidchess.logic.board.Square toSquare = to.square.getSquare();
        fromSquare.getPiece().move(toSquare);
        to.pieceView.setImageDrawable(pieceView.getDrawable());
        pieceView.setImageDrawable(null);
        return true;
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

        return VectorDrawableCompat.create(getResources(), resId, null);
    }

    /**
     * Drag shadow that's 2x as big as the view
     */
    private class BiggerDragShadowBuilder extends View.DragShadowBuilder {

        public BiggerDragShadowBuilder(View view) {
            super(view);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            int width = getView().getWidth() * 2;
            int height = getView().getHeight() * 2;
            outShadowSize.set(width, height);
            outShadowTouchPoint.set(width / 2, height / 2);
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.scale(2, 2);
            getView().draw(canvas);
        }
    }
}
