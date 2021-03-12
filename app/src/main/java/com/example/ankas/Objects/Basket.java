package com.example.ankas.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class Basket {
    private static List<Basket> basketList;

    private int id_;
    private String name;
    private String image;
    private int quantity;
    private int price;

    // Иницелизация данных корзины
    public Basket(int id_, String name, String image, int quantity, int price) {
        this.id_ = id_;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
    }

    // Редоктирование листа
    public static List<Basket> getBasketList() {
        return basketList;
    }

    // Добавление товара в корззину
    public static void addProductBasket(Context context, int id_, String name, String image, int price) {
        Basket basketItem = new Basket(
                id_,
                name,
                image,
                1,
                price
        );
        basketList.add(basketItem);
        int i = basketList.size();
        saveSystemList(context);
    }

    public static int getSizeBasket() {
        return basketList.size();
    }

    // Сохранение данных в память
    public static void saveSystemList(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = getSizeBasket();
        editor.putInt("countItem", count).apply();
        for (int position = 0; position < count; position++) {
            Basket basket = getBasketList().get(position);
            editor.putInt("id" + position, basket.id_).apply();
            editor.putString("name" + position, basket.getName()).apply();
            editor.putString("image" + position, basket.getImage()).apply();
            editor.putInt("quantity" + position, basket.getQuantity()).apply();
            editor.putInt("price" + position, basket.getPrice()).apply();
        }
    }

    // Получение данных из памяти
    public static void getSystemList(Context context) {
        basketList = new ArrayList<>();
        // Отчищаем List
        basketList.clear();
        // Считываем данные
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int countItem = sharedPreferences.getInt("countItem", 0);
        for (int position = 0; position < countItem; position++) {
            Basket basket = new Basket(
                    sharedPreferences.getInt("id" + position, 0),
                    sharedPreferences.getString("name" + position, null),
                    sharedPreferences.getString("image" + position, null),
                    sharedPreferences.getInt("quantity" + position, 0),
                    sharedPreferences.getInt("price" + position, 0)
            );
            basketList.add(basket);
        }
    }

    // Удаление элемента
    public static void deleteItemBasket(Context context, int position) {
        basketList.remove(position);
        saveSystemList(context);
    }

    // Установка кол-во товара
    public int setQuantity(Context context, int value) {
        quantity += value;
        saveSystemList(context);
        return quantity;
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

    public int getSumPrice() {
        int sum = price * quantity;
        return sum;
    }
}
