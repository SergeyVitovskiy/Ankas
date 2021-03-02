package com.example.ankas.Class;

import android.content.Context;
import android.content.SharedPreferences;
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

    private static Context mContext;
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
    public static void getProductSystem(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int basketCount = sharedPreferences.getInt("BasketCount", 0);
        for (int position = 0; position < basketCount; position++) {
            int id_ = sharedPreferences.getInt(("id" + position), 0);
            if (id_ != 0)
                addItemBasket(id_, context);
        }
    }

    public static void setContext(Context context) {
        mContext = context;
    }

    // Запись в память
    public static void setProductSystem(Context context) {
        SharedPreferences.Editor sharedPreferencesEditor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        int basketCount = getCountBasket();
        sharedPreferencesEditor.putInt("BasketCount", basketCount);
        for (int position = 0; position < basketCount; position++) {
            sharedPreferencesEditor.putInt(("id" + position), getBasketList().get(position).id_).commit();
        }
    }

    // получение кол-ва товара из памяти
    public static int getCountBasketSystem(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("BasketCount", 0);
    }

    // Есть ли товар в корзине
    public static boolean checkBasket(int id_) {
        for (int i = 0; i < basketList.size(); i++) {
            if (basketList.get(i).id_ == id_)
                return true;
        }
        return false;
    }

    // Получение кол-во товаров из корзины
    public static int getCountBasket() {
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
    public static void addItemBasket(int id_, Context context) {
        new getProduct().execute(String.valueOf(id_));
        setProductSystem(context);
    }

    // Удлаение элемента из корзины
    public static void deleteItemBasket(int position) {
        basketList.remove(position);
    }

    // Получени общей стоимость товаров
    public static int getSumPrice() {
        int sum = 0;
        for (int i = 0; i < basketList.size(); i++) {
            sum += basketList.get(i).price * basketList.get(i).quantity;
        }
        return sum;
    }

    // Получить общую стоимость товара
    public static String getSumProduct(int position) {
        StringBuffer price = new StringBuffer((basketList.get(position).getPrice() *
                basketList.get(position).getQuantity())
                + " ₽");
        if (price.length() >= 5)
            price = price.insert((price.length() - 5), " ");
        if (price.length() >= 9)
            price = price.insert((price.length() - 9), " ");
        return price.toString();
    }

    // Получение информации о товаре из сети
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
                    String nameImage = jsonObjectProduct.getString("name_image");
                    String name = jsonObjectProduct.getString("name");
                    int price = jsonObjectProduct.getInt("price");
                    Basket basket = new Basket(id_, nameImage, name, price, 1);
                    basketList.add(basket);
                    setProductSystem(mContext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Установка кол-ва товара
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static void setQuantityProduct(int position, int quantity) {
        basketList.get(position).setQuantity(
                basketList.get(position).getQuantity() + quantity
        );
    }

    public int getId_() {
        return id_;
    }

    public String getNameImage() {
        return nameImage;
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
}
