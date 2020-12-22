package com.seanyj.mysamples.media.video;

import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.seanyj.mysamples.R;

public class VideoViewActivity extends AppCompatActivity {
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        mVideoView = findViewById(R.id.videoView);
        mVideoView.setMediaController(new MediaController(this));
        Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                + "/老友记第一季-01.mp4");
//        mVideoView.setVideoPath("/sdcard/download/老友记第一季-01.mp4");
        mVideoView.setVideoURI(uri);
        mVideoView.start();
    }
}
