package com.seanyj.mysamples.interaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.seanyj.mysamples.R;
import com.seanyj.mysamples.util.DensityUtil;

public class CoordinatorLayoutDemo1Activity extends AppCompatActivity {
    private int height = 0;
    private TextView mTitleView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatorlayout_demo1);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.mCollapsingToolbarLayout);
        mTitleView = findViewById(R.id.titleBar);
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (height == 0) {
                    height = collapsingToolbarLayout.getHeight();
                    Log.e("hello", "height: " + height);
                    height = height - DensityUtil.dp2px(CoordinatorLayoutDemo1Activity.this, 60);
                    Log.e("hello", "fix Height: " + height);
                }


                Log.e("hello", "verticalOffset: " + verticalOffset);
                float alpha = (float) Math.abs(verticalOffset) / height;
                Log.e("hello", "alpha: " + alpha);
                mTitleView.setAlpha(alpha);
            }
        });

    }
}
