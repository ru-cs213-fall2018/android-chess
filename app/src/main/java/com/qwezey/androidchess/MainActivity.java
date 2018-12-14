package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.view.SquareView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add content
        setContentView(R.layout.activity_main);

        // Handle player swap
        AppStateViewModel appState = ViewModelProviders.of(this).get(AppStateViewModel.class);
        appState.addCurrentPlayerListener(player -> {

            // Update info text
            TextView textView = findViewById(R.id.infoText);
            textView.setText(player + "'s Turn");

            // Disable undo
            Button undoButton = findViewById(R.id.undoButton);
            undoButton.setEnabled(appState.canUndoLastMove());
        });
    }

    public void onUndoButtonClick(View view) {
        AppStateViewModel appState = ViewModelProviders.of(this).get(AppStateViewModel.class);
        BoardView boardView = findViewById(R.id.boardView);
        TransitionManager.beginDelayedTransition(boardView);
        appState.undoLastMove();
    }

    public void onDrawButtonClick(View view) {
        BoardView boardView = findViewById(R.id.boardView);
        boardView.showEndGameDialog(true);
    }

    public void onResignButtonClick(View view) {
        BoardView boardView = findViewById(R.id.boardView);
        boardView.showEndGameDialog(false);
    }

    public void onAutoButtonClick(View view) {
        AppStateViewModel appState = ViewModelProviders.of(this).get(AppStateViewModel.class);
        BoardView boardView = findViewById(R.id.boardView);
        while (true) {
            SquareView from = (SquareView) boardView.getChildAt(boardView.getChildIndex(randomCoordinate()));
            SquareView to = (SquareView) boardView.getChildAt(boardView.getChildIndex(randomCoordinate()));
            if (from.hasPlayerPiece() && from.movePiece(to)) {
                appState.madeMove(from.getCoordinate(), to.getCoordinate());
                break;
            }
        }
    }

    public Coordinate randomCoordinate() {
        int x = new Random().nextInt(8);
        int y = new Random().nextInt(8);
        return new Coordinate(x, y);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Recordings");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.toString()) {
            case "Recordings":
                Intent intent = new Intent(this, RecordingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
