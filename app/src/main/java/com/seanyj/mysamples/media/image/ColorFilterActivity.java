package com.seanyj.mysamples.media.image;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.seanyj.mysamples.R;

public class ColorFilterActivity extends AppCompatActivity {
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_filter);

        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_color_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_colorMatrix:
                ColorMatrix matrix = new ColorMatrix(new float[]{
                        -1, 0, 0, 1, 1,
                        0, -1, 0, 1, 1,
                        0, 0, -1, 1, 1,
                        0, 0, 0, 1, 0
                });
                mImageView.setColorFilter(new ColorMatrixColorFilter(matrix));
                break;
            case R.id.action_lighting:
                mImageView.setColorFilter(new LightingColorFilter(0xFF00FFFF, 0));
                break;
            case R.id.action_color_porterDuff:
                mImageView.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));
                break;
            case R.id.action_paint_bitmap:
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
                paint.setColorFilter(new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN));
                BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.scenery3).mutate();
                Bitmap srcBitmap = drawable.getBitmap();

                Bitmap bitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(srcBitmap, 0, 0, paint);
                mImageView.setImageBitmap(bitmap);
                break;
        }
        return true;
    }
}
