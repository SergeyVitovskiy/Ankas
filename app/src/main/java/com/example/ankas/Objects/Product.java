package com.example.ankas.Objects;

public class Product {

    int id_;
    String name;
    int price;
    int quantity;
    String brand_name;
    String brand_country;
    String name_image;

    public Product(int id_, String name, int price, int quantity, String brand_name, String brand_country, String name_image) {
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

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public String getBrand_country() {
        return brand_country;
    }

    public String getName_image() {
        return name_image;
    }
}
