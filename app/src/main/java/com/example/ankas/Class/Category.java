package com.example.ankas.Class;

public class Category {
    int id_;
    String name;
    String description;
    String image;

    public Category(int id_, String name, String description, String image) {
        this.id_ = id_;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_() {
        return id_;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
