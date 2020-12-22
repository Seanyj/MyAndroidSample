package com.seanyj.mysamples.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SlideView extends ViewGroup {
    private float mLastX;
    private float mLastY;
    private List<View> mLeftViews;
    private List<View> mRightView;
    private View mContentView;

    public SlideView(Context context) {
        this(context, null);
    }

    public SlideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SlideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mLeftViews = new ArrayList<>();
        mRightView = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int desireWidth = 0;
        int desireHeight = 0;

        for (int i = 0; i < getChildCount(); i++) {
            desireWidth += getChildAt(i).getMeasuredWidth();
            desireHeight = Math.max(getChildAt(i).getMeasuredHeight(), desireHeight);
        }
        desireWidth = Math.max(getSuggestedMinimumWidth(), desireWidth);
        desireHeight = Math.max(getSuggestedMinimumHeight(), desireHeight);

        int width = desireWidth;
        int height = getSize(desireHeight, heightMeasureSpec);

        Log.e("hello", "width: " + width);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int border = 0;
        if (mContentView != null) {
            mContentView.layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }

        if (mLeftViews != null) {
            for (int i = 0; i < mLeftViews.size(); i++) {
                View v = mLeftViews.get(i);
                v.layout(border - v.getMeasuredWidth(), 0, border, v.getMeasuredHeight());
                border -= v.getMeasuredWidth();
            }
        }

        border = 0;
        if (mRightView != null) {
            for (int i = 0; i < mRightView.size(); i++) {
                View v = mRightView.get(i);
                v.layout(border, 0, border + v.getMeasuredWidth(), v.getMeasuredHeight());
                border += v.getMeasuredWidth();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("hello", "onTouchEvent: " + event.getAction());
        float x = event.getRawX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - mLastX;
                Log.e("hello", "deltaX: " + deltaX);
                if (Math.abs(deltaX) > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                    Log.e("hello", "setTranslationX: " + deltaX);
//                    setTranslationX(deltaX);
                    scrollBy((int) -deltaX, 0);
                    mLastX = x;
                }
                break;
            case MotionEvent.ACTION_UP:
                setTranslationX(0);
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private int getSize(int desireSize, int spec) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                size = desireSize;
                break;
            case MeasureSpec.AT_MOST:
                size = Math.min(desireSize, size);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }

        return size;
    }

    public void addLeftView(View v, final OnClickListener onClickListener) {
        addView(v);
        mLeftViews.add(v);
        requestLayout();

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public void addRightView(View v, final OnClickListener onClickListener) {
        addView(v);
        mRightView.add(v);
        requestLayout();

        v.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
    }

    public void setContentView(View v) {
        addView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mContentView = v;
        requestLayout();
    }

    public View getContentView() {
        return mContentView;
    }
}
