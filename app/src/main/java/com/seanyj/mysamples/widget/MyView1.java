package com.seanyj.mysamples.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyView1 extends ViewGroup{
    public static final int PADDING = 30;
    private TextView mTextView;

    public MyView1(Context context) {
        super(context);
        init();
    }

    public MyView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(Color.CYAN);
        mTextView = new TextView(getContext());
        mTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 120));
        mTextView.setText("hello world");
        mTextView.setTextSize(28);
        mTextView.setBackgroundColor(Color.MAGENTA);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, 200);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mTextView.layout(0, 0, getWidth() - PADDING , getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(PADDING, 0);
        mTextView.draw(canvas);
        canvas.restore();
    }
}
