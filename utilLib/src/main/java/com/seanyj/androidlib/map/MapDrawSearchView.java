package com.seanyj.androidlib.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MapDrawSearchView extends View
{
	private float b = 1.0F;
	private int dragColor = Color.argb(220, 51, 181, 229);
	private Paint dragPaint;
	private Path dragPath;
	private Point endPoint = null;
	private float lastX;
	private float lastY;
	private MapDrawSearchViewCallback mapDrawSearchViewCallback;
	private Bitmap pathBitmap;
	private Canvas pathCanvas;
	private RectF region;
	private Point startPoint = null;
	private float strokeWidth = 12.0F;

	public MapDrawSearchView(Context paramContext)
	{
		super(paramContext);
		init();
	}

	public MapDrawSearchView(Context paramContext,
			AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		init();
	}

	public MapDrawSearchView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt)
	{
		super(paramContext, paramAttributeSet, paramInt);
		init();
	}

	private void drawPathOnBitmap(int paramInt)
	{
		if (paramInt == OutLineStatus.OUTLINE_ERROR) {
			Toast.makeText(getContext(), "��ͼʧ�ܣ������³��ԣ�", 1).show();
			return;
		}

		switch (paramInt) {
		case OutLineStatus.OUTLINE_UNCLOSE:
			this.dragPaint.setStrokeWidth(80.0F * this.b);
			this.pathCanvas.drawPath(this.dragPath, this.dragPaint);
			break;
		case OutLineStatus.OUTLINE_POINT:
			this.dragPaint.setStrokeWidth(100.0F * this.b);
			this.pathCanvas.drawPath(this.dragPath, this.dragPaint);
			break;
		case OutLineStatus.OUTLINE_CLOSE:
			this.dragPath.close();
			this.dragPaint.setStyle(Paint.Style.FILL);
			this.pathCanvas.drawPath(this.dragPath, this.dragPaint);
		}
	}

	private int getDrawOutline()
	{
		if ((this.startPoint == null) || (this.endPoint == null))
			return OutLineStatus.OUTLINE_ERROR;
		this.dragPath.computeBounds(this.region, false);
		Point localPoint1 = new Point((int) this.region.left,
				(int) this.region.top);
		Point localPoint2 = new Point((int) this.region.right,
				(int) this.region.bottom);
		double d1 = Math.sqrt(Math.pow(localPoint2.y - localPoint1.y, 2.0D)
				+ Math.pow(localPoint2.x - localPoint1.x, 2.0D));
		double d2 = Math.sqrt(Math.pow(this.endPoint.y - this.startPoint.y,
				2.0D) + Math.pow(this.endPoint.x - this.startPoint.x, 2.0D));
		if (d1 == 0.0D)
			return OutLineStatus.OUTLINE_POINT;
		if (d2 * 2.0D <= d1)
			return OutLineStatus.OUTLINE_CLOSE;
		return OutLineStatus.OUTLINE_UNCLOSE;
	}

	private void init()
	{
		this.dragPath = new Path();
		this.dragPaint = new Paint();
		this.dragPaint.setColor(this.dragColor);
		this.dragPaint.setAntiAlias(true);
		this.dragPaint.setDither(true);
		this.dragPaint.setStyle(Paint.Style.STROKE);
		this.dragPaint.setStrokeJoin(Paint.Join.ROUND);
		this.dragPaint.setStrokeCap(Paint.Cap.ROUND);
		this.dragPaint.setStrokeWidth(this.strokeWidth);
		this.region = new RectF();
	}

	private void onDown(float x, float y)
	{
		b();
		this.dragPaint.setStyle(Paint.Style.STROKE);
		this.dragPaint.setStrokeWidth(this.strokeWidth);
		this.dragPath.reset();
		this.dragPath.moveTo(x, y);
		this.lastX = x;
		this.lastY = y;
		this.startPoint = new Point((int) this.lastX, (int) this.lastY);
	}

	private void onMove(float paramFloat1, float paramFloat2)
	{
		float f1 = Math.abs(paramFloat1 - this.lastX);
		float f2 = Math.abs(paramFloat2 - this.lastY);
		if ((f1 < 4.0F) && (f2 < 4.0F))
			return;
		this.dragPath.quadTo(this.lastX, this.lastY,
				(paramFloat1 + this.lastX) / 2.0F,
				(paramFloat2 + this.lastY) / 2.0F);
		this.lastX = paramFloat1;
		this.lastY = paramFloat2;
	}

	private void onUp()
	{
		this.dragPath.lineTo(this.lastX, this.lastY);
		this.endPoint = new Point((int) this.lastX, (int) this.lastY);
		drawPathOnBitmap(getDrawOutline());
		this.startPoint = null;
		this.endPoint = null;
		this.dragPaint.reset();
		if (this.mapDrawSearchViewCallback == null)
			return;
		this.mapDrawSearchViewCallback.onDrawEnd(this.dragPath);
	}

	private void test()
	{
	}

	public void a()
	{
	}

	public void b()
	{
		a();
		this.dragPaint.setColor(this.dragColor);
		if (this.pathBitmap != null) {
			this.pathBitmap.recycle();
			this.pathBitmap = null;
		}
		this.pathBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
				Bitmap.Config.ARGB_4444);
		this.pathCanvas = new Canvas(this.pathBitmap);
		invalidate();
	}

	public void clear()
	{
		this.dragPath.reset();
		invalidate();
	}

	public MapDrawSearchViewCallback getMapDrawSearchViewCallback()
	{
		return this.mapDrawSearchViewCallback;
	}

	public Bitmap getPathBitmap()
	{
		return this.pathBitmap;
	}

	public boolean isPointInRange(Point paramPoint)
	{
		return this.pathBitmap.getPixel(paramPoint.x, paramPoint.y) >> 24 != 0;
	}

	protected void onDraw(Canvas paramCanvas)
	{
		paramCanvas.drawColor(0);
		paramCanvas.drawPath(this.dragPath, this.dragPaint);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent)
	{
		float x = paramMotionEvent.getX();
		float y = paramMotionEvent.getY();
		switch (paramMotionEvent.getAction()) {
		case MotionEvent.ACTION_DOWN:
			onDown(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			onMove(x, y);
			break;
		case MotionEvent.ACTION_UP:
			onUp();
			break;
		}
		invalidate();
		return true;
	}

	public void setMapDrawSearchViewCallback(
			MapDrawSearchViewCallback paramMapDrawSearchViewCallback)
	{
		this.mapDrawSearchViewCallback = paramMapDrawSearchViewCallback;
	}

	public void setPathBitmap(Bitmap paramBitmap)
	{
		this.pathBitmap = paramBitmap;
	}

	public static abstract interface MapDrawSearchViewCallback
	{
		public abstract void onDrawEnd(Path paramPath);
	}
}
