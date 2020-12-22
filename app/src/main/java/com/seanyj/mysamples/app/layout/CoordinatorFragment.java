package com.seanyj.mysamples.app.layout;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seanyj.mysamples.R;

public class CoordinatorFragment extends Fragment implements View.OnClickListener{
    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof  OnItemClickListener)) {
            throw new IllegalArgumentException("activity must implements OnItemClickListener");
        }

        mOnItemClickListener = (OnItemClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_coordinator, container, false);
        root.findViewById(R.id.appBarButton).setOnClickListener(this);
        root.findViewById(R.id.followBehaviorButton).setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        mOnItemClickListener.onItemClick(v);
    }

    public interface OnItemClickListener {
        void onItemClick(View v);
    }
}
