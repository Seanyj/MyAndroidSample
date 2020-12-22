package com.seanyj.mysamples.widget;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.seanyj.mysamples.R;

public class SlideView1 extends LinearLayout {
    private static final String TAG = "SlideView";
    private static final int TAN = 2;

    private LinearLayout mViewContent;
    private RelativeLayout mHolder;
    private Scroller mScroller;
    private OnSlideListener mOnSlideListener;
    private int mHolderWidth = 120;
    private int mLastX = 0;
    private int mLastY = 0;
    private Context mContext;
    private boolean mScrolled;

    public SlideView1(Context context) {
        super(context);
        initView();
    }

    public SlideView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("hello", "width: " + getMeasuredWidth() + " : height: " + getMeasuredHeight());
    }

    private void initView() {
        mContext = getContext();
        mScroller = new Scroller(mContext);
        setOrientation(LinearLayout.HORIZONTAL);
        inflate(mContext, R.layout.view_slide_view_1, this);
        mViewContent = findViewById(R.id.view_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mHolderWidth,
                getResources().getDisplayMetrics()));
    }

    public void setButtonText(CharSequence text) {
        ((TextView) findViewById(R.id.delete)).setText(text);
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        mOnSlideListener = onSlideListener;
    }

    public void shrink() {
        if (getScaleX() != 0) {
            smoothScrollTo(0, 0);
        }
    }

    public void onRequireTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mScrolled = false;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, OnSlideListener.SLIDE_STATUS_SCROLL);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }

                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mHolderWidth) {
                        newScrollX = mHolderWidth;
                    }
                    scrollTo(newScrollX, 0);
                    mScrolled = true;
                    Log.e("hello", "scrollTo: " + newScrollX);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                int newScrollX = 0;
                if (scrollX - mHolderWidth * 0.75 > 0) {
                    newScrollX = mHolderWidth;
                }
                smoothScrollTo(newScrollX, 0);
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(this, newScrollX == 0 ? OnSlideListener.SLIDE_STATUS_OFF : OnSlideListener.SLIDE_STATUS_ON);
                }
                break;
            }
            default:
                break;
        }

        mLastX = x;
        mLastY = y;
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public boolean isScrolled() {
        return mScrolled;
    }

    public void setScrolled(boolean scrolled) {
        mScrolled = scrolled;
    }

    public interface OnSlideListener {
        int SLIDE_STATUS_OFF = 0;
        int SLIDE_STATUS_SCROLL = 1;
        int SLIDE_STATUS_ON = 2;

        void onSlide(View view, int status);
    }
}
