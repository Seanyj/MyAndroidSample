package com.seanyj.mysamples.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.seanyj.mysamples.R;

public class GeometryActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Spinner mShapeSpinner;
    private Spinner mFeatureSpinner;

    private String[][] mFeatures;
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geometry);

        mImageView = (ImageView) findViewById(R.id.imageView);
        mShapeSpinner = (Spinner) findViewById(R.id.shapeSpinner);
        mFeatureSpinner = (Spinner) findViewById(R.id.featureSpinner);

        initData();
        initListeners();
    }

    private void initData() {
        String[] shapes = new String[]{
                "Arc",
                "Circle",
                "Rectangle",
                "Oval",
                "Lines",
                "Path",
                "Text"
        };

        mFeatures = new String[][]{
                {"stroke, useCenter true", "stoke, useCenter false", "fill, useCenter true", "fill, useCenter false"},
                {"stroke", "fill"},
                {"stroke", "fill", "round"},
                {"stroke", "fill"},
                {"normal"},
                {"normal"},
                {"normal", "path clockwise", "path anticlockwise"}
        };

        ChooseAdapter shapeAdapter = new ChooseAdapter(this, android.R.layout.simple_spinner_item, shapes);
        shapeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        mShapeSpinner.setAdapter(shapeAdapter);

        mBitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.YELLOW);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLUE);
    }

    private void initListeners() {
        mShapeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFeatureSpinner.setAdapter(new ChooseAdapter(GeometryActivity.this, android.R.layout.simple_spinner_item,
                        mFeatures[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mFeatureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                drawShape();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void drawShape() {
        int shape = mShapeSpinner.getSelectedItemPosition();
        int feature = mFeatureSpinner.getSelectedItemPosition();
        mCanvas.drawColor(Color.YELLOW);

        switch (shape) {
            case 0:
                drawArc(feature);
                break;
            case 1:
                drawCircle(feature);
                break;
            case 2:
                drawRectangle(feature);
                break;
            case 3:
                drawOval(feature);
                break;
            case 4:
                drawLines(feature);
                break;
            case 5:
                drawPaths(feature);
                break;
            case 6:
                drawText(feature);
                break;
        }
    }

    private void drawText(int feature) {
        String text = "hello world!yg";
        Path path = new Path();
        mPaint.setTextSize(30);
        if (feature == 0) {
            mCanvas.drawText(text, 0, text.length(), 0, 100, mPaint);
        } else if (feature == 1) {
            path.addCircle(150, 150, 100, Path.Direction.CW);
            mPaint.setStyle(Paint.Style.STROKE);
            mCanvas.drawPath(path, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mCanvas.drawTextOnPath(text, path, 0, 0, mPaint);
        } else {
            path.addCircle(150, 150, 100, Path.Direction.CCW);
            mPaint.setStyle(Paint.Style.STROKE);
            mCanvas.drawPath(path, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mCanvas.drawTextOnPath(text, path, 0, 0, mPaint);
        }
    }

    private void drawPaths(int feature) {
        Path path = new Path();
        path.moveTo(10, 0);
        path.lineTo(10, 100);
        path.quadTo(50, 150, 100, 100);
        path.cubicTo(50, 75, 150, 25, 100, 10);
        path.close();
        mCanvas.drawPath(path, mPaint);
    }

    private void drawLines(int feature) {
        final float[] lines = {
                0, 10, 100.2f, 10,
                0, 50, 300.5f, 50,
                0, 100, 200.4f, 100,
                50, 0, 50, 200.5f,
                100, 0, 100, 300.8f
        };
        mCanvas.drawLines(lines, 0, lines.length, mPaint);
    }

    private void drawOval(int feature) {
        if (feature == 0) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }
        RectF rectF = new RectF(0, 0, 300, 200);
        mCanvas.drawOval(rectF, mPaint);
    }

    private void drawRectangle(int feature) {
        if (feature == 2) {
            RectF rectF = new RectF(20, 20, 120, 120);
            mCanvas.drawRoundRect(rectF, 20, 20, mPaint);
            return;
        }

        if (feature == 0) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else if (feature == 1){
            mPaint.setStyle(Paint.Style.FILL);
        }
        mCanvas.drawRect(20, 20, 120, 120, mPaint);
    }

    private void drawCircle(int feature) {
        if (feature == 0) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }
        mCanvas.drawCircle(100, 100, 80, mPaint);
    }

    private void drawArc(int feature) {
        if (feature == 0 || feature == 1) {
            mPaint.setStyle(Paint.Style.STROKE);
        } else {
            mPaint.setStyle(Paint.Style.FILL);
        }
        boolean isUserCenter = feature == 0 || feature == 2;
        RectF rectF = new RectF(0, 0, 300, 300);
        mCanvas.drawArc(rectF, 15, 90, isUserCenter, mPaint);

        mImageView.setImageBitmap(mBitmap);
    }

    private class ChooseAdapter extends ArrayAdapter<String> {

        public ChooseAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull String[] objects) {
            super(context, resource, objects);
        }
    }
}
