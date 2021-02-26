package com.example.ankas.Class;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Basket {


    private static List<Basket> basketList = new ArrayList<>();

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
    // Считывание из памяти товаров
    public static void getProductSystem(){

    }
    // Получение кол-во товаров из корзины
    public static int getCountBasket(){
        int count = 0;
        try {
            count = basketList.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }
    // Получене товаров из корзины
    public static List<Basket> getBasketList() {
        return basketList;
    }

    // Добавление в корзину товаров
    public static void addItemBasket(int id_) {
        new getProduct().execute(String.valueOf(id_));
    }

    // Удлаение элемента из корзины
    public static void deleteItemBasket(int position) {
        basketList.remove(position);
    }

    // Получени общей стоимость товаров
    public static int getSumPrice() {
        int sum = 0;
        for (int i = 0; i < basketList.size(); i++) {
            sum += basketList.get(i).price;
        }
        return sum;
    }

    private static class getProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/Product/" + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                    result.append(line);
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("")) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObjectProduct = jsonArray.getJSONObject(0);
                    int id_ = jsonObjectProduct.getInt("id_");
                    String nameImage = jsonObjectProduct.getString("");
                    String name = jsonObjectProduct.getString("name");
                    int price = jsonObjectProduct.getInt("price");
                    Basket basket = new Basket(id_, nameImage, name, price, 1);
                    basketList.add(basket);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
