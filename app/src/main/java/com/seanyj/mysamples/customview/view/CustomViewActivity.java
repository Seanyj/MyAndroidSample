package com.seanyj.mysamples.customview.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.DensityUtil;

public class CustomViewActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewGroup mContentLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview);

        mContentLayout = (ViewGroup) findViewById(R.id.contentLayout);

        findViewById(R.id.chatViewButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.chatViewButton:
                showView(makeMarkView());
                break;
        }
    }

    private void showView(View v) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(v);
    }

    private View makeMarkView() {
        String textToShow = "hello world";
        NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) getResources().getDrawable(R.drawable.bg_mark);
        Rect paddingRect = new Rect();
        ninePatchDrawable.getPadding(paddingRect);
        int intrinsicWidth = ninePatchDrawable.getIntrinsicWidth();
        int intrinsicHeight = ninePatchDrawable.getIntrinsicHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(DensityUtil.dp2px(this, 14));
        int baseline = (int) (paddingRect.top - paint.getFontMetrics().ascent);
        int textMeasureWidth = (int) paint.measureText(textToShow);
        int textMeasureHeight = (int) (paint.getFontMetrics().descent - paint.getFontMetrics().ascent);

        int width = paddingRect.left + paddingRect.right + textMeasureWidth;
        int height = paddingRect.top + paddingRect.bottom + textMeasureHeight;
        width = width > intrinsicWidth ? width : intrinsicWidth;
        height = height > intrinsicHeight ? height : intrinsicHeight;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        ninePatchDrawable.setBounds(0, 0, width, height);
        ninePatchDrawable.draw(canvas);
        paint.setColor(Color.RED);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(textToShow, width / 2, baseline, paint);

        ImageView iv = new ImageView(this);
        iv.setImageBitmap(bitmap);

        return iv;
    }

}
