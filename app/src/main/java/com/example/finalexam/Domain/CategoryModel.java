package com.example.finalexam.Domain;


import java.io.Serializable;

public class CategoryModel implements Serializable {
    private String title;
    private int id;

    public CategoryModel() {
        this.title = "";
        this.id = 0;
    }

    public CategoryModel(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
