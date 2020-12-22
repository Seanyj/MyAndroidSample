package com.seanyj.mysamples.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.seanyj.mysamples.R;

/**
 * Created by Administrator on 2017/9/14.
 */

public class MyView extends View {
    private Paint mPaint;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setColor(Color.CYAN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float[] arr = new float[]{
                0.393f, 0.769f, 0.189f, 0, 0,
                0.349f, 0.686f, 0.168f, 0, 0,
                0.272f, 0.534f, 0.131f, 0, 0,
                0, 0, 0, 1, 0
        };
        mPaint.setColorFilter(new ColorMatrixColorFilter(arr));

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery2);

        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        RectF rectF = new RectF(0, 0, 100, 100);
        canvas.drawArc(rectF, -90, 135, false, mPaint);
        mPaint.setTextSize(30);
        canvas.drawPosText("hello", new float[]{10, 10, 40, 40, 70, 70, 100, 100, 130, 130}, mPaint);
    }
}
