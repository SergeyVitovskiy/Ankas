package com.example.ankas.Objects;

public class Basket {
    int id_;
    String name;
    String image;
    int quantity;
    int price;

    public Basket(int id_, String name, String image, int quantity, int price) {
        this.id_ = id_;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public int setQuantity(int value){
        quantity += value;
        return quantity;
    }

    public int getId_() {
        return id_;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
