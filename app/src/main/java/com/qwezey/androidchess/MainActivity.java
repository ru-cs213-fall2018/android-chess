package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.game.Game;
import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.view.GridViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        try {
            Game game = new Game();
            GridViewModel grid = ViewModelProviders.of(this).get(GridViewModel.class);
            setContentView(new BoardView(this, game.getBoard(), grid));

        } catch (BadInputException e) {

        }
    }
}
