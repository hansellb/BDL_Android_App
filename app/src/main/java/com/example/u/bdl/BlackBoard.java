package com.example.u.bdl;

/**
 * Created by u on 2017-04-14.
 */

public class BlackBoard {
    private String image_url;
    private String name;
    private Float rating;

    public BlackBoard(String image_url, String name, Float rating) {
        this.image_url = image_url;
        this.name = name;
        this.rating = rating;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
