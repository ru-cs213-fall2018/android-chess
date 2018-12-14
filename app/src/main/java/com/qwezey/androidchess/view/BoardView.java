package com.qwezey.androidchess.view;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.qwezey.androidchess.AppStateViewModel;
import com.qwezey.androidchess.GameRecord;
import com.qwezey.androidchess.MainActivity;
import com.qwezey.androidchess.R;
import com.qwezey.androidchess.Storage;
import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.chess.Color;
import com.qwezey.androidchess.logic.game.Game;
import com.qwezey.androidchess.logic.game.Player;

import java.util.List;
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
                            Coordinate from = origin.getCoordinate();
                            Coordinate to = thisView.getCoordinate();
                            Game.Result moveResult = appState.madeMove(from, to);
                            handleMadeMove(moveResult, from, to);
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
            int squareWidth = getWidth() / 8;
            int squareHeight = getHeight() / 8;
            for (int i = 0; i < 9; i++) {
                canvas.drawLine(i * squareWidth, 0, i * squareWidth, getHeight(), new Paint());
            }
            for (int i = 0; i < 9; i++) {
                canvas.drawLine(0, i * squareHeight, getWidth(), i * squareHeight, new Paint());
            }
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

    private void handleMadeMove(Game.Result moveResult, Coordinate from, Coordinate to) {
        switch (moveResult) {

            case CONTINUE:
                appState.recordMove(new GameRecord.Move(from, to));
                break;

            case PROMOTION:

                break;

            case DRAW:
                appState.recordMove(new GameRecord.Move(from, to));
                showEndGameDialog(true);
                break;

            case CHECK_MATE:
                appState.recordMove(new GameRecord.Move(from, to));
                showEndGameDialog(false);
                break;
        }
    }

    public void showEndGameDialog(boolean isDraw) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (isDraw) builder.setTitle("Draw");
        else builder.setTitle(appState.getOtherPlayer() + " Wins!");

        LayoutInflater inflater = activity.getLayoutInflater();
        View saveGameView = inflater.inflate(R.layout.dialog_save_game, null);

        builder.setView(saveGameView);

        builder.setNegativeButton("NEW GAME", (dialogInterface, i) -> {
        });

        EditText editText = saveGameView.findViewById(R.id.saveGameTextInput);

        Storage storage = new Storage(activity);

        builder.setPositiveButton("SAVE RECORDING", (dialogInterface, i) -> {
            String text = editText.getText().toString().trim();
            storage.recordGame(text, appState.getRecord());
        });

        builder.setOnDismissListener(dialogInterface -> {
            appState.resetGame();
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        button.setEnabled(false);

        List<String> gameNames = storage.getAllRecordNames();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence == null ? null : charSequence.toString().trim();
                if (text == null || text.equals("") || gameNames.contains(text)) {
                    button.setEnabled(false);
                } else button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) { }
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
