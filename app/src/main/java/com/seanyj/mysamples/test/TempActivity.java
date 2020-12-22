package com.seanyj.mysamples.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.seanyj.mysamples.widget.MyView1;

public class TempActivity extends AppCompatActivity {
    private ListView mListView;
    private EditText mEditText;
    private ImageView mImageView;
    private boolean mSet;
    private int mLevel;
    private TextView mTextView;
    private MyView mMyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyView1 myView1 = new MyView1(this);
        setContentView(myView1);
    }

}

