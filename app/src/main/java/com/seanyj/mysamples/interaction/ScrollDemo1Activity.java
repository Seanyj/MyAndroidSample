package com.seanyj.mysamples.interaction;

import android.opengl.EGLExt;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.seanyj.mysamples.R;

import java.util.ArrayList;
import java.util.List;

public class ScrollDemo1Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_demo1);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TheAdapter adapter = new TheAdapter();
        adapter.mList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            adapter.mList.add("item " + i);
        }
        mRecyclerView.setAdapter(adapter);
    }

    class TheAdapter extends RecyclerView.Adapter {
        private List<String> mList;

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TheViewHolder holder = new TheViewHolder(getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            String s = mList.get(position);
            ((TheViewHolder)holder).mTextView.setText(s);
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            Log.e("hello", "onViewAttachedToWindow: " + holder.getAdapterPosition());
        }
    }

    class TheViewHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public TheViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
