package com.seanyj.mysamples.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.seanyj.mysamples.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioSynthesisActivity extends AppCompatActivity {

    @BindView(R.id.startSoundBtn)
    TextView mStartSoundBtn;
    @BindView(R.id.endSoundBtn)
    TextView mEndSoundBtn;

    private AudioSynthesisTask audioSynth;
    private boolean keepGoing = false;
    private float synth_frequecy = 440;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_synthesis);
        ButterKnife.bind(this);
        mEndSoundBtn.setEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        keepGoing = false;
        mEndSoundBtn.setEnabled(false);
        mStartSoundBtn.setEnabled(true);
    }

    @OnClick({R.id.startSoundBtn, R.id.endSoundBtn})
    void onViewClick(View v) {
        if (v == mStartSoundBtn) {
            keepGoing = true;
            audioSynth = new AudioSynthesisTask();
            audioSynth.execute();
            mEndSoundBtn.setEnabled(true);
            mStartSoundBtn.setEnabled(false);
        } else if (v == mEndSoundBtn) {
            keepGoing = false;
            mEndSoundBtn.setEnabled(false);
            mStartSoundBtn.setEnabled(true);
        }
    }

    private class AudioSynthesisTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            final int SAMPLE_RATE = 11025;
            int minSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                    AudioFormat.ENCODING_PCM_16BIT);
            AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                    AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, minSize,
                    AudioTrack.MODE_STREAM);
            audioTrack.play();

//            short[] buffer = {
//                    8130, 15752, 22389, 27625, 31134, 32695, 32210, 29711,
//                    25354, 19410, 4329, -3865, -11818, -19032, -25055,
//                    -29511, -32121, -32722, -31276, -27874, -22728, -16160,
//                    -8582, -466
//            };
//            while (keepGoing) {
//                audioTrack.write(buffer, 0, buffer.length);
//            }

            short[] buffer = new short[minSize];
            float angular_frequency = (float) (2 * Math.PI) * synth_frequecy / SAMPLE_RATE;
            float angle = 0;
            while (keepGoing) {
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (short) (Short.MAX_VALUE * (float)Math.sin(angle));
                    angle += angular_frequency;
                }
                audioTrack.write(buffer, 0, buffer.length);
            }
            return null;
        }
    }
}












