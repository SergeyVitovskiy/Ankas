package com.example.ankas.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class Basket {
    private static List<Basket> basketList;

    private int id_;
    private String name;
    private String image;
    private int quantity;
    private int price;

    public Basket(int id_, String name, String image, int quantity, int price) {
        this.id_ = id_;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    public int setQuantity(int value) {
        quantity += value;
        return quantity;
    }

    // Редоктирование листа
    public static List<Basket> getBasketList() {
        return basketList;
    }

    public static void setBasketList(List<Basket> basketList) {
        Basket.basketList = basketList;
    }

    // Добавление товара в корззину
    public static void addProductBasket(int id_, String name, String image, int price) {
        Basket basket = new Basket(
                id_,
                name,
                image,
                1,
                price
        );
        basketList.add(basket);
    }

    // Получение данных из памяти
    public static void getSystemList(Context context) {
        // Отчищаем List
        basketList.clear();
        // Считываем данные
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int countItem = sharedPreferences.getInt("countItem", 0);
        for (int position = 0; position < countItem; position++) {
            Basket basket = new Basket(
                    sharedPreferences.getInt("id", 0),
                    sharedPreferences.getString("name", null),
                    sharedPreferences.getString("image", null),
                    sharedPreferences.getInt("quantity", 0),
                    sharedPreferences.getInt("price", 0)
            );
            basketList.add(basket);
        }
    }

    // Получение отдальных данных
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
