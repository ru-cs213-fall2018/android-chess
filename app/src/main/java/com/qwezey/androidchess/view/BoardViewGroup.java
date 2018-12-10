package com.qwezey.androidchess.view;

import android.content.Context;
import android.view.ViewGroup;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.logic.board.Coordinate;

public class BoardViewGroup extends ViewGroup {

    AppStateViewModel appState;

    public BoardViewGroup(Context context, AppStateViewModel appState) {
        super(context);
        this.appState = appState;

        BoardView.forEachCoordinate(c -> {
            addView(new SquareView(context, appState.getSquare(c)), getChildIndex(c));
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

    private int getChildIndex(Coordinate c) {
        return c.getX() * 8 + c.getY();
    }
}
