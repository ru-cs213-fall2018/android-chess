package com.qwezey.androidchess;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.List;

public class RecordingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        RecordAdapter adapter = new RecordAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    public void onGameButtonPress(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {

        Context context;
        List<String> recordNames;
        Storage storage;

        public RecordAdapter(Context context) {
            storage = new Storage(context);
            this.context = context;
            recordNames = storage.getAllRecordNames();
        }

        @NonNull
        @Override
        public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new RecordViewHolder(new TextView(context));
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder recordViewHolder, int i) {
            String name = recordNames.get(i);
            LocalDateTime date = storage.getRecordTime(name);
            recordViewHolder.getTextView().setText(name + " " + date.toString());
        }

        @Override
        public int getItemCount() {
            return recordNames.size();
        }
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public RecordViewHolder(TextView view) {
            super(view);
            textView = view;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
