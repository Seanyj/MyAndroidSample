package com.seanyj.mysamples.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.seanyj.mysamples.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimatorTestActivity extends AppCompatActivity  {
    @BindView(R.id.menuButton)
    Button mMenuButton;
    @BindView(R.id.twitterButton)
    Button mTwitterButton;
    @BindView(R.id.facebookButton)
    Button mFacebookButton;
    @BindView(R.id.googleButton)
    Button mGoogleButton;
    @BindView(R.id.youtubeButton)
    Button mYoutubeButton;
    @BindView(R.id.yahooButton)
    Button mYahooButton;
    @BindView(R.id.button)
    Button mButton;

    private boolean mIsMenuOpen = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ainmator_test);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.menuButton, R.id.twitterButton, R.id.facebookButton, R.id.googleButton, R.id.youtubeButton,
            R.id.yahooButton, R.id.button})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.menuButton:
                if (!mIsMenuOpen) {
                    mIsMenuOpen = true;
                    doAnimateOpen(mTwitterButton, 0, 5, 300);
                    doAnimateOpen(mFacebookButton, 1, 5, 300);
                    doAnimateOpen(mGoogleButton, 2, 5, 300);
                    doAnimateOpen(mYoutubeButton, 3, 5, 300);
                    doAnimateOpen(mYahooButton, 4, 5, 300);
                } else {
                    mIsMenuOpen = false;
                    doAnimateClose(mTwitterButton, 0, 5, 300);
                    doAnimateClose(mFacebookButton, 1, 5, 300);
                    doAnimateClose(mGoogleButton, 2, 5, 300);
                    doAnimateClose(mYoutubeButton, 3, 5, 300);
                    doAnimateClose(mYahooButton, 4, 5, 300);
                }
                break;
            case R.id.twitterButton:
                Toast.makeText(this, "twitter", Toast.LENGTH_SHORT).show();
                break;
            case R.id.facebookButton:
                Toast.makeText(this, "facebook", Toast.LENGTH_SHORT).show();
                break;
            case R.id.googleButton:
                Toast.makeText(this, "google", Toast.LENGTH_SHORT).show();
                break;
            case R.id.youtubeButton:
                Toast.makeText(this, "youtube", Toast.LENGTH_SHORT).show();
                break;
            case R.id.yahooButton:
                Toast.makeText(this, "yahoo", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button:
//                buttonAnimate(v);
                buttonAnimate1();
                break;
        }
    }

    private void buttonAnimate(View v) {
        ViewWrapper viewWrapper = new ViewWrapper(v);
        if (viewWrapper.getWidth() == 500) {
            ObjectAnimator.ofInt(viewWrapper, "width", 250).start();
        } else {
            ObjectAnimator.ofInt(viewWrapper, "width", 500).start();
        }
    }

    private void buttonAnimate1() {
        ValueAnimator valueAnimator =ValueAnimator.ofInt(0, 500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            private TypeEvaluator mEvaluator = new IntEvaluator();

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                float fraction = (float) (value / 500.);
                int ret = (int) mEvaluator.evaluate(fraction, 0, 500);
                mButton.getLayoutParams().width = ret;
                mButton.requestLayout();
            }
        });

        valueAnimator.setDuration(500).start();
    }

    private void doAnimateOpen(View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }

        double degree = Math.PI * index / ((total - 1) * 2);
        int translationX = (int) (radius * Math.cos(degree));
        int translationY = (int) (radius * Math.sin(degree));
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, translationX),
                ObjectAnimator.ofFloat(view, "translationY", 0, translationY),
                ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f),
                ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f),
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1)
        );

        set.setDuration(500).start();
    }

    private void doAnimateClose(final View view, int index, int total, int radius) {
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        double degree = Math.PI * index / ((total - 1) * 2);
        int translationX = (int) (radius * Math.cos(degree));
        int translationY = (int) (radius * Math.sin(degree));
        AnimatorSet set = new AnimatorSet();

        set.playTogether(
                ObjectAnimator.ofFloat(view, "translationX", translationX, 0),
                ObjectAnimator.ofFloat(view, "translationY", translationY, 0),
                ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f),
                ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f),
                ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)
        );

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(android.view.View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        set.setDuration(500).start();
    }

    private static class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }

}
