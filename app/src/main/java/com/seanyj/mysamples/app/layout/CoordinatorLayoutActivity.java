package com.seanyj.mysamples.app.layout;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.seanyj.mysamples.R;

public class CoordinatorLayoutActivity extends AppCompatActivity implements CoordinatorFragment.OnItemClickListener{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinatorlayout);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout,
                new CoordinatorFragment()).commit();
    }

    @Override
    public void onItemClick(View v) {
        Fragment fragment = null;
        switch (v.getId()) {
            case R.id.followBehaviorButton:
                fragment = new FollowBehaviorFragment();
                break;
            case R.id.appBarButton:
                fragment = new AppBarFragment();
                break;
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.containerLayout,
                fragment).addToBackStack(null).commit();
    }
}
