package com.example.custom;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import static android.content.ContentValues.TAG;

/**
 * @author Lix
 * @date 2018/10/16 17:26
 */
public class CustomGestureDetector implements GestureDetector.OnGestureListener {


    private CustomView mCustomView;

    private static final int degreeLimit = 30;
    private static final int distanceLimit = 15;

    private boolean isScroll = false;


    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        Log.d(TAG, "myGestureDetectorLis onDown");
        isScroll = false;
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        if (isScroll) return false;
        double degree = Math.atan(Math.abs(e2.getY() - e1.getY()) / Math.abs(e2.getX() - e1.getX())) * 180 / Math.PI;
        float delta = e2.getX() - e1.getX();
        if (delta > distanceLimit && degree < degreeLimit) {
            Log.d(TAG, "向右滑");
            isScroll = true;
            mCustomView.scrollRight();
        } else if (delta < -distanceLimit && degree < degreeLimit) {
            Log.d(TAG, "向左滑");
            isScroll = true;
            mCustomView.scrollLeft();
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // TODO Auto-generated method stub
        return false;
    }

    public CustomView getCustomView() {
        return mCustomView;
    }

    public void setCustomView(CustomView customView) {
        mCustomView = customView;
    }
}