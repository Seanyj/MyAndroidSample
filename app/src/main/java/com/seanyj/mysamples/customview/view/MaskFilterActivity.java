package com.seanyj.mysamples.customview.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.seanyj.mysamples.R;

/**
 * Created by Administrator on 2018/3/13.
 */

public class MaskFilterActivity extends AppCompatActivity {

    private MenuItem mNormal;
    private MenuItem mSolid;
    private MenuItem mInner;
    private MenuItem mOuter;
    private MenuItem mFromBack;
    private MenuItem mFromFront;
    private MenuItem mShadow;
    private LinearLayout mRoot;
    private MyView mMyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mask_filter);
        mRoot = (LinearLayout) findViewById(R.id.root);
        mMyView = new MyView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = 24;
        layoutParams.topMargin = 24;
        layoutParams.rightMargin = 24;
        layoutParams.bottomMargin = 24;
        mRoot.addView(mMyView, layoutParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu blurMenu = menu.addSubMenu("blur mask");
        blurMenu.setHeaderTitle("Blur Mask");
        mNormal = blurMenu.add("normal");
        mSolid = blurMenu.add("solid");
        mInner = blurMenu.add("inner");
        mOuter = blurMenu.add("outer");
        SubMenu embossMenu = menu.addSubMenu("emboss mask");
        mFromBack = embossMenu.add("from back");
        mFromFront = embossMenu.add("from front");
        mShadow = menu.add("Shadow Layer");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MaskFilter maskFilter = null;
        final int radius = 10;

        if (item == mNormal) {
            maskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
        } else if (item == mSolid) {
            maskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.SOLID);
        } else if (item == mInner) {
            maskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.INNER);
        } else if (item == mOuter) {
            maskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.OUTER);
        } else if (item == mFromBack) {
            maskFilter = new EmbossMaskFilter(new float[]{1, 1, -1}, 0.1f, 20, 20);
        } else if (item == mFromFront) {
            maskFilter = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.1f, 20, 20);
        } else if (item == mShadow){
            mMyView.applyShadow();
            return true;
        }

        mMyView.setMaskFilter(maskFilter);

        return true;
    }

    private class MyView extends View {
        private Paint mPaint;

        public MyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.GREEN);
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawColor(Color.WHITE);
            canvas.drawRect(100, 100, 500, 500, mPaint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public void setMaskFilter(MaskFilter filter) {
            mPaint.clearShadowLayer();
            mPaint.setMaskFilter(filter);
            invalidate();
        }

        public void applyShadow() {
            mPaint.setMaskFilter(null);
            mPaint.setShadowLayer(20, 10, 10, Color.RED);
            invalidate();
        }
    }

}
