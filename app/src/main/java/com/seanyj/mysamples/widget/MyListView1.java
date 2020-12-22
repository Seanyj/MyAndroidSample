package com.seanyj.mysamples.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.seanyj.mysamples.customview.SlideViewTest1Activity;

public class MyListView1 extends ListView{
    private SlideView1 mFocusedItemView;

    public MyListView1(Context context) {
        super(context);
    }

    public MyListView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                int position = pointToPosition(x, y);
                if (position != INVALID_POSITION) {
                    SlideViewTest1Activity.MessageItem data = (SlideViewTest1Activity.MessageItem) getItemAtPosition(position);
                    mFocusedItemView = data.slideView;
                }
                break;
            }
        }

        if (mFocusedItemView != null) {
            mFocusedItemView.onRequireTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (mFocusedItemView != null && mFocusedItemView.isScrolled()) {
                ev.setAction(MotionEvent.ACTION_CANCEL);
            }
        }

        return super.onTouchEvent(ev);
    }

}
