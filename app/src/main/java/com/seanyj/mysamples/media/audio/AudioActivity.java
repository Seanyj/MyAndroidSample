package com.seanyj.mysamples.media.audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.seanyj.mysamples.R;

import java.io.File;
import java.io.IOException;

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        findViewById(R.id.useSystemBtn).setOnClickListener(this);
        findViewById(R.id.playRawBtn).setOnClickListener(this);
        findViewById(R.id.playHttpBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.useSystemBtn:
                Intent i = new Intent(Intent.ACTION_VIEW);
                File sdcard = Environment.getExternalStorageDirectory();
                File audioFile = new File(sdcard, "/Download/m1.mp3");
                Uri uri = Uri.fromFile(audioFile);
                i.setDataAndType(uri, "audio/mp3");
                startActivity(i);
                break;
            case R.id.playRawBtn:
                if (mMediaPlayer == null) {
//                    mMediaPlayer = MediaPlayer.create(this,
//                            Uri.parse("android.resource://com.seanyj.mysamples/" + R.raw.test_cbr));
                    mMediaPlayer = MediaPlayer.create(this, R.raw.test_cbr);
                    //  set loop play, alternate way is implementing OnCompletionListener
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.start();
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    } else {
                        mMediaPlayer.start();
                    }
                }
                break;
            case R.id.playHttpBtn:
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                    try {
                        mMediaPlayer.setDataSource("http://localhost:8080/hello/m1.mp3");
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.pause();
                    } else {
                        mMediaPlayer.start();
                    }
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMediaPlayer!= null && !mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
