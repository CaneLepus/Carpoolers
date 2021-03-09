package com.example.carpoolers.SwipeFunction;

public class Model {
    private int image;
    private String title;
    private String desc;
    private Float rating;

    public Model(int image, String title, String desc, Float rating) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getRating(){return rating;}
}