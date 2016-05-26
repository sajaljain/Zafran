package com.monkporter.zafran.model;

import com.monkporter.zafran.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sajal on 26-May-16.
 */
public class Products {
    private String name;
    private String desc;
    private int thumbnail;


    public Products(String name, String desc, int id) {
    this.name = name;
        this.desc = desc;
        this.thumbnail = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
