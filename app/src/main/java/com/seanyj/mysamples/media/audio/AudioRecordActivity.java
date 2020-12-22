package com.seanyj.mysamples.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.seanyj.mysamples.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioRecordActivity extends AppCompatActivity {
    @BindView(R.id.statusTextView)
    TextView mStatusTextView;
    @BindView(R.id.startRecordingBtn)
    TextView mStartRecordingBtn;
    @BindView(R.id.stopRecordingBtn)
    TextView mStopRecordingBtn;
    @BindView(R.id.startPlaybackBtn)
    TextView mStartPlaybackBtn;
    @BindView(R.id.stopPlaybackBtn)
    TextView mStopPlaybackBtn;

    private RecordAudio recordTask;
    private PlayAudio playTask;
    private File mRecordingFile;
    private boolean isRecording = false;
    private boolean isPlaying = false;
    private final int frequency = 11025;
    private int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        ButterKnife.bind(this);

        mStopRecordingBtn.setEnabled(false);
        mStartPlaybackBtn.setEnabled(false);
        mStopPlaybackBtn.setEnabled(false);

        File path = getExternalFilesDir("");
        path.mkdirs();
        try {
            mRecordingFile = File.createTempFile("recording", ".pcm", path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.startPlaybackBtn, R.id.stopPlaybackBtn, R.id.startRecordingBtn, R.id.stopRecordingBtn})
    void onViewClick(View v) {
        if (v == mStartRecordingBtn) {
            record();
        } else if (v == mStopRecordingBtn) {
            stopRecording();
        } else if (v == mStartPlaybackBtn) {
            play();
        } else if (v == mStopPlaybackBtn) {
            stopPlaying();
        }
    }

    private void stopPlaying() {
        isPlaying = false;
        mStopPlaybackBtn.setEnabled(false);
        mStartPlaybackBtn.setEnabled(true);
    }

    private void play() {
        mStartPlaybackBtn.setEnabled(true);
        playTask = new PlayAudio();
        playTask.execute();
        mStopPlaybackBtn.setEnabled(true);
    }

    private void stopRecording() {
        isRecording = false;
    }

    private void record() {
        mStartRecordingBtn.setEnabled(false);
        mStopRecordingBtn.setEnabled(true);
        recordTask = new RecordAudio();
        recordTask.execute();
    }

    private class PlayAudio extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            isPlaying = true;
            int bufferSize = AudioTrack.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
            short[] audiodata = new short[bufferSize / 4];

            try {
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(mRecordingFile)));
                AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
                        channelConfiguration, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                audioTrack.play();

                while (isPlaying && dis.available() > 0) {
                    int i = 0;
                    while (dis.available() > 0 && i < audiodata.length) {
                        audiodata[i] = dis.readShort();
                        i++;
                    }
                    audioTrack.write(audiodata, 0, audiodata.length);
                }
                dis.close();
                mStopPlaybackBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        mStartPlaybackBtn.setEnabled(false);
                        mStopPlaybackBtn.setEnabled(true);
                    }
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class RecordAudio extends AsyncTask<Void, Integer, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            isRecording = true;
            try {
                DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(mRecordingFile)));
                int bufferSize = AudioRecord.getMinBufferSize(frequency, channelConfiguration, audioEncoding);
                AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
                        channelConfiguration, audioEncoding, bufferSize);
                short[] buffer = new short[bufferSize];
                audioRecord.startRecording();

                int r = 0;
                while (isRecording) {
                    int bufferReadResult = audioRecord.read(buffer, 0, bufferSize);
                    for (int i = 0; i < bufferReadResult; i++) {
                        dos.writeShort(buffer[i]);
                    }
                    publishProgress(new Integer(r));
                    r++;
                }

                audioRecord.stop();
                dos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mStatusTextView.setText(values[0].toString());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mStartRecordingBtn.setEnabled(true);
            mStopRecordingBtn.setEnabled(false);
            mStartPlaybackBtn.setEnabled(true);
        }
    }
}











