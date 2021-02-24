package com.example.ankas;

import java.util.ArrayList;
import java.util.List;

public class Basket {

    private List<Basket> basketList = new ArrayList<>();

    private int id_;
    private String nameImage;
    private String name;
    private int price;
    private int quantity;

    public Basket(int id_, String nameImage, String name, int price, int quantity) {
        this.id_ = id_;
        this.nameImage = nameImage;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Получене товаров из корзины
    public List<Basket> getBasketList() {
        return basketList;
    }

    // Добавление в корзину товаров
    public void addItemBasket(int id_, String nameImage, String name, int price, int quantity) {
        Basket basket = new Basket(id_, nameImage, name, price, quantity);
        basketList.add(basket);
    }

    // Удлаение элемента из корзины
    public void deleteItemBasket(int position) {
        basketList.remove(position);
    }

    // Получени общей стоимость товаров
    public int getSumPrice() {
        int sum = 0;
        for (int i = 0; i < basketList.size(); i++) {
            sum += basketList.get(i).price;
        }
        return sum;
    }
}
