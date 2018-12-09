package com.qwezey.androidchess.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class SquareView extends View {

    Square square;

    public SquareView(Context context, Square square) {
        super(context);
        this.square = square;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(square.getPaint());
    }
}
