package com.qwezey.androidchess.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.ViewGroup;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.chess.Color;
import com.qwezey.androidchess.logic.game.Game;

import java.util.function.Consumer;

/**
 * @author Ammaar Muhammad Iqbal
 */
public class BoardView extends ViewGroup {

    AppStateViewModel appState;
    AppCompatActivity activity;
    PieceViewProvider pieceViewProvider;

    /**
     * @param context The context for the view
     */
    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (this.isInEditMode()) {
            this.setWillNotDraw(false);
            return;
        }

        activity = (AppCompatActivity) context;
        appState = ViewModelProviders.of(activity).get(AppStateViewModel.class);
        pieceViewProvider = new PieceViewProvider(context);

        // Create square views for each coordinate
        BoardView.forEachCoordinate(c -> {

            SquareView squareView = new SquareView(context, appState, pieceViewProvider, c);
            squareView.setOnDragListener((view, dragEvent) -> {

                SquareView origin = (SquareView) dragEvent.getLocalState();
                SquareView thisView = (SquareView) view;

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        origin.hidePiece();
                        setGuideColors(origin);
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (origin.canMovePiece(thisView))
                            thisView.setColor(SquareView.Color.VALID_SELECTION);
                        else if (origin != thisView)
                            thisView.setColor(SquareView.Color.INVALID_SELECTION);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        setGuideColors(origin);
                        break;

                    case DragEvent.ACTION_DROP:
                        setOriginalColors();
                        if (origin.movePiece(thisView)) {
                            Game.Result moveResult = appState
                                    .madeMove(origin.getCoordinate(), thisView.getCoordinate());
                            handleMadeMove(moveResult);
                        } else return false;
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        origin.showPiece();
                        thisView.showPiece();
                        setOriginalColors();
                        break;
                }

                return true;
            });

            addView(squareView, getChildIndex(c));
        });

        appState.addCurrentPlayerListener(player -> {
            invalidate();
        });
    }

    @Override
    public void invalidate() {
        super.invalidate();
        BoardView.forEachCoordinate(c -> {
            SquareView squareView = (SquareView) getChildAt(getChildIndex(c));
            squareView.invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.isInEditMode()) {
            Paint paint = new Paint();
            paint.setColor(android.graphics.Color.YELLOW);
            canvas.drawPaint(paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        if (this.isInEditMode()) return;

        TransitionManager.beginDelayedTransition(this);

        BoardView.forEachCoordinate(c -> {

            int rectWidth = getWidth() / 8;
            int rectHeight = getHeight() / 8;
            int l = (
                    appState.getCurrentPlayer().getColor() == Color.White ?
                            c.getX() :
                            (7 - c.getX())
            ) * rectWidth;
            int t = (
                    appState.getCurrentPlayer().getColor() == Color.White ?
                            (7 - c.getY()) :
                            c.getY()
            ) * rectHeight;
            int r = l + rectWidth;
            int b = t + rectHeight;

            getChildAt(getChildIndex(c)).layout(l, t, r, b);
        });
    }

    private void handleMadeMove(Game.Result moveResult) {
        switch (moveResult) {
            case CONTINUE:
                break;
            case PROMOTION:
                break;
            case DRAW:
                break;
            case CHECK_MATE:
                break;
        }
    }

    /**
     * Sets all squares to the original color
     */
    private void setOriginalColors() {
        BoardView.forEachCoordinate(c -> {
            SquareView child = (SquareView) getChildAt(getChildIndex(c));
            child.setColor(SquareView.Color.ORIGINAL);
        });
    }

    /**
     * Highlights the squares the piece on selection can goto
     *
     * @param selection The selected square
     */
    private void setGuideColors(SquareView selection) {
        BoardView.forEachCoordinate(c -> {

            SquareView child = (SquareView) getChildAt(getChildIndex(c));

            if (selection.canMovePiece(child))
                child.setColor(SquareView.Color.CAN_MOVE);
            else if (selection == child)
                child.setColor(SquareView.Color.VALID_SELECTION);
            else
                child.setColor(SquareView.Color.ORIGINAL);
        });
    }

    /**
     * @param c The coordinate to get the index
     * @return The index of the child at c
     */
    private int getChildIndex(Coordinate c) {
        return c.getX() * 8 + c.getY();
    }

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
