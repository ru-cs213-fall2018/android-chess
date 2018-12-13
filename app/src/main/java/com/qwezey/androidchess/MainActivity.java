package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set display
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        getSupportActionBar().hide();

        // Add content
        setContentView(R.layout.activity_main);

        // Handle button events
        AppStateViewModel appState = ViewModelProviders.of(this).get(AppStateViewModel.class);
        Button undoButton = findViewById(R.id.undoButton);
        undoButton.setOnClickListener(button -> {
            appState.undoLastMove();
        });

        // Handle player swap
        appState.addCurrentPlayerListener(player -> {

            // Update info text
            TextView textView = findViewById(R.id.infoText);
            textView.setText(player + "'s Turn");

            // Disable undo
            undoButton.setEnabled(appState.canUndoLastMove());
        });
    }
}
