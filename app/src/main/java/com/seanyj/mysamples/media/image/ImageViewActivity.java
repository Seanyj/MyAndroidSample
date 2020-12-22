package com.seanyj.mysamples.media.image;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;


public class ImageViewActivity extends AppCompatActivity{
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageView = new ImageView(this);
        mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(mImageView);

        loadImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, 1, 1, "print size");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            printSize();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void printSize() {
        int w = (mImageView.getDrawable()).getIntrinsicWidth();
        int h = (mImageView.getDrawable()).getIntrinsicHeight();
        Log.e("hello", "w: " + w + " h: " + h);
    }

    private void loadImage() {
        String url = "http://lensbuyersguide.com/gallery/219/2/23_iso100_14mm.jpg";
        String url1 = "http://e.hiphotos.baidu.com/image/pic/item/72f082025aafa40fafb5fbc1a664034f78f019be.jpg";
//        Glide.with(this).load(url)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        e.printStackTrace();
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        Log.e("hello", "onResourceReady: " + resource.getIntrinsicWidth() + " : " + resource.getIntrinsicHeight());
//                        return false;
//                    }
//                })
//                .into(mImageView);
//        Glide.with(this).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                Log.e("hello", "onResourceReady: " + resource.getByteCount()/ 1024. / 1024 + "M");
//                mImageView.setImageBitmap(resource);
//            }
//
//            @Override
//            public void onLoadStarted(Drawable placeholder) {
//                super.onLoadStarted(placeholder);
//                Log.e("hello", "onLoadStarted");
//            }
//
//            @Override
//            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                super.onLoadFailed(e, errorDrawable);
//                Log.e("hello", "onLoadFailed");
//                e.printStackTrace();
//            }
//        });

        Glide.with(this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Log.e("hello", "onResourceReady: " + resource.getByteCount()/ 1024. / 1024 + "M");
                mImageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadStarted(@Nullable Drawable placeholder) {
                super.onLoadStarted(placeholder);
                Log.e("hello", "onLoadStarted");
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Log.e("hello", "onLoadFailed");
            }
        });

    }
}
