package com.seanyj.mysamples.media.camera;

import android.content.ContentValues;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.seanyj.mysamples.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SurfaceViewCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback,
        View.OnClickListener, Camera.PictureCallback {

    @BindView(R.id.surfaceView)
    SurfaceView mSurfaceView;

    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view_camera);
        ButterKnife.bind(this);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);

        mSurfaceView.setFocusable(true);
        mSurfaceView.setFocusableInTouchMode(true);
        mSurfaceView.setClickable(true);
        mSurfaceView.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
            if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
                parameters.set("orientation", "portrait");
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);

                List<String> colorEffects = parameters.getSupportedColorEffects();
                Iterator<String> cei = colorEffects.iterator();
                while (cei.hasNext()) {
                    String currentEffect  = cei.next();
                    if (currentEffect.equals(Camera.Parameters.EFFECT_SOLARIZE)) {
                        parameters.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
                        break;
                    }
                }
            } else {
                parameters.set("orientation", "landscape");
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            }

            mCamera.setParameters(parameters);
        } catch (IOException e) {
            e.printStackTrace();
            mCamera.release();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
    }

    @Override
    public void onClick(View v) {
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Uri imageFileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new ContentValues());
        try {
            OutputStream imageFileOS =  getContentResolver().openOutputStream(imageFileUri);
            imageFileOS.write(data);
            imageFileOS.flush();
            imageFileOS.close();
        } catch (FileNotFoundException e) {
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast t = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            t.show();
            e.printStackTrace();
        }
        mCamera.startPreview();
    }
}
