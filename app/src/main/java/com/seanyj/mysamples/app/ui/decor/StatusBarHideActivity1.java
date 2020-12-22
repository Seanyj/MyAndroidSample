package com.seanyj.mysamples.app.ui.decor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.DensityUtil;


public class StatusBarHideActivity1 extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_hide_1);
        getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        if (hasFocus) {
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//        }
    }

    public void onClick2(final View v) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        final View root = dialog.findViewById(R.id.dlg_root);
        root.setScaleX(0.2f);
        root.setScaleY(0.2f);
        root.animate().scaleX(1).scaleY(1).setDuration(2000).start();
//        ColorDrawable background = (ColorDrawable) popupWindow.getBackground();
//        ValueAnimator colorAnim = ObjectAnimator.ofInt(background, "color", 0x00000000, 0xFF000000);
//        colorAnim.setDuration(2000);
//        colorAnim.setEvaluator(new ArgbEvaluator());
//        colorAnim.start();

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.animate().scaleX(0.2f).scaleY(0.2f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void onClick1(View v) {
        final View decor = getWindow().getDecorView();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            int options = View.SYSTEM_UI_FLAG_VISIBLE;
            decor.setSystemUiVisibility(options);
        }
    }

    public void onClick(final View v) {

        final View decor = getWindow().getDecorView();
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
            iv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    iv.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }

                iv.animate().scaleX(0.2f).scaleY(0.2f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        popupWindow.dismiss();
                    }
                });
                ColorDrawable background = (ColorDrawable) popupWindow.getBackground();
                ValueAnimator colorAnim = ObjectAnimator.ofInt(background, "color", 0xFF000000, 0x00000000);
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
        ValueAnimator colorAnim = ObjectAnimator.ofInt(background, "color", 0x00000000, 0xFF000000);
        colorAnim.setDuration(2000);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.start();

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }
}
