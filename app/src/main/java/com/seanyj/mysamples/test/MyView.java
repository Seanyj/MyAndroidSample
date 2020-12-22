package com.seanyj.mysamples.test;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.seanyj.mysamples.R;

public class MyView extends View {
    private LinearLayout mLinearLayout;
    private Context mContext;
    private String[] mAttrs;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setupValues(attrs, R.attr.testStyle, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        setupValues(attrs, defStyleAttr, defStyleRes);
    }

    private void setupValues(AttributeSet set, int defStyleAttr, int defStyleRes) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

