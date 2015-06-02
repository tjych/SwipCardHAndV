package com.tjych.swip.swip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.tjych.swip.R;
import com.tjych.swip.vertical_t.VerticalViewPager;

/**
 * Created by dionysis_lorentzos on 5/8/14
 * for package com.lorentzos.swipecards
 * and project Swipe cards.
 * Use with caution dinausaurs might appear!
 */


@SuppressLint("NewApi")
public class FlingCardListener implements View.OnTouchListener {


    private static final int INVALID_POINTER_ID = -1;
    private  VerticalViewPager verticalViewPager;
    private  SwipeFlingAdapterView swipFilingAdapterView;
    // The active pointer is the one currently moving our object.
    private int mActivePointerId = INVALID_POINTER_ID;
    private final float objectX;
    private final float objectY;
    private final int objectH;
    private final int objectW;
    private final int parentWidth;
    private final FlingListener mFlingListener;
    private final Object dataObject;
    private final float halfWidth;
    private final int TOUCH_ABOVE = 0;
    private final int TOUCH_BELOW = 1;
    private final Object obj = new Object();
    private float BASE_ROTATION_DEGREES;
    private float aPosX;
    private float aPosY;
    private float aDownTouchX;
    private float aDownTouchY;
    private float aDownTouchX_2;
    private float aDownTouchY_2;
    private View frame = null;
    private int touchPosition;
    private boolean isAnimationRunning = false;
    private float MAX_COS = (float) Math.cos(Math.toRadians(45));


    public FlingCardListener(View frame, Object itemAtPosition, FlingListener flingListener) {
        this(null,frame, itemAtPosition, 15f, flingListener);
    }

    public FlingCardListener(SwipeFlingAdapterView swipeFlingAdapterView ,View frame, Object itemAtPosition, float rotation_degrees, FlingListener flingListener) {
        super();
        this.frame = frame;
        this.objectX = frame.getX();
        this.objectY = frame.getY();
        this.objectH = frame.getHeight();
        this.objectW = frame.getWidth();
        this.halfWidth = objectW / 2f;
        this.dataObject = itemAtPosition;
        this.parentWidth = ((ViewGroup) frame.getParent()).getWidth();
        this.BASE_ROTATION_DEGREES = rotation_degrees;
        this.mFlingListener = flingListener;
        verticalViewPager = (VerticalViewPager) frame.findViewById(R.id.directional_view_pager);
        this.swipFilingAdapterView = swipeFlingAdapterView;
    }


    private float allRoat = 0.0f;
    private int locker = -1;


