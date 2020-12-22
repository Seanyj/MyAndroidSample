package com.seanyj.mysamples.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyViewGroup extends ViewGroup {
    public MyViewGroup(Context context) {
        this(context, null);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desireWidth = 0;
        int desireHeight = 0;

        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                MarginLayoutParams layoutParams = (MarginLayoutParams) v.getLayoutParams();
                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);
                int width = v.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                desireWidth = desireWidth > width ? desireWidth : width;
                desireHeight += v.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
            }

            desireWidth += getPaddingLeft() + getPaddingRight();
            desireHeight += getPaddingTop() + getPaddingBottom();
            desireWidth = Math.max(desireWidth, getSuggestedMinimumWidth());
            desireHeight = Math.max(desireHeight, getSuggestedMinimumHeight());
        }

        setMeasuredDimension(resolveSize(desireWidth, widthMeasureSpec), resolveSize(desireHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        int heightUsed = paddingTop;

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            int left = paddingLeft + lp.leftMargin;
            int top = heightUsed + lp.topMargin;
            int right = left + v.getMeasuredWidth();
            int bottom = heightUsed + v.getMeasuredHeight();
            heightUsed = bottom;
            v.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public static class MyLayoutParmas extends MarginLayoutParams {
        public MyLayoutParmas(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public MyLayoutParmas(int width, int height) {
            super(width, height);
        }

        public MyLayoutParmas(MarginLayoutParams source) {
            super(source);
        }

        public MyLayoutParmas(LayoutParams source) {
            super(source);
        }
    }
}
