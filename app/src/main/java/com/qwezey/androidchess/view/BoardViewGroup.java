package com.qwezey.androidchess.view;

import android.content.Context;
import android.view.DragEvent;
import android.view.ViewGroup;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.board.Square;

/**
 * @author Ammaar Muhammad Iqbal
 */
public class BoardViewGroup extends ViewGroup {

    AppStateViewModel appState;

    /**
     * @param context  The context for the view
     * @param appState The state of the app
     */
    public BoardViewGroup(Context context, AppStateViewModel appState) {
        super(context);
        this.appState = appState;

        // Create square views for each coordinate
        BoardView.forEachCoordinate(c -> {
            SquareView squareView = new SquareView(context, appState.getSquareViewState(c));
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
                        else thisView.setColor(SquareView.Color.INVALID_SELECTION);
                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        setGuideColors(origin);
                        break;

                    case DragEvent.ACTION_DROP:
                        setOriginalColors();
                        if (origin.canMovePiece(thisView))
                            origin.movePiece(thisView);
                        else return false;
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        origin.showPiece();
                        setOriginalColors();
                        break;
                }

                return true;
            });

            addView(squareView, getChildIndex(c));
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        BoardView.forEachCoordinate(c -> {

            int rectWidth = getWidth() / 8;
            int rectHeight = getHeight() / 8;
            int l = c.getX() * rectWidth;
            int t = (7 - c.getY()) * rectHeight;
            int r = l + rectWidth;
            int b = t + rectHeight;

            getChildAt(getChildIndex(c)).layout(l, t, r, b);
        });
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
            if (selection.canMovePiece(child)) child.setColor(SquareView.Color.CAN_MOVE);
            else child.setColor(SquareView.Color.ORIGINAL);
        });
    }

    /**
     * @param c The coordinate to get the index
     * @return The index of the child at c
     */
    private int getChildIndex(Coordinate c) {
        return c.getX() * 8 + c.getY();
    }
}
