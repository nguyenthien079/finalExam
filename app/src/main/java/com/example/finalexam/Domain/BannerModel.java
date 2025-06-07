package com.example.finalexam.Domain;

import java.io.Serializable;

public class BannerModel implements Serializable {
    private String url;

    public BannerModel() {
        this.url = "";
    }

    public BannerModel(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}