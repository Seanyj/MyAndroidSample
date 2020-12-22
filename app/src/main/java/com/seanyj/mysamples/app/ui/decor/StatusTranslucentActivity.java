package com.seanyj.mysamples.app.ui.decor;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.SystemUiUtil;

public class StatusTranslucentActivity extends AppCompatActivity {
    private int mMode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView iv = new ImageView(this);
        FrameLayout frameLayout = new FrameLayout(this);
        iv.setImageResource(R.drawable.scenery18);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        frameLayout.addView(iv);
        setContentView(frameLayout);

        setStatusBarFullTransparent();

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = (mMode + 1) % 3;
                if (mMode == 0) {
                    setStatusBarFullTransparent();
                } else if (mMode == 1) {
                    setStatusBarHalfTransparent();
                } else if (mMode == 2) {
                    setStatusColor(StatusTranslucentActivity.this, Color.RED);
                }
            }
        });
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setStatusBarHalfTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //虚拟键盘也透明
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        }
    }

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        View contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
    protected void setDrawerLayoutFitSystemWindow() {
        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
            int statusBarHeight = SystemUiUtil.getStatusBarHeight(this);
            View contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
            if (contentViewGroup instanceof DrawerLayout) {
                DrawerLayout drawerLayout = (DrawerLayout) contentViewGroup;
                drawerLayout.setClipToPadding(true);
                drawerLayout.setFitsSystemWindows(false);
                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
                    View child = drawerLayout.getChildAt(i);
                    child.setFitsSystemWindows(false);
                    child.setPadding(0, statusBarHeight, 0, 0);
                }

            }
        }
    }

    public static void setStatusColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = new View(activity);
            statusView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    SystemUiUtil.getStatusBarHeight(activity)));
            statusView.setBackgroundColor(color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
        }

        // 没有效果， fitSystemWindow测试只有在Activity首次加载起作用
        ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
        rootView.setClipToPadding(true);
    }

}
