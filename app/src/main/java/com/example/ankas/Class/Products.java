package com.example.ankas.Class;

public class Products {

    int id_;
    String name;
    int price;
    int quantity;
    String brand_name;
    String brand_country;
    String name_image;

    public Products(int id_, String name, int price, int quantity, String brand_name, String brand_country, String name_image) {
        this.id_ = id_;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.brand_name = brand_name;
        this.brand_country = brand_country;
        this.name_image = name_image;
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getBrand_country() {
        return brand_country;
    }

    public void setBrand_country(String brand_country) {
        this.brand_country = brand_country;
    }

    public String getName_image() {
        return name_image;
    }

    public void setName_image(String name_image) {
        this.name_image = name_image;
    }
}
