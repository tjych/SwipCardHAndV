package com.tjych.swip.swip;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by tjych on 2015/6/1.
 */
public class CustomFrameLayout extends FrameLayout {


    private float mLastY;
    private float mLastX;

    public CustomFrameLayout(Context context) {
        super(context);
        init();
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

    }



}
