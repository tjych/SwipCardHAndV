package com.tjych.swip.swip;


import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.tjych.swip.R;
import com.tjych.swip.activity.MainActivity;
import com.tjych.swip.adapter.TestFragmentAdapter;
import com.tjych.swip.vertical.DirectionalViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dionysis_lorentzos on 5/8/14
 * for package com.lorentzos.swipecards
 * and project Swipe cards.
 * Use with caution dinosaurs might appear!
 */

public class SwipeFlingAdapterView extends BaseFlingAdapterView {


    private int MAX_VISIBLE = 4;
    private int MIN_ADAPTER_STACK = 6;
    private float ROTATION_DEGREES = 15.f;

    private Adapter mAdapter;
    private int LAST_OBJECT_IN_STACK = 0;
    private onFlingListener mFlingListener;
    private AdapterDataSetObserver mDataSetObserver;
    private boolean mInLayout = false;
    private View mActiveCard = null;
    private OnItemClickListener mOnItemClickListener;
    private FlingCardListener flingCardListener;
    private DirectionalViewPager directionalViewPager;


    public SwipeFlingAdapterView(Context context) {
        this(context, null);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.SwipeFlingStyle);
    }

    public SwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeFlingAdapterView, defStyle, 0);
        MAX_VISIBLE = a.getInt(R.styleable.SwipeFlingAdapterView_max_visible, MAX_VISIBLE);
        MIN_ADAPTER_STACK = a.getInt(R.styleable.SwipeFlingAdapterView_min_adapter_stack, MIN_ADAPTER_STACK);
        ROTATION_DEGREES = a.getFloat(R.styleable.SwipeFlingAdapterView_rotation_degrees, ROTATION_DEGREES);
        a.recycle();
    }


    /**
     * A shortcut method to set both the listeners and the adapter.
     *  @param context  The activity context which extends onFlingListener, OnItemClickListener or both
     * @param mAdapter The adapter you have to set.
     */
    public void init(final Context context, Adapter mAdapter) {
        if (context instanceof onFlingListener) {
            mFlingListener = (onFlingListener) context;
        } else {
            throw new RuntimeException("Activity does not implement SwipeFlingAdapterView.onFlingListener");
        }
        if (context instanceof OnItemClickListener) {
            mOnItemClickListener = (OnItemClickListener) context;
        }
        setAdapter(mAdapter);
    }

    @Override
    public View getSelectedView() {
        return mActiveCard;
    }


    @Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        try{
            // if we don't have an adapter, we don't need to do anything
            if (mAdapter == null) {
                return;
            }

            mInLayout = true;
            final int adapterCount = mAdapter.getCount();
            System.out.println(adapterCount + " /// ");
            if (adapterCount == 0) {
                removeAllViewsInLayout();
            } else {
                View topCard = getChildAt(LAST_OBJECT_IN_STACK);
                if (mActiveCard != null && topCard != null && topCard == mActiveCard) {
                    removeViewsInLayout(0, LAST_OBJECT_IN_STACK);
                    layoutChildren(1, adapterCount);
                } else {
                    // Reset the UI and set top view listener
                    removeAllViewsInLayout();
                    layoutChildren(0, adapterCount);
                    setTopView();
                }
            }

            mInLayout = false;

            if (adapterCount < MAX_VISIBLE) mFlingListener.onAdapterAboutToEmpty(adapterCount);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private Map<Integer,View> viewMap = new HashMap<Integer,View>();


    private void layoutChildren(int startingIndex, int adapterCount) {
        System.out.println(startingIndex + " ... ");
        while (startingIndex < Math.min(adapterCount, MAX_VISIBLE)) {
            View newUnderChild = mAdapter.getView(startingIndex, null, this);

            System.out.println(newUnderChild  + " new Under Child");
            if (newUnderChild.getVisibility() != GONE) {
                makeAndAddView(newUnderChild);
                LAST_OBJECT_IN_STACK = startingIndex;
            }
            startingIndex++;
        }
    }



    private void makeAndAddView(View child) {

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
//		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(350,500);
        if(lp == null){
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        addViewInLayout(child, 0, lp, true);

        final boolean needToMeasure = child.isLayoutRequested();
        if (needToMeasure) {
            try{
                int childWidthSpec = getChildMeasureSpec(getWidthMeasureSpec(),
                        getPaddingLeft() + getPaddingRight() + lp.leftMargin + lp.rightMargin,
                        lp.width);
                int childHeightSpec = getChildMeasureSpec(getHeightMeasureSpec(),
                        getPaddingTop() + getPaddingBottom() + lp.topMargin + lp.bottomMargin,
                        lp.height);
                System.out.println(childWidthSpec + " childWidthSpec "+  childHeightSpec + " childHeightSpec");
                child.measure(childWidthSpec, childHeightSpec);
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            cleanupLayoutState(child);
        }


        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();

        int gravity = lp.gravity;
        if (gravity == -1) {
            gravity = Gravity.TOP | Gravity.START;
        }


//        int layoutDirection = getLayoutDirection();
        int layoutDirection = 1;
//        final int absoluteGravity = Gravity.getAbsoluteGravity(gravity, layoutDirection);
        final int absoluteGravity = gravity;
        final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

        int childLeft;
        int childTop;
        switch (absoluteGravity & Gravity.HORIZONTAL_GRAVITY_MASK) {
            case Gravity.CENTER_HORIZONTAL:
                childLeft = (getWidth() + getPaddingLeft() - getPaddingRight() - w) / 2 +
                        lp.leftMargin - lp.rightMargin;
                break;
            case Gravity.END:
                childLeft = getWidth() + getPaddingRight() - w - lp.rightMargin;
                break;
            case Gravity.START:
            default:
                childLeft = getPaddingLeft() + lp.leftMargin;
                break;
        }
        switch (verticalGravity) {
            case Gravity.CENTER_VERTICAL:
                childTop = (getHeight() + getPaddingTop() - getPaddingBottom() - h) / 2 +
                        lp.topMargin - lp.bottomMargin;
                break;
            case Gravity.BOTTOM:
                childTop = getHeight() - getPaddingBottom() - h - lp.bottomMargin;
                break;
            case Gravity.TOP:
            default:
                childTop = getPaddingTop() + lp.topMargin;
                break;
        }

        child.layout(childLeft, childTop, childLeft + w, childTop + h);
    }


    /**
     * Set the top view and add the fling listener
     */
    private void setTopView() {
        if (getChildCount() > 0) {

            mActiveCard = getChildAt(LAST_OBJECT_IN_STACK);
            if (mActiveCard != null) {

                flingCardListener = new FlingCardListener(this,mActiveCard, mAdapter.getItem(0),
                        ROTATION_DEGREES,new FlingCardListener.FlingListener() {

                    @Override
                    public void onCardExited() {
                        mActiveCard = null;
                        System.out.println(" onCardExited ");
                        mFlingListener.removeFirstObjectInAdapter();
                    }

                    @Override
                    public void leftExit(Object dataObject) {
                        mFlingListener.onLeftCardExit(dataObject);
                    }

                    @Override
                    public void rightExit(Object dataObject) {
                        mFlingListener.onRightCardExit(dataObject);
                    }

                    @Override
                    public void onClick(Object dataObject) {
                        if (mOnItemClickListener != null)
                            mOnItemClickListener.onItemClicked(0, dataObject);
                    }

                    @Override
                    public void onScroll(float scrollProgressPercent) {
                        mFlingListener.onScroll(scrollProgressPercent);
                    }

                    @Override
                    public void onAbove(Object dataObject) {
                        mFlingListener.onAboveCard(dataObject);
                    }

                    @Override
                    public void onBelow(Object dataObject) {
                        mFlingListener.onBelowCard(dataObject);
                    }
                });


                mActiveCard.setOnTouchListener(flingCardListener);
            }
        }
    }

    public FlingCardListener getTopCardListener() throws NullPointerException {
        if (flingCardListener == null) {
            throw new NullPointerException();
        }
        return flingCardListener;
    }

    public void setMaxVisible(int MAX_VISIBLE) {
        this.MAX_VISIBLE = MAX_VISIBLE;
    }

    public void setMinStackInAdapter(int MIN_ADAPTER_STACK) {
        this.MIN_ADAPTER_STACK = MIN_ADAPTER_STACK;
    }

    @Override
    public Adapter getAdapter() {
        return mAdapter;
    }


    @Override
    public void setAdapter(Adapter adapter) {
        try{
            if (mAdapter != null && mDataSetObserver != null) {
                mAdapter.unregisterDataSetObserver(mDataSetObserver);
                mDataSetObserver = null;
            }

            mAdapter = adapter;

            if (mAdapter != null && mDataSetObserver == null) {
                mDataSetObserver = new AdapterDataSetObserver();
                mAdapter.registerDataSetObserver(mDataSetObserver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setFlingListener(onFlingListener onFlingListener) {
        this.mFlingListener = onFlingListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FrameLayout.LayoutParams(getContext(), attrs);
    }


    public interface OnItemClickListener {
        public void onItemClicked(int itemPosition, Object dataObject);
    }


    public interface onFlingListener {
        public void removeFirstObjectInAdapter();

        public void onLeftCardExit(Object dataObject);

        public void onRightCardExit(Object dataObject);

        public void onAdapterAboutToEmpty(int itemsInAdapter);

        public void onScroll(float scrollProgressPercent);

        public void onAboveCard(Object dataObject);

        public void onBelowCard(Object dataObject);


    }

    private class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            try{
                requestLayout();
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onInvalidated() {
            requestLayout();
        }

    }


}
