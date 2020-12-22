package com.seanyj.mysamples.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

public class MyListView extends ListView {
    public static final int MODE_HORIZONTAL = 1;
    public static final int MODE_VERTICAL = 2;

    private int mMode;
    private float mLastX;
    private float mLastY;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MyListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mMode = 0;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        float x = ev.getX();
        float y = ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (mMode == 0) {
                    mLastX = x;
                    mLastY = y;
                    return false;
                } else {
                    return true;
                }
            case MotionEvent.ACTION_MOVE:
                if (mMode == 0) {
                    float deltaX = x - mLastX;
                    float deltaY = y - mLastY;
                    Log.e("hello", "deltaX: " + deltaX);
                    Log.e("hello", "deltaY: " + deltaY);
                    float max = Math.max(Math.abs(deltaX), Math.abs(deltaY));
                    if (max > ViewConfiguration.get(getContext()).getScaledTouchSlop()) {
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            mMode = MODE_HORIZONTAL;
                        } else {
                            mMode = MODE_VERTICAL;
                            MotionEvent event = MotionEvent.obtain(ev);
                            event.setAction(MotionEvent.ACTION_DOWN);
                            dispatchTouchEvent(event);
                        }
                    }
                    Log.e("hello", "mode: " + mMode);
                }
                break;
            case MotionEvent.ACTION_UP:
                mMode = 0;
                break;
        }

        return mMode == MODE_VERTICAL;
    }
}
