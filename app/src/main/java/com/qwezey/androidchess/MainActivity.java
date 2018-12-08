package com.qwezey.androidchess;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.logic.chess.BadInputException;
import com.qwezey.androidchess.logic.game.Game;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            Game game = new Game();

            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
            );
            setContentView(R.layout.activity_main);

            setContentView(new BoardView(this, game.getBoard()));

        } catch (BadInputException e) {

        }
    }
}
