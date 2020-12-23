package com.seanyj.mysamples.animation;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.seanyj.mysamples.R;

public class PicZoomActivity extends AppCompatActivity implements UIAnimation.UIStateListener {
    private RelativeLayout mOuterCircle;
    private View mInnerCircle;

    private UIAnimation mUIAnimation;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_zoom);
        mOuterCircle = findViewById(R.id.outer_circle);
        mInnerCircle = findViewById(R.id.inner_circle);

        mProgressBar = findViewById(R.id.progress_bar);

        start();
    }

    private void start() {
        int[] thumbResources = new int[] {R.id.mic, R.id.play, R.id.music};
        ImageView[] thumbs = new ImageView[3];
        for(int i=0; i < 3; i++) {
            thumbs[i] = findViewById(thumbResources[i]);
        }
        View containerView = findViewById(R.id.container);
        ImageView expandedView = findViewById(R.id.expanded);
        int animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mUIAnimation = new UIAnimation(containerView, thumbs, expandedView, animationDuration,
                this);
    }

    @Override
    public void onUIStateChanged(UIAnimation.UIState state) {
    }

}
