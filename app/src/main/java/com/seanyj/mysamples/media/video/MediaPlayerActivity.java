package com.seanyj.mysamples.media.video;

import android.media.MediaPlayer;
import android.os.Environment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import com.seanyj.mysamples.R;

import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback,
        MediaController.MediaPlayerControl
{
    private Display currentDisplay;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private MediaController controller;
    int videoWidth = 0;
    int videoHeight = 0;
    boolean readyToPlay = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnInfoListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnSeekCompleteListener(this);
        mediaPlayer.setOnVideoSizeChangedListener(this);
        controller = new MediaController(this);

        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                + "//老友记第一季-01.mp4";
        try {
            mediaPlayer.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentDisplay = getWindowManager().getDefaultDisplay();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
            Log.v("hello", "Media Error, Server Died " + extra);
        } else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
            Log.v("hello", "Media Error, Error Unknown " + extra);
        }
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
        } else if (what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
        } else if (what == MediaPlayer.MEDIA_INFO_UNKNOWN) {
        } else if (what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
        } else if (what == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        videoWidth = mp.getVideoWidth();
        videoHeight = mp.getVideoHeight();
        if (videoWidth > currentDisplay.getWidth() || videoHeight > currentDisplay.getHeight()) {
            float heightRatio = (float)videoHeight / currentDisplay.getHeight();
            float widthRatio = (float)videoWidth / currentDisplay.getWidth();
            if (heightRatio > 1 || widthRatio > 1) {
                if (heightRatio > widthRatio) {
                    videoHeight = (int) Math.ceil(videoHeight / (float)heightRatio);
                    videoWidth = (int) Math.ceil(videoWidth / (float)heightRatio);
                } else {
                    videoHeight = (int) Math.ceil(videoHeight / (float)widthRatio);
                    videoWidth = (int) Math.ceil(videoWidth / (float)widthRatio);
                }
            }
            surfaceView.setLayoutParams(new ConstraintLayout.LayoutParams(videoWidth, videoHeight));
            mp.start();

            controller.setMediaPlayer(this);
            controller.setAnchorView(findViewById(R.id.MainView));
            controller.setEnabled(true);
            controller.show();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mediaPlayer.setDisplay(holder);
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (controller.isShowing()) {
            controller.hide();
        } else {
            controller.show();
        }
        return false;
    }

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
