package com.seanyj.mysamples.app.layout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.seanyj.mysamples.R;

public class FollowBehaviorFragment extends Fragment{
    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_follow_behavior, container, false);
        final TextView textView = (TextView) root.findViewById(R.id.textView);
        Button button = (Button) root.findViewById(R.id.button);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int x = (int) (event.getRawX() - v.getWidth() / 2);
                    int y = (int) (event.getRawY() - v.getHeight() / 2);
                    v.setX(x);
                    v.setY(y);
                }
                return true;
            }
        });
        return root;
    }
}
