package com.seanyj.mysamples.app.layout;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seanyj.mysamples.R;

public class CollapseFragment1 extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_collapse1, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);

        return root;
    }
}
