package com.qwezey.androidchess.view;

import android.content.Context;
import android.view.DragEvent;
import android.view.ViewGroup;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.logic.board.Coordinate;

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
            SquareView squareView = new SquareView(context, appState.getSquare(c));
            squareView.setOnDragListener((view, dragEvent) -> {

                SquareView origin = (SquareView) dragEvent.getLocalState();
                SquareView thisView = (SquareView) view;

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        origin.hidePiece();
                        break;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        origin.movePiece(thisView);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        origin.showPiece();
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
     * @param c The coordinate to get the index
     * @return The index of the child at c
     */
    private int getChildIndex(Coordinate c) {
        return c.getX() * 8 + c.getY();
    }
}
