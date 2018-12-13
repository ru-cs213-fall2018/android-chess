package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.logic.board.Coordinate;

/**
 * @author Ammaar Muhammad Iqbal
 */
public class SquareView extends ViewGroup {

    private AppStateViewModel appState;
    private PieceViewProvider pieceViewProvider;
    private Coordinate coordinate;
    private SquareViewState state;
    private ImageView pieceView;
    private Color currentColor;
    private Paint borderPaint = new Paint(android.graphics.Color.BLACK);

    /**
     * The colors of squareViews
     */
    public enum Color {
        ORIGINAL,
        CAN_MOVE,
        VALID_SELECTION,
        INVALID_SELECTION
    }

    /**
     * @param context    The context of the view
     * @param coordinate Coordinate of this square
     */
    public SquareView(Context context, AppStateViewModel appState, PieceViewProvider pieceViewProvider, Coordinate coordinate) {
        super(context);
        this.appState = appState;
        this.pieceViewProvider = pieceViewProvider;
        this.coordinate = coordinate;
        this.state = appState.getSquareViewState(coordinate);
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        syncWithLogic();
        if (getChildCount() > 0) getChildAt(0).layout(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(state.getPaint());
        canvas.drawLine(0, 0, getWidth(), 0, borderPaint); // Top
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), borderPaint); // Bottom
        canvas.drawLine(0, 0, 0, getHeight(), borderPaint); // Left
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), borderPaint); // Right
        syncWithLogic();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
        if (!hasPlayerPiece()) return false;
        startDragAndDrop(null, new SquareView.BiggerDragShadowBuilder(pieceView), this, 0);
        return true;
    }

    /**
     * @return The coordinate of this square
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Hides the piece on this square
     */
    public void hidePiece() {
        if (pieceView != null)
            pieceView.setVisibility(INVISIBLE);
    }

    /**
     * Show the piece on this square
     */
    public void showPiece() {
        if (pieceView != null)
            pieceView.setVisibility(VISIBLE);
    }

    /**
     * @return True if this has a piece on it, false otherwise
     */
    public boolean hasPiece() {
        return state.hasPiece();
    }

    /**
     * @return True if this square has the current player's piece, false otherwise
     */
    public boolean hasPlayerPiece() {
        return state.hasPlayerPiece(appState.getCurrentPlayer());
    }

    /**
     * Checks if current player can move the piece on this to to
     *
     * @param to The square view to move the piece to
     * @return true if current player can move, false otherwise
     */
    public boolean canMovePiece(SquareView to) {
        return state.canMovePiece(appState.getCurrentPlayer(), to.state);
    }

    /**
     * Moves the piece on this square to to
     *
     * @param to The square to move the piece to
     * @return true if moved, false otherwise
     */
    public boolean movePiece(SquareView to) {
        if (!state.movePiece(appState.getCurrentPlayer(), to.state)) return false;
        return true;
    }

    /**
     * Updates this square's piece to match the square in logic
     */
    public void syncWithLogic() {
        if (state.hasPiece())
            pieceView = pieceViewProvider.putPieceViewOnSquareView(state.getPiece(), this);
        else {
            pieceView = null;
            removeAllViews();
        }
    }

    /**
     * Sets the color of this square if not already color
     *
     * @param color The color to change to
     */
    public void setColor(Color color) {
        if (color == currentColor) return;
        state.setColor(color);
        this.currentColor = color;
        invalidate();
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
