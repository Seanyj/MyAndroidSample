package com.seanyj.mysamples.app.layout;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.seanyj.mysamples.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppBarFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_app_bar, container, false);
        Toolbar toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getTitle());

        ListView listView = (ListView) root.findViewById(R.id.listView);
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            listView.setNestedScrollingEnabled(true);
        }

        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("str", "# " + i);
            list.add(map);
        }
        listView.setAdapter(new SimpleAdapter(getContext(), list, android.R.layout.simple_list_item_1,
                new String[]{"str"}, new int[]{android.R.id.text1}));

        return root;
    }
}