    public boolean onTouch(View view, MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                verticalViewPager.onTouchEvent(event);
                allRoat = 0.0f;
                locker = -1;
                //from http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
                // Save the ID of this pointer
                mActivePointerId = event.getPointerId(0);
                if (mActivePointerId >= event.getPointerCount()) {
                    mActivePointerId = INVALID_POINTER_ID;
                } else {
                    final float x = event.getX(mActivePointerId);
                    final float y = event.getY(mActivePointerId);

                    // Remember where we started
                    aDownTouchX = x;
                    aDownTouchY = y;
                    //to prevent an initial jump of the magnifier, aposX and aPosY must
                    //have the values from the magnifier frame
                    if (aPosX == 0) {
                        aPosX = frame.getX();
                    }
                    if (aPosY == 0) {
                        aPosY = frame.getY();
                    }

//	                if (y < objectH/2) {
                    // touchPosition = TOUCH_ABOVE;
//	                } else {
//	                    touchPosition = TOUCH_BELOW;
//	                }
                }
                break;

            case MotionEvent.ACTION_UP:
                locker = -1;
                allRoat = 0;
                mActivePointerId = INVALID_POINTER_ID;
                aDownTouchX_2 = event.getX();
                aDownTouchY_2 = event.getY();
                float dx = aDownTouchX_2 - aDownTouchX;
                float dy = aDownTouchY_2 - aDownTouchY;
                aPosX += dx;
                aPosY += dy;
                float distobjectX = aPosX - objectX;
                float rotation = BASE_ROTATION_DEGREES * 2.f * distobjectX / parentWidth;
               // System.out.println(rotation + " roation");
//                if (touchPosition == TOUCH_BELOW) {
//                    rotation = -rotation;
//                }

                if(Math.abs(rotation) < 20){
                    frame.animate()
                            .setDuration(200)
                            .setInterpolator(new OvershootInterpolator(1.5f))
                            .x(objectX)
                            .y(objectY)
                            .rotation(0);
                    mFlingListener.onScroll(0.0f);
                }

                if(Math.abs(rotation) < 4 && Math.abs(rotation) > 0){
                    verticalViewPager.onTouchEvent(event);
                    aPosX -= dx;
                    aPosY -= dy;
                    if(dy < 0){
                        mFlingListener.onAbove(dataObject);
                    }else{
                        mFlingListener.onBelow(dataObject);
                    }
                    return false;
                }
                resetCardViewOnStack();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                verticalViewPager.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                verticalViewPager.onTouchEvent(event);
                // Extract the index of the pointer that left the touch sensor
                final int pointerIndex = (event.getAction() &
                        MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // Find the index of the active pointer and fetch its position
                final int pointerIndexMove = event.findPointerIndex(mActivePointerId);
                final float xMove = event.getX(pointerIndexMove);
                final float yMove = event.getY(pointerIndexMove);

                //from http://android-developers.blogspot.com/2010/06/making-sense-of-multitouch.html
                // Calculate the distance moved
                 dx = xMove - aDownTouchX;
                dy = yMove - aDownTouchY;
                float dx_abs = Math.abs(dx);
                float dy_abs = Math.abs(dy);


                // Move the frame
                aPosX += dx;
                aPosY += dy;
                System.out.println(aPosX + " " + aPosY);


                // calculate the rotation degrees
                 distobjectX = aPosX - objectX;
                 rotation = BASE_ROTATION_DEGREES * 2.f * distobjectX / parentWidth;
               // System.out.println(rotation + " roation");
//                if (touchPosition == TOUCH_BELOW) {
//                    rotation = -rotation;
//                }
                allRoat += Math.abs(rotation);

               // System.out.println(allRoat +  " allRoat " + Math.abs(rotation) );
                System.out.println(dx_abs + " " + dy_abs + " " + allRoat);


                if(locker == -1){
                    if(dy_abs  > dx_abs){
                        locker = -2;
                        System.out.println(" locker is -2 " );
                    }else{
                        locker = -3;
                        System.out.println(" locker is -3 ");
                    }
                }
                if(locker == -2){
                    aPosX -= dx;
                    aPosY -= dy;
                    verticalViewPager.onTouchEvent(event);
                }

               if(Math.abs(rotation) < 4 && locker == -2){
                   System.out.println("垂直滑动了");
                   mFlingListener.onScroll(0.0f);
                   return false;
               }else if (locker == -3){
                   System.out.println("水平滑动了");

                   //in this area would be code for doing something with the view as the frame moves.
                   frame.setX(aPosX);
//                frame.setY(aPosY);
                   frame.setRotation(rotation);
                   mFlingListener.onScroll(getScrollProgressPercent());
               }

                break;

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }
        }

