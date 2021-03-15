package com.example.carpoolers.SwipeFunction;

public class Model {
    private String image;
    private String title;
    private String desc;
    private Float rating;
    private String uid;
    private Double dist;

    public Model(String image, String uid, String title, String desc, Float rating, Double dist) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.rating = rating;
        this.uid = uid;
        this.dist = dist;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public String getUid(){return uid;}

    public Double getDist(){return dist;}

    public void setDist(Double dist){this.dist = dist;}
}