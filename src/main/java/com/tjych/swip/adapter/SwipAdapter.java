package com.tjych.swip.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import com.tjych.swip.R;
import com.tjych.swip.activity.MainActivity;
import com.tjych.swip.object.ImageModel;
import com.tjych.swip.vertical.DirectionalViewPager;
import com.tjych.swip.vertical_t.VerticalViewPager;

import java.util.List;

/**
 * Created by tjych on 2015/5/28.
 */
public class SwipAdapter extends BaseAdapter {

    private Context context;

    private List<ImageModel> imageModelList ;
    private float mLastY;
    private float mLastX;


    public SwipAdapter(Context context, List<ImageModel> imageModelList) {
        this.context = context;
        this.imageModelList = imageModelList;

    }

    @Override
    public int getCount() {
        return imageModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewhold = new ViewHold();
        try{
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.swip_item, null);
                viewhold.fl_container = (FrameLayout) convertView.findViewById(R.id.fl_container);
                //viewhold.directional_view_pager = (DirectionalViewPager) convertView.findViewById(R.id.directional_view_pager);
                viewhold.verticalViewPager = (VerticalViewPager) convertView.findViewById(R.id.directional_view_pager);
                convertView.setTag(viewhold);
            } else {
                viewhold = (ViewHold) convertView.getTag();
            }


            View[] views = new View[imageModelList.get(position).getSubImageModle().size()];
            List<ImageModel> subImageModleList = imageModelList.get(position).getSubImageModle();
            for(int i = 0 ; i < subImageModleList.size() ; i++){
                views[i] = LayoutInflater.from(context).inflate(R.layout.image_item,null);
                views[i].setBackgroundResource(subImageModleList.get(i).getImageId());
                System.out.println(subImageModleList.get(i).getImageId());
            }
            viewhold.verticalViewPager.setViews(views);
            viewhold.verticalViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHold {
        FrameLayout fl_container;
        VerticalViewPager verticalViewPager;
    }
}
