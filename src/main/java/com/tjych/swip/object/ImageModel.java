package com.tjych.swip.object;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tjych on 2015/5/28.
 */
public class ImageModel implements Serializable {

    private int id;

    private int imageId;

    private String title;

    private List<ImageModel> subImageModle = new ArrayList<ImageModel>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ImageModel> getSubImageModle() {
        return subImageModle;
    }

    public void setSubImageModle(List<ImageModel> subImageModle) {
        this.subImageModle = subImageModle;
    }

}
