package com.seanyj.mysamples.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.seanyj.mysamples.R;

public class SlideSwitch extends View {
    public static final int SWITCH_OFF = 0;
    public static final int SWITCH_ON = 1;
    public static final int SWITCH_SCROLLING = 2;

    private String mOnText = "打开";
    private String mOffText = "关闭";
    private int mSwitchStatus = SWITCH_OFF;
    private boolean mHasScrolled = false;
    private int mSrcX;
    private int mDstX;
    private int mBmpHeight = 0;
    private int mBmpWidth = 0;
    private int mThumbWidth = 0;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private OnSwitchChangedListener mOnSwitchChangedListener;
    Bitmap mSwitchOff, mSwitchOn, mSwitchThumb;

    public SlideSwitch(Context context) {
        this(context, null);
    }

    public SlideSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Resources res = getResources();
        mSwitchOff = BitmapFactory.decodeResource(res, R.drawable.bg_switch_off);
        mSwitchOn = BitmapFactory.decodeResource(res, R.drawable.bg_switch_on);
        mSwitchThumb = BitmapFactory.decodeResource(res, R.drawable.bg_switch_thumb);
        mBmpWidth = mSwitchOn.getWidth();
        mBmpHeight = mSwitchOn.getHeight();
        mThumbWidth = mSwitchThumb.getWidth();
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = mBmpWidth;
        params.height = mBmpHeight;
        super.setLayoutParams(params);
    }

    public void setOnSwitchChangedListener(OnSwitchChangedListener onSwitchChangedListener) {
        mOnSwitchChangedListener = onSwitchChangedListener;
    }

    public void setText(String onText, String offText) {
        mOnText = onText;
        mOffText = offText;
        invalidate();
    }

    public void setStatus(boolean on) {
        mSwitchStatus = on ? SWITCH_ON : SWITCH_OFF;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mSrcX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mDstX = Math.max((int) event.getX(), mThumbWidth / 2);
                mDstX = Math.min(mDstX, mBmpWidth - mThumbWidth / 2);
                if (mSrcX == mDstX) {
                    return true;
                }
                mHasScrolled = true;
                AnimationTransRunnable transRunnable = new AnimationTransRunnable(mSrcX, mDstX, 0);
                new Thread(transRunnable).start();
                mSrcX = mDstX;
                break;
            case MotionEvent.ACTION_UP:
                if (!mHasScrolled) {
                    mSwitchStatus = Math.abs(mSwitchStatus - 1);
                    int xFrom = 10, xTo = 62;
                    if (mSwitchStatus == SWITCH_OFF) {
                        xFrom = 62;
                        xTo = 10;
                    }
                    AnimationTransRunnable runnable = new AnimationTransRunnable(xFrom, xTo, 1);
                    new Thread(runnable).start();
                } else {
                    mSwitchStatus = mDstX > mThumbWidth / 2 ? SWITCH_ON : SWITCH_OFF;
                    invalidate();
                    mHasScrolled = false;
                }

                if (mOnSwitchChangedListener != null) {
                    mOnSwitchChangedListener.onSwitchChanged(this, mSwitchStatus);
                }
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(14);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);

        if (mSwitchStatus == SWITCH_OFF) {
            drawBitmap(canvas, null, null, mSwitchOff);
            drawBitmap(canvas, null, null, mSwitchThumb);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.translate(mSwitchThumb.getWidth(), 0);
            canvas.drawText(mOffText, 0, mBmpHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint);
        } else if (mSwitchStatus == SWITCH_ON) {
            drawBitmap(canvas, null, null, mSwitchOn);
            int count = canvas.save();
            canvas.translate(mSwitchOn.getWidth() - mSwitchThumb.getWidth(), 0);
            drawBitmap(canvas, null, null, mSwitchThumb);
            mPaint.setColor(Color.WHITE);
            canvas.restoreToCount(count);
            canvas.drawText(mOnText, 17, mBmpHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint);
        } else {
            mSwitchStatus = mDstX > mThumbWidth / 2 ? SWITCH_ON : SWITCH_OFF;
            drawBitmap(canvas, new Rect(0, 0, mDstX, mBmpHeight), new Rect(0, 0, (int) mDstX,
                    mBmpHeight), mSwitchOn);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(mOnText, 17, mBmpHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint);
            int count = canvas.save();
            canvas.translate(mDstX, 0);
            drawBitmap(canvas, new Rect(mDstX, 0, mBmpWidth, mBmpHeight), new Rect(0, 0,
                    mBmpWidth - mDstX, mBmpHeight), mSwitchOff);
            canvas.restoreToCount(count);
            count = canvas.save();
            canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight);
            canvas.translate(mThumbWidth, 0);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.drawText(mOffText, 0, mBmpHeight / 2 - (mPaint.descent() + mPaint.ascent()) / 2, mPaint);
            canvas.restoreToCount(count);
            count = canvas.save();
            canvas.translate(mDstX - mThumbWidth / 2, 0);
            drawBitmap(canvas, null, null, mSwitchThumb);
            canvas.restoreToCount(count);
        }
    }

    private void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap) {
        dst = dst == null ? new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()) : dst;
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    public class AnimationTransRunnable implements Runnable {
        private int srcX, dstX;
        private int duration;

        public AnimationTransRunnable(int srcX, int dstX, int duration) {
            this.srcX = srcX;
            this.dstX = dstX;
            this.duration = duration;
        }

        @Override
        public void run() {
            final int patch = dstX > srcX ? 5 : -5;
            if (duration == 0) {
                SlideSwitch.this.mSwitchStatus = SWITCH_SCROLLING;
                SlideSwitch.this.postInvalidate();
            } else {
                int x = srcX + patch;
                while (Math.abs(x - dstX) > 5) {
                    mDstX = x;
                    SlideSwitch.this.mSwitchStatus = SWITCH_SCROLLING;
                    SlideSwitch.this.postInvalidate();
                    x += patch;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                SlideSwitch.this.mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;
                SlideSwitch.this.postInvalidate();
            }
        }
    }

    public interface OnSwitchChangedListener {
        void onSwitchChanged(SlideSwitch slideSwitch, int status);
    }
}
