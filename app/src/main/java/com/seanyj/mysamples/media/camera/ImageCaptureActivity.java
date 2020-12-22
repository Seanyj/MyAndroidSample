package com.seanyj.mysamples.media.camera;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.seanyj.mysamples.R;

import java.io.File;
import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageCaptureActivity extends AppCompatActivity {
    @BindView(R.id.thumbnailImageView)
    ImageView mThumbnailImageView;
    @BindView(R.id.bigImageView)
    ImageView mBigImageView;
    @BindView(R.id.thumbnailBtn)
    Button mThumbnailBtn;
    @BindView(R.id.imageButton)
    Button mImageButton;
    @BindView(R.id.surfaceButton)
    Button mSurfaceButton;

    private String mImagePath;
    private Uri mImageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.thumbnailBtn, R.id.imageButton, R.id.surfaceButton})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumbnailBtn:
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, 1);
                break;
            case R.id.imageButton:
                boolean readPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                boolean writePermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                if (readPermission && writePermission) {
                    takePhoto();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                break;
            case R.id.surfaceButton:
                int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
                } else {
                    takePhotoUserSurace();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this, "you have denied to take photo", Toast.LENGTH_SHORT);
                }
                break;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhotoUserSurace();
                } else {
                    Toast.makeText(this, "you have denied to open camera", Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    private void takePhotoUserSurace() {
        startActivity(new Intent(this, SurfaceViewCameraActivity.class));
    }

    private void takePhoto() {
        takePicture1();
    }

    private void takePicture() {
        Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        mImagePath = dir + "/capture.jpg";
        File f = new File(mImagePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mImageUri = FileProvider.getUriForFile(this, "com.seanyj.mysamples.fileprovider", f);
        } else {
            mImageUri = Uri.fromFile(f);
        }
        // this will make onActivityResult(... Intent data) data null
        i1.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(i1, 2);
    }

    // output use MediaStore
    private void takePicture1() {
        Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues contentValues = new ContentValues();
        // could add some properties
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, "This is a test title");
//        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "This is a test description");
        mImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        i1.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(i1, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            mThumbnailImageView.setImageBitmap(bitmap);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(mImagePath, options);
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int dw = metrics.widthPixels;
            int dh = metrics.heightPixels;
            int widthRatio = (int) Math.ceil(options.outWidth / (float) dw);
            int heightRatio = (int) Math.ceil(options.outHeight / (float) dh);
            int ratio = 1;
            Log.e("hello", "screenWidth: " + dw + "   screenHeight: " + dh);
            Log.e("hello", "withRatio: " + widthRatio);
            Log.e("hello", "heightRatio: " + heightRatio);
            if (widthRatio > 1 && heightRatio > 1) {
                ratio = Math.max(widthRatio, heightRatio);
            }

            options.inJustDecodeBounds = false;
            options.inSampleSize = ratio;
//            Bitmap bitmap1 = BitmapFactory.decodeFile(mImagePath, options);
            Bitmap bitmap1 = null;
            try {
                bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mBigImageView.setImageBitmap(bitmap1);
            Log.e("hello", "width: " + bitmap1.getWidth());
            Log.e("hello", "height: " + bitmap1.getHeight());
        }
    }

}
