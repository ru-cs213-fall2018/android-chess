package com.qwezey.androidchess;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.time.Instant;
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
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(),
                ((LinearLayoutManager) layoutManager).getOrientation());
        recyclerView.addItemDecoration(decoration);
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
            View view = LayoutInflater.from(context).inflate(R.layout.records_row, viewGroup, false);
            return new RecordViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordViewHolder recordViewHolder, int i) {
            String name = recordNames.get(i);
            LocalDateTime date = storage.getRecordTime(name);
            TextView t1 = recordViewHolder.getView().findViewById(R.id.rowNameButton);
            TextView t2 = recordViewHolder.getView().findViewById(R.id.rowDate);
            Button b = recordViewHolder.getView().findViewById(R.id.rowDeleteButton);
            t1.setText(name);
            t2.setText(date.toString());
            b.setOnClickListener(view -> {
                storage.removeRecord(name);
                recordNames = storage.getAllRecordNames();
                notifyDataSetChanged();
            });
            recordViewHolder.getView().setOnClickListener(view -> {
                Intent intent = new Intent(context, RecordActivity.class);
                intent.putExtra("name", name);
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return recordNames.size();
        }
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder {

        View view;

        public RecordViewHolder(View view) {
            super(view);
            this.view = view;
        }

        public View getView() {
            return view;
        }
    }
}
