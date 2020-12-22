package com.seanyj.mysamples.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.seanyj.mysamples.R;

/**
 * Created by Administrator on 2018/3/14.
 */

public class ShaderActivity extends AppCompatActivity {

    private MyView mMyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMyView = new MyView(this);
        setContentView(mMyView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Matrix matrix = new Matrix();
        switch (item.getItemId()) {
            case R.id.action_bitmap_shader:
                mMyView.applyShader(item.getItemId(), null);
                return true;
            case R.id.action_linear_gradient:
                mMyView.applyShader(item.getItemId(), null);
                return true;
            case R.id.action_sweep_gradient:
                mMyView.applyShader(item.getItemId(), null);
                return true;
            case R.id.action_radial_gradient:
                mMyView.applyShader(item.getItemId(), null);
                return true;
            case R.id.action_matrix_translate:
                matrix = new Matrix();
                matrix.setTranslate(100, 100);
                mMyView.applyShader(0, matrix);
                return true;
            case R.id.action_matrix_rotate:
                matrix = new Matrix();
                matrix.setRotate(45, 100, 100);
                mMyView.applyShader(0, matrix);
                return true;
            case R.id.action_matrix_scale:
                matrix = new Matrix();
                matrix.setScale(0.6f, 1.4f, 0.5f, 0.5f);
                mMyView.applyShader(0, matrix);
                return true;
            case R.id.action_matrix_compose:
                matrix = new Matrix();
                matrix.setTranslate(100, 100);
                matrix.preRotate(45);
                matrix.postScale(0.6f, 1.4f, 0.5f, 0.5f);
                mMyView.applyShader(0, matrix);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyView extends View {
        private Paint mPaint;
        private Shader mShader;
        private Bitmap mBitmap;

        public MyView(Context context) {
            super(context);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_small_4);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint);
        }

        public void applyShader(int fromId, Matrix matrix) {
            if (fromId == R.id.action_bitmap_shader) {
                mShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            } else if (fromId == R.id.action_linear_gradient) {
                mShader = new LinearGradient(0, 0, getWidth() / 2, getHeight() / 2,
                        new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN},
                        new float[]{0, 0.25f, 0.5f, 0.75f, 1f,}, Shader.TileMode.REPEAT);
            } else if (fromId == R.id.action_sweep_gradient) {
                mShader = new SweepGradient(getWidth() / 2, getHeight() / 2,
                        new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN},
                        null);
            } else if (fromId == R.id.action_radial_gradient) {
                mShader = new RadialGradient(getWidth() / 2, getHeight() / 2, 200,
                        new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN},
                        null, Shader.TileMode.REPEAT);
            } else {
                if (mShader == null || matrix == null) {
                    return;
                }

                mShader.setLocalMatrix(matrix);
            }

            mPaint.setShader(mShader);
            invalidate();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
