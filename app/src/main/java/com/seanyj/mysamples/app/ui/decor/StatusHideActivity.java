package com.seanyj.mysamples.app.ui.decor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.DensityUtil;

public class StatusHideActivity extends AppCompatActivity {

    private boolean mIsNormal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_hide);

        View decor = getWindow().getDecorView();
        decor.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                Log.e("hello", "onSystemUiVisibilityChange: " + visibility);
            }
        });

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        toolBar.setFitsSystemWindows(true);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && !mIsNormal) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    public void onClick(final View v) {
        mIsNormal = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int options = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            final View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(options);
        }

        final ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.scenery1);
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setContentView(iv);
        popupWindow.setWidth(DensityUtil.getScreenSize(this).x);
        popupWindow.setHeight(DensityUtil.getScreenSize(this).y);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));// 给popupWindow设置背景
        popupWindow.setAnimationStyle(0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            iv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv.animate().scaleX(0.2f).scaleY(0.2f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        popupWindow.dismiss();
                    }
                });
                ColorDrawable background = (ColorDrawable) popupWindow.getBackground();
                ValueAnimator colorAnim = ObjectAnimator.ofInt(background,"color", 0xFF000000, 0x00000000);
                colorAnim.setDuration(2000);
                colorAnim.setEvaluator(new ArgbEvaluator());
                colorAnim.start();
            }
        });

        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, 0, 0);
        iv.setScaleX(0.2f);
        iv.setScaleY(0.2f);
        iv.animate().scaleX(1).scaleY(1).setDuration(2000).start();
        ColorDrawable background = (ColorDrawable) popupWindow.getBackground();
        ValueAnimator colorAnim = ObjectAnimator.ofInt(background,"color", 0x00000000, 0xFF000000);
        colorAnim.setDuration(2000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();
    }

}
