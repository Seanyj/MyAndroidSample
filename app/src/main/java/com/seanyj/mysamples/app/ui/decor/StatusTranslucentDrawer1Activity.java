package com.seanyj.mysamples.app.ui.decor;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.SystemUiUtil;

public class StatusTranslucentDrawer1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_translucent_drawer1);
        setStatusBarHalfTransparent();
        setDrawerLayoutFitSystemWindow();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
}
