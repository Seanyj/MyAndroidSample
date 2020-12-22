package com.seanyj.mysamples.media.camera;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.seanyj.mysamples.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.descTextView)
    TextView mDescTextView;
    @BindView(R.id.imageButton)
    ImageButton mImageButton;

    public final static int DISPLAYWIDTH = 200;
    public final static int DISPLAYHEIGHT = 200;
    private Cursor mCursor;
    private int mFileColumn;
    private int mDisplayColumn;
    private int mTitleColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        mImageButton.setOnClickListener(this);
        init();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton) {
            showNextPic();
        }
    }

    private void init() {
        String columns[] = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID,
                MediaStore.Images.Media.TITLE, MediaStore.Images.Media.DESCRIPTION};
        mCursor = managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);

        if (mCursor.moveToFirst()) {
            mFileColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            mTitleColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            try {
                mDisplayColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            } catch (IllegalArgumentException e) {
                mDisplayColumn = mTitleColumn;
                e.printStackTrace();
            }
            mDescTextView.setText(mCursor.getString(mDisplayColumn));
            String path = mCursor.getString(mFileColumn);
            Bitmap bitmap = getBitmap(path);
            mImageButton.setImageBitmap(bitmap);
        }
    }

    private Bitmap getBitmap(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        int heightRation = (int) Math.ceil(options.outHeight / (float)DISPLAYHEIGHT);
        int widthRatio = (int) Math.ceil(options.outWidth / (float)DISPLAYWIDTH);
        if (heightRation > 1 && widthRatio > 1) {
            if (heightRation > widthRatio) {
                options.inSampleSize = heightRation;
            } else {
                options.inSampleSize = widthRatio;
            }
        }

        options.inJustDecodeBounds = false;
        bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    private void showNextPic() {
        if (mCursor.moveToNext()) {
            mDescTextView.setText(mCursor.getString(mDisplayColumn));
            String path = mCursor.getString(mFileColumn);
            Bitmap bitmap = getBitmap(path);
            mImageButton.setImageBitmap(bitmap);
        }
    }
}
