package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Adapter.CharacteristicAdapter;
import com.example.ankas.Adapter.ComplexProductActivityAdapter;
import com.example.ankas.Adapter.ComplexProductAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Components.MySliderImage;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Characteristic;
import com.example.ankas.Objects.Product;
import com.squareup.picasso.Picasso;

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
import java.util.Timer;
import java.util.TimerTask;

public class ProductActivity extends AppCompatActivity {
    ComplexProductActivityAdapter complexProductActivityAdapter;
    RecyclerView recyclerProduct;

    Product mProduct;
    List<String> mListImage;
    List<Characteristic> mListCharacteristic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // Обьявление массивов
        mListImage = new ArrayList<>();
        mListCharacteristic = new ArrayList<>();
        // Получение информации с другой формы и добавление как обьект
        int id_ = getIntent().getIntExtra("Id_", 0);
        String name = getIntent().getStringExtra("Name");
        int quantity = getIntent().getIntExtra("Quantity", 0);
        int price = getIntent().getIntExtra("Price", 0);
        String description = getIntent().getStringExtra("Description");
        String name_image = getIntent().getStringExtra("Name_image");
        String brand_name = getIntent().getStringExtra("Brand_name");
        String brand_country = getIntent().getStringExtra("Brand_country");
        mProduct = new Product(id_, name, price, quantity, description, brand_name, brand_country, name_image);
        recyclerProduct = findViewById(R.id.recyclerProduct);
        // Обьявление и присвоение компонента
        recyclerProduct.setLayoutManager(new LinearLayoutManager(this));
        complexProductActivityAdapter =
                new ComplexProductActivityAdapter(this, mProduct, mListImage, mListCharacteristic);
        recyclerProduct.setAdapter(complexProductActivityAdapter);
        // Получение основной информации из сети
        // Получение изображения
        new getImage().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Image/" + id_);
        // Получение информации о товаре
        new getInfoProduct().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Product/" + id_);
        // Получение характеристики
        new getSpecifications().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Characteristics/" + id_);
        // Верхнее меню
        toolbar();
    }

    // Изображения
    private class getImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Получение ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                    result.append(line);
                // Отправка на парсинг
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Парсинг ответа
            if (checkResult(result)) {
                try {
                    mListImage.clear();
                    List<String> listImage = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int position = 0; position < jsonArray.length(); position++) {
                        JSONObject jsonObjectImage = jsonArray.getJSONObject(position);
                        String nameImage = jsonObjectImage.getString("name_image");
                        mListImage.add(nameImage);
                    }
                    complexProductActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Получение информации о товаре
    private class getInfoProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Считывание ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                    result.append(line);
                // Отправка на парсинг
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkResult(result)) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObjectProduct = jsonArray.getJSONObject(0);
                    // Заись данных
                    int id_ = jsonObjectProduct.getInt("id_");
                    String name = jsonObjectProduct.getString("name");
                    int price = jsonObjectProduct.getInt("price");
                    int quantity = jsonObjectProduct.getInt("quantity");
                    String description = jsonObjectProduct.getString("description");
                    String brand_name = jsonObjectProduct.getString("brand_name");
                    String brand_country = jsonObjectProduct.getString("brand_country");
                    String name_image = jsonObjectProduct.getString("name_image");
                    mProduct = new Product(id_, name, price, quantity, description,
                            brand_name, brand_country, name_image);
                    // Отправка в адаптер
                    complexProductActivityAdapter.setProduct(mProduct);
                    complexProductActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Получить характеристики
    private class getSpecifications extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                // Параметры подключения
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Получение ответа
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                // Отправка на парсинг
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "null";
        }

        // Парсинг ответа от сервера
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkResult(result)) {
                try {
                    mListCharacteristic.clear();
                    JSONArray jsonArraySpecifications = new JSONArray(result);
                    for (int position = 0; position < jsonArraySpecifications.length(); position++) {
                        JSONObject jsonObjectCharacteristic
                                = jsonArraySpecifications.getJSONObject(position);
                        Characteristic characteristic = new Characteristic(
                                jsonObjectCharacteristic.getString("name"),
                                jsonObjectCharacteristic.getString("characteristic")
                        );
                        mListCharacteristic.add(characteristic);
                    }
                    // Отправка в адаптер
                    complexProductActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Верхнее меню
    private void toolbar() {
        ImageView img_logo = findViewById(R.id.img_logo);
        ImageView img_search = findViewById(R.id.img_search);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                intent.putExtra("ItemFragment", R.id.item_main);
                startActivity(intent);
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // Проверка ответа
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}