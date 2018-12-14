package com.qwezey.androidchess;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public class RecordAdapter extends RecyclerView.Adapter<RecordViewHolder> {

        Context context;
        List<String> recordNames;

        public RecordAdapter(Context context) {
            Storage storage = new Storage(context);
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
            recordViewHolder.getTextView().setText(recordNames.get(i));
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
