package com.seanyj.mysamples.media.audio;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.seanyj.mysamples.R;

public class AudioSynthesis1Activity extends AppCompatActivity implements View.OnTouchListener {
    static final float BASE_FREQUENCY = 440;

    private AudioSynthesisTask audioSynth;
    private boolean play = false;
    private float synth_frequency = BASE_FREQUENCY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_synthesis1);

        View mainView = findViewById(R.id.rootView);
        mainView.setOnTouchListener(this);
        audioSynth = new AudioSynthesisTask();
        audioSynth.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        play = false;
        finish();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                play = true;
                synth_frequency = event.getX() + BASE_FREQUENCY;
                break;
            case MotionEvent.ACTION_MOVE:
                play = true;
                synth_frequency = event.getX() + BASE_FREQUENCY;
                break;
            case MotionEvent.ACTION_UP:
                play = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;

        }
        return true;
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
            float angle = 0;
            while (true) {
                if (play) {
                    for (int i = 0; i < buffer.length; i++) {
                        float angular_frequency = (float) (2 * Math.PI) * synth_frequency / SAMPLE_RATE;
                        buffer[i] = (short) (Short.MAX_VALUE * (float) Math.sin(angle));
                        angle += angular_frequency;
                    }
                    audioTrack.write(buffer, 0, buffer.length);
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}












