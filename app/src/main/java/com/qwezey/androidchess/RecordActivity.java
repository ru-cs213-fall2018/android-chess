package com.qwezey.androidchess;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.qwezey.androidchess.logic.board.Coordinate;
import com.qwezey.androidchess.logic.piece.PieceConstructor;
import com.qwezey.androidchess.view.BoardView;
import com.qwezey.androidchess.view.SquareView;

import java.util.List;

public class RecordActivity extends AppCompatActivity {

    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        String recordName = getIntent().getStringExtra("name");
        Storage storage = new Storage(this);
        GameRecord gameRecord = storage.getGameRecord(recordName);
        List<GameRecord.Move> moves = gameRecord.getMoves();
        i = 0;

        AppStateViewModel appState = ViewModelProviders.of(this).get(AppStateViewModel.class);
        BoardView boardView = findViewById(R.id.recordView);

        Button previousButton = findViewById(R.id.previousButton);
        Button nextButton = findViewById(R.id.nextButton);

        previousButton.setEnabled(false);

        previousButton.setOnClickListener(view -> {
            GameRecord.Move move = moves.get(i-1);
            appState.goBack(move.getTo());
            i--;
            nextButton.setEnabled(true);
            if (i < 1) previousButton.setEnabled(false);
        });

        nextButton.setOnClickListener(view -> {
            GameRecord.Move move = moves.get(i);
            Coordinate from = move.getFrom();
            Coordinate to = move.getTo();
            PieceConstructor pc = move.getPieceConstructor();
            SquareView fs = (SquareView) boardView.getChildAt(boardView.getChildIndex(from));
            SquareView ts = (SquareView) boardView.getChildAt(boardView.getChildIndex(to));

            fs.movePiece(ts);
            appState.madeMove(from, to);
            if (pc!= null) appState.promoteLastDestination(pc, boardView.getPieceViewProvider(), ts);
            i++;
            previousButton.setEnabled(true);
            if (i > moves.size() - 1) nextButton.setEnabled(false);
        });
    }
}
