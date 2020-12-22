package com.seanyj.mysamples.media.audio;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.seanyj.mysamples.R;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MediaRecorderActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    @BindView(R.id.recordBtn)
    TextView mRecordBtn;
    @BindView(R.id.playBtn)
    TextView mPlayBtn;
    @BindView(R.id.statusTextView)
    TextView mStatusTextView;
    @BindView(R.id.startBtn)
    TextView mStartBtn;
    @BindView(R.id.stopBtn)
    TextView mStopBtn;
    @BindView(R.id.playRecordBtn)
    TextView mPlayRecordBtn;
    @BindView(R.id.finishBtn)
    TextView mFinishBtn;

    private Uri mAudioFileUri;
    private MediaPlayer mMediaPlayer;
    private MediaRecorder mMediaRecorder;
    private File mAudioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ButterKnife.bind(this);
        findViewById(R.id.playBtn).setEnabled(false);
        mStatusTextView.setText("ready");
        mPlayRecordBtn.setEnabled(false);
        mStopBtn.setEnabled(false);
    }

    @OnClick({R.id.playBtn, R.id.recordBtn, R.id.startBtn, R.id.stopBtn, R.id.playRecordBtn, R.id.finishBtn})
    public void onViewClick(View v) {
        if (v.getId() == R.id.playBtn) {
            // with testing, error occurs which says finding no activity to hand this intent
            MediaPlayer mediaPlayer = MediaPlayer.create(this, mAudioFileUri);
            mediaPlayer.start();
            findViewById(R.id.recordBtn).setEnabled(false);
        } else if (v.getId() == R.id.recordBtn) {
            Intent i = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            startActivityForResult(i, 0);
        } else if (v.getId() == R.id.startBtn) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            File path = getExternalFilesDir("");
            path.mkdirs();
            try {
                mAudioFile = File.createTempFile("recording", ".3gp", path);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaRecorder.start();
            mStatusTextView.setText("Recording");
            mPlayRecordBtn.setEnabled(false);
            mStopBtn.setEnabled(true);
            mStartBtn.setEnabled(false);
        } else if (v == mFinishBtn) {
            finish();
        } else if (v == mStopBtn) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            try {
                mMediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mMediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mStatusTextView.setText("Ready to play");
            mPlayRecordBtn.setEnabled(true);
            mStopBtn.setEnabled(false);
            mStartBtn.setEnabled(true);
        } else if (v == mPlayRecordBtn) {
            mMediaPlayer.start();
            mStatusTextView.setText("Playing");
            mPlayRecordBtn.setEnabled(false);
            mStopBtn.setEnabled(false);
            mStartBtn.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            mAudioFileUri = data.getData();
            findViewById(R.id.playBtn).setEnabled(true);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlayRecordBtn.setEnabled(true);
        mStopBtn.setEnabled(false);
        mStartBtn.setEnabled(true);
        mStatusTextView.setText("Ready");
    }
}
