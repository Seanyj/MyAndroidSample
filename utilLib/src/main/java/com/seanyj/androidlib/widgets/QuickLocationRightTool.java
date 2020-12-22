package com.seanyj.androidlib.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * �Ҳ���ĸ�����ٶ�λ
 */
public class QuickLocationRightTool extends View
{
	private String[] mLetters = null;
	private OnTouchingLetterChangedListener mOnTouchingLetterChangedListener;
	private int mChoose = -1;
	private Paint mPaint = new Paint();
	private boolean mShowBkg = false;

	public QuickLocationRightTool(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public QuickLocationRightTool(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public QuickLocationRightTool(Context context)
	{
		super(context);
	}

	public void setLetters(String[] letters)
	{
		this.mLetters = letters;
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);

		if (mShowBkg) {
			canvas.drawColor(Color.parseColor("#10000000"));
		}

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / mLetters.length;

		for (int i = 0; i < mLetters.length; i++) {
			mPaint.setColor(Color.BLACK);
			mPaint.setTypeface(Typeface.DEFAULT_BOLD);
			mPaint.setAntiAlias(true);
			if (i == mChoose) {
				mPaint.setColor(Color.parseColor("#3399ff"));
				mPaint.setFakeBoldText(true);
			}
			float xPos = width / 2 - mPaint.measureText(mLetters[i]) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(mLetters[i], xPos, yPos, mPaint);
			mPaint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = mChoose;
		final OnTouchingLetterChangedListener listener = mOnTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * mLetters.length); // ��ĸλ��

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mShowBkg = true;
			if (oldChoose != c && listener != null) {
				if (c > 0 && c <= mLetters.length) { // �����һ����ĸ��#����Ч����Ļ���������Ϊc>0
					listener.onTouchingLetterChanged(mLetters[c]);
					mChoose = c; // �����ظ�
					invalidate();
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (oldChoose != c && listener != null) {
				if (c > 0 && c <= mLetters.length) { // �����һ����ĸ��#����Ч����Ļ���������Ϊc>0
					listener.onTouchingLetterChanged(mLetters[c]);
					mChoose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			mShowBkg = false;
			mChoose = -1;
			invalidate();
			break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener)
	{
		this.mOnTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener
	{
		public void onTouchingLetterChanged(String s);
	}

}
