package com.tjych.swip.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tjych.swip.R;
import com.tjych.swip.swip.SwipeFlingAdapterView;
import com.tjych.swip.adapter.SwipAdapter;
import com.tjych.swip.object.ImageModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements SwipeFlingAdapterView.onFlingListener{

    private int[] PIC_DATA = {R.drawable.pic_01,R.drawable.pic_02,R.drawable.pic_03,R.drawable.pic_04,R.drawable.pic_05,R.drawable.pic_06};
    private int [][] PIC_DATA_ARRAY = new int[][]{
            {R.drawable.bg_01_01,R.drawable.bg_01_02,R.drawable.bg_01_03,R.drawable.bg_01_04,R.drawable.bg_01_05,R.drawable.bg_01_06},
            {R.drawable.bg_02_01,R.drawable.bg_02_02,R.drawable.bg_02_03,R.drawable.bg_02_04},
            {R.drawable.bg_03_01,R.drawable.bg_03_02,R.drawable.bg_03_03,R.drawable.bg_03_04},
            {R.drawable.bg_04_01,R.drawable.bg_04_02,R.drawable.bg_04_03,R.drawable.bg_04_04,R.drawable.bg_04_05}
    };


    private SwipeFlingAdapterView flingContainer;
    private ArrayList<ImageModel> imageModelList;
    private SwipAdapter swipAdapter;
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.init();
    }

    private void init() {
        this.flingContainer = (SwipeFlingAdapterView) findViewById(R.id.sw_filing_view);
        imageModelList = new ArrayList<ImageModel>();
        ImageModel imageModel = new ImageModel();
        imageModel.setId(1);
        imageModel.setImageId(R.drawable.pic_01);
        imageModel.setTitle("");


        List<ImageModel> imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[0].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(j);
            subImageModel.setImageId(PIC_DATA_ARRAY[0][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);



        imageModel = new ImageModel();
        imageModel.setId(2);
        imageModel.setImageId(R.drawable.pic_02);
        imageModel.setTitle("");
        imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[1].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(i);
            subImageModel.setImageId(PIC_DATA_ARRAY[1][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setId(3);
        imageModel.setImageId(R.drawable.pic_03);
        imageModel.setTitle("");
        imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[2].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(j);
            subImageModel.setImageId(PIC_DATA_ARRAY[2][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setId(4);
        imageModel.setImageId(R.drawable.pic_04);
        imageModel.setTitle("");
        imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[3].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(j);
            subImageModel.setImageId(PIC_DATA_ARRAY[3][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);


        imageModel = new ImageModel();
        imageModel.setId(5);
        imageModel.setImageId(R.drawable.pic_05);
        imageModel.setTitle("");
        imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[0].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(i);
            subImageModel.setImageId(PIC_DATA_ARRAY[0][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);

        imageModel = new ImageModel();
        imageModel.setId(6);
        imageModel.setImageId(R.drawable.pic_06);
        imageModel.setTitle("");
        imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[1].length; j++){
            ImageModel subImageModel = new ImageModel();
            subImageModel.setId(j);
            subImageModel.setImageId(PIC_DATA_ARRAY[1][j]);
            subImageModel.setTitle("");
            imageModelListSub.add(subImageModel);
        }
        imageModel.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel);

        swipAdapter = new SwipAdapter(this,imageModelList);
        flingContainer.init(this,swipAdapter);

        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(MainActivity.this, "Click!");
            }
        });
    }


    @Override
    public void removeFirstObjectInAdapter() {
        // this is the simplest way to delete an object from the Adapter (/AdapterView)
        Log.d("LIST", "removed object!");
        imageModelList.remove(0);
        swipAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLeftCardExit(Object dataObject) {
        //Do something on the left!
        //You also have access to the original object.
        //If you want to use it just cast it (String) dataObject
        makeToast(MainActivity.this, "Left!");
    }

    @Override
    public void onRightCardExit(Object dataObject) {
        makeToast(MainActivity.this, "Right!");
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {
        // Ask for more data here
        ImageModel imageModel1 = new ImageModel();
        imageModel1.setId(i);
        imageModel1.setTitle("");
        imageModel1.setImageId(PIC_DATA[i%6]);
        List<ImageModel> imageModelListSub = new ArrayList<ImageModel>();
        for(int j = 0 ; j < PIC_DATA_ARRAY[i%4].length; j++){
            ImageModel imageModelSub = new ImageModel();
            imageModelSub.setId(j);
            imageModelSub.setImageId(PIC_DATA_ARRAY[i%4][j]);
            imageModelSub.setTitle("");
            imageModelListSub.add(imageModelSub);
        }
        imageModel1.setSubImageModle(imageModelListSub);
        imageModelList.add(imageModel1);
        swipAdapter.notifyDataSetChanged();
        Log.d("LIST", "notified");
        i++;
    }

    @Override
    public void onScroll(float scrollProgressPercent) {
        View view = flingContainer.getSelectedView();
        View leftView, rightView;
        if (view != null) {
            leftView = view.findViewById(R.id.item_swipe_right_indicator);
            rightView = view.findViewById(R.id.item_swipe_left_indicator);
            if (leftView != null) {
                leftView.setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
            }
            if (rightView != null) {
                rightView.setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        }
    }

    int j = 0 ;

    @Override
    public void onAboveCard(Object dataObject) {
        makeToast(MainActivity.this, "Above");

    }

    @Override
    public void onBelowCard(Object dataObject) {
        makeToast(MainActivity.this, "below");
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}
