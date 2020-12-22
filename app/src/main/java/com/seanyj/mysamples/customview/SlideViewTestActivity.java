package com.seanyj.mysamples.customview;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.widget.MyListView;
import com.seanyj.mysamples.widget.SlideView;

import java.util.ArrayList;
import java.util.List;

public class SlideViewTestActivity extends AppCompatActivity {
    private MyListView mListView;
    private List<String> mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_view_test);
        mListView = findViewById(R.id.listView);
        mData = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mData.add("hello " + i);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getBaseContext(), "click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mData == null ? 0 : mData.size();
            }

            @Override
            public Object getItem(int position) {
                return mData.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new SlideView(getBaseContext());
                    convertView.setBackgroundColor(Color.YELLOW);
                    TextView textView = new TextView(getBaseContext());
                    textView.setTextSize(20);
                    textView.setPadding(0, 40, 0, 40);
                    textView.setTextColor(Color.CYAN);
                    textView.setBackgroundColor(Color.LTGRAY);
                    SlideView slideView = (SlideView) convertView;
                    slideView.setContentView(textView);
                    TextView leftView1 = new TextView(getBaseContext());
                    leftView1.setTextSize(20);
                    leftView1.setText("delete");
                    slideView.addLeftView(leftView1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getBaseContext(), "clicked", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                SlideView slideView = (SlideView) convertView;
                ((TextView) slideView.getContentView()).setText((String) getItem(position));

                return convertView;
            }
        });

    }
}
