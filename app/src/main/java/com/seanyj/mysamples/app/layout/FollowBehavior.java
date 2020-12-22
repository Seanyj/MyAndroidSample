package com.seanyj.mysamples.app.layout;

import android.content.Context;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FollowBehavior extends CoordinatorLayout.Behavior<TextView>{
    public FollowBehavior() {
    }

    public FollowBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency instanceof Button;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {
        child.setX(dependency.getX());
        child.setY(dependency.getY() + 100);
        return true;
    }
}
