package com.seanyj.mysamples.graphic.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.seanyj.androidlib.widgets.graphic.BlurringView;
import com.seanyj.mysamples.R;

public class BlurImageActivity extends AppCompatActivity{
    private ImageView mImageView;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur_image);
        mImageView = (ImageView) findViewById(R.id.originalImageView);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBar.setMax(23);

        final BlurringView blurringView = (BlurringView) findViewById(R.id.blurring_view);
        final View blurredView = findViewById(R.id.originalImageView);
        blurringView.setBlurredView(blurredView);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int radius = seekBar.getProgress() + 1;
                Log.e("hello", "radius: " + radius);
                blurringView.setBlurRadius(radius);
                blurringView.invalidate();

//                Bitmap bitmap= createBlurredImage(radius + 1);
//                Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery1);
//                Bitmap bitmap = blurBitmap(originalBitmap, radius, getBaseContext());
//                Bitmap bitmap = fastblur(originalBitmap, radius);
//                mImageView.setImageBitmap(bitmap);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private Bitmap createBlurredImage(int radius)
    {
        // Load a clean bitmap to work from.
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scenery1);

        // Create another bitmap that will hold the results of the filter.
        Bitmap blurredBitmap = Bitmap.createBitmap(originalBitmap);

        RenderScript renderScript = RenderScript.create(this);
        // Load up an instance of the specific script that we want to use.
        // An Element is similar to a C type. The second parameter, Element.U8_4,
        // tells the Allocation is made up of 4 fields of 8 unsigned bits.
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        // Create an Allocation for the kernel inputs.
        Allocation input = Allocation.createFromBitmap(renderScript, originalBitmap,
                Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);

        // Assign the input Allocation to the script.
        script.setInput(input);

        // Set the blur radius
        script.setRadius(radius);

        // Finally we need to create an output allocation to hold the output of the Renderscript.
        Allocation output = Allocation.createTyped(renderScript, input.getType());

        // Next, run the script. This will run the script over each Element in the Allocation, and copy it's
        // output to the allocation we just created for this purpose.
        script.forEach(output);

        // Copy the output to the blurred bitmap
        output.copyTo(blurredBitmap);

        // Cleanup.
        output.destroy();
        input.destroy();
        script.destroy();

        return blurredBitmap;
    }

    /**
     * 通过调用系统高斯模糊api的方法模糊
     *
     * @param bitmap  source bitmap
     * @param radius  0 < radius <= 25
     * @param context context
     * @return out bitmap
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context) {
        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        android.renderscript.RenderScript rs = android.renderscript.RenderScript.create(context);

        //Create an Intrinsic Blur Script using the Renderscript
        android.renderscript.ScriptIntrinsicBlur blurScript = android.renderscript.ScriptIntrinsicBlur
                .create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        android.renderscript.Allocation allIn = android.renderscript.Allocation.createFromBitmap(rs, bitmap);
        android.renderscript.Allocation allOut = android.renderscript.Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(radius);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        //        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
//        view.setBackground(new BitmapDrawable(context.getResources(), outBitmap));
        rs.destroy();

        return outBitmap;
    }

    /**
     *@param
     *@描述  快速模糊化处理bitmap
     *@作者  tll
     *@时间  2016/12/5 19:22
     */
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int temp = 256 * divsum;
        int dv[] = new int[temp];
        for (i = 0; i < temp; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return (bitmap);
    }
}
