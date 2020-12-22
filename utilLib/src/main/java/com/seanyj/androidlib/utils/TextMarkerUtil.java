package com.seanyj.androidlib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

import com.seanyj.androidlib.R;

public class TextMarkerUtil
{
	private static float baseline;
	private static float fontHeiht;
	private static int intrinsicHeight;
	private static int intrinsicWidth;
	private static NinePatchDrawable ninePatchDrawable;
	private static Rect paddingRect;
	private static Paint paint;

	public static float getFontHeight(Paint paramPaint)
	{
		Paint.FontMetrics localFontMetrics = paramPaint.getFontMetrics();
		return (float) Math.ceil(localFontMetrics.descent
				- localFontMetrics.ascent);
	}

	/**
	 * used for add baidu map marker
	 * @param context
	 * @param text
	 * @return
	 */
	public static Bitmap getTextMarker(Context context, String text)
	{
		if ((paint == null) || (ninePatchDrawable == null)) {
			paint = new Paint();
			paint.setColor(0xFFFFFFFF);
			paint.setTextSize(SizeUtil.dip2px(context, 13.0F));
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setAntiAlias(true);
			fontHeiht = getFontHeight(paint);
			ninePatchDrawable = (NinePatchDrawable) context.getResources()
					.getDrawable(R.drawable.bg_map_marker);
			paddingRect = new Rect();
			ninePatchDrawable.getPadding(paddingRect);
			intrinsicHeight = ninePatchDrawable.getIntrinsicHeight();
			intrinsicWidth = ninePatchDrawable.getIntrinsicWidth();
			baseline = -paint.getFontMetrics().ascent + paddingRect.top;
		}

		float width = paint.measureText(text) + paddingRect.left
				+ paddingRect.right;
		if (width < intrinsicWidth)
			width = intrinsicWidth;
		float height = fontHeiht + paddingRect.top + paddingRect.bottom;
		if (height < intrinsicHeight)
			height = intrinsicHeight;
		Bitmap localBitmap = Bitmap.createBitmap((int) width, (int) height,
				Bitmap.Config.ARGB_4444);
		Canvas localCanvas = new Canvas(localBitmap);
		ninePatchDrawable.setBounds(0, 0, (int) width, (int) height);
		ninePatchDrawable.draw(localCanvas);
		localCanvas.drawText(text, width / 2.0F, baseline, paint);

		return localBitmap;
	}
}