        return true;
    }




    private float getScrollProgressPercent() {
        if (movedBeyondLeftBorder()) {
            return -1f;
        } else if (movedBeyondRightBorder()) {
            return 1f;
        } else {
            float zeroToOneValue = (aPosX + halfWidth - leftBorder()) / (rightBorder() - leftBorder());
            return zeroToOneValue * 2f - 1f;
        }
    }

    private boolean resetCardViewOnStack() {
        if (movedBeyondLeftBorder()) {
            // Left Swipe
            onSelected(true, getExitPoint(-objectW), 100);
            mFlingListener.onScroll(-1.0f);
        } else if (movedBeyondRightBorder()) {
            // Right Swipe
            onSelected(false, getExitPoint(parentWidth), 100);
            mFlingListener.onScroll(1.0f);
        } else {
            float abslMoveDistance = Math.abs(aPosX - objectX);
            float abslMoveDistance_y = Math.abs(aPosY - objectY);
            System.out.println(abslMoveDistance + " " + abslMoveDistance_y + " ///  ");
            aPosX = 0;
            aPosY = 0;
            aDownTouchX = 0;
            aDownTouchY = 0;
            frame.animate()
                    .setDuration(200)
                    .setInterpolator(new OvershootInterpolator(1.5f))
                    .x(objectX)
                    .y(objectY)
                    .rotation(0);
            mFlingListener.onScroll(0.0f);
            if (abslMoveDistance < 4.0) {
                if(abslMoveDistance < 4.0 &&  abslMoveDistance_y > 40){
                    System.out.println("上下滑动");
                }else{
                    mFlingListener.onClick(dataObject);
                }

            }
        }
        return true;
    }

    private boolean movedBeyondLeftBorder() {
        return aPosX + halfWidth < leftBorder();
    }

    private boolean movedBeyondRightBorder() {
        return aPosX + halfWidth > rightBorder();
    }


    public float leftBorder() {
        return parentWidth / 4.f;
    }

    public float rightBorder() {
        return 3 * parentWidth / 4.f;
    }


    public void onSelected(final boolean isLeft,
                           float exitY, long duration) {

        isAnimationRunning = true;
        float exitX;
        if (isLeft) {
            exitX = -objectW - getRotationWidthOffset();
        } else {
            exitX = parentWidth + getRotationWidthOffset();
        }

        this.frame.animate()
                .setDuration(duration)
                .setInterpolator(new AccelerateInterpolator())
                .x(exitX)
                .y(exitY)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (isLeft) {
                            mFlingListener.onCardExited();
                            mFlingListener.leftExit(dataObject);
                        } else {
                            mFlingListener.onCardExited();
                            mFlingListener.rightExit(dataObject);
                        }
                        isAnimationRunning = false;
                    }
                })
                .rotation(getExitRotation(isLeft));
    }


    /**
     * Starts a default left exit animation.
     */
    public void selectLeft() {
        if (!isAnimationRunning)
            onSelected(true, objectY, 200);
    }

    /**
     * Starts a default right exit animation.
     */
    public void selectRight() {
        if (!isAnimationRunning)
            onSelected(false, objectY, 200);
    }


    private float getExitPoint(int exitXPoint) {
        float[] x = new float[2];
        x[0] = objectX;
        x[1] = aPosX;

        float[] y = new float[2];
        y[0] = objectY;
        y[1] = aPosY;

        LinearRegression regression = new LinearRegression(x, y);

        //Your typical y = ax+b linear regression
        return (float) regression.slope() * exitXPoint + (float) regression.intercept();
    }

    private float getExitRotation(boolean isLeft) {
        float rotation = BASE_ROTATION_DEGREES * 2.f * (parentWidth - objectX) / parentWidth;
        if (touchPosition == TOUCH_BELOW) {
            rotation = -rotation;
        }
        if (isLeft) {
            rotation = -rotation;
        }
        return rotation;
    }


    /**
     * When the object rotates it's width becomes bigger.
     * The maximum width is at 45 degrees.
     * <p/>
     * The below method calculates the width offset of the rotation.
     */
    private float getRotationWidthOffset() {
        return objectW / MAX_COS - objectW;
    }


    public void setRotationDegrees(float degrees) {
        this.BASE_ROTATION_DEGREES = degrees;
    }



    protected interface FlingListener {
        public void onCardExited();

        public void leftExit(Object dataObject);

        public void rightExit(Object dataObject);

        public void onClick(Object dataObject);

        public void onScroll(float scrollProgressPercent);

        public void onAbove(Object dataObject);

        public void onBelow(Object dataObject);

    }

}





