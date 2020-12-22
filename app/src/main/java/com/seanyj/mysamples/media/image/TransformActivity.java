package com.seanyj.mysamples.media.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.seanyj.mysamples.R;

public class TransformActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private Bitmap mBitmap;
    private Paint mPaint;
    private Canvas mCanvas;
    private Bitmap mSrcBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transform);
        mImageView = findViewById(R.id.imageView);
        findViewById(R.id.matrixBtn).setOnClickListener(this);
        findViewById(R.id.rotateBtn).setOnClickListener(this);
        findViewById(R.id.scaleBtn).setOnClickListener(this);
        findViewById(R.id.translateBtn).setOnClickListener(this);
        findViewById(R.id.compBtn).setOnClickListener(this);
        findViewById(R.id.mirrorBtn).setOnClickListener(this);
        findViewById(R.id.createBtn).setOnClickListener(this);

        mPaint = new Paint();
        mSrcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pic_small_1);
        mBitmap = Bitmap.createBitmap(mSrcBitmap.getWidth(), mSrcBitmap.getHeight(), mSrcBitmap.getConfig());
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.matrixBtn:
                Matrix matrix = new Matrix();
                matrix.setValues(new float[] {
                        1, 0, 0,
                        0, 1, 0,
                        0, 0, 1
                });
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.rotateBtn:
                Matrix matrix1 = new Matrix();
                matrix1.setRotate(30, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix1, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.scaleBtn:
                Matrix matrix2 = new Matrix();
                matrix2.setScale(1.5f, 1);
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix2, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.translateBtn:
                Matrix matrix3 = new Matrix();
                matrix3.setTranslate(1.5f, -10);
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix3, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.compBtn:
                Matrix matrix4 = new Matrix();
                matrix4.setScale(1.5f, 1);
                matrix4.postRotate(15, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix4, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.mirrorBtn:
                Matrix matrix5 = new Matrix();
                matrix5.setScale(-1, 1);
                matrix5.postTranslate(mBitmap.getWidth(), 0);
                mCanvas.drawColor(Color.WHITE);
                mCanvas.drawBitmap(mSrcBitmap, matrix5, mPaint);
                mImageView.setImageBitmap(mBitmap);
                break;
            case R.id.createBtn:
                Matrix matrix6 = new Matrix();
                matrix6.setRotate(15, mBitmap.getWidth() / 2, mBitmap.getHeight() / 2);
                // bitmap's size be fixed by matrix
                Bitmap bitmap = Bitmap.createBitmap(mSrcBitmap, 0, 0, mSrcBitmap.getWidth(),
                        mSrcBitmap.getHeight(), matrix6, false);
                mImageView.setImageBitmap(bitmap);
                break;
        }
    }

}
