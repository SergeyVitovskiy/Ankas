package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankas.Class.Category;
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

public class MainScreen extends AppCompatActivity {

    private List<Category> categoryArrayList = new ArrayList<>();
    private List<String> banerArrayList = new ArrayList<>();

    ImageView img_baner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img_baner = findViewById(R.id.img_baner);
        // Получить баннеры
        new getBanner().execute();
        // Получение категорий
        new getCategory().execute();
    }
    // Получить баннер
    private class getBanner extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/MainScreen/Banner");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }
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
            try {
                JSONArray jsonArray = new JSONArray(result);
                for(int i = 0 ;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    banerArrayList.add(jsonObject.getString("name_image"));
                }
                Picasso.with(MainScreen.this)
                        .load("http://anndroidankas.h1n.ru/image/" + banerArrayList.get(0))
                        .into(img_baner);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    // Получить категории
    private class getCategory extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Подключение
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/0");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // Считывание ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuffer result = new StringBuffer();
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
            // Обработка ответа
            if (!result.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayCategory = jsonObject.getJSONArray("Category");
                    for (int i = 0; i < jsonArrayCategory.length(); i++) {
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(i);
                        // Запись в класс
                        Category category = new Category(
                                jsonObjectCategory.getInt("id_"),
                                jsonObjectCategory.getString("name"),
                                jsonObjectCategory.getString("description"),
                                jsonObjectCategory.getString("image")
                        );
                        categoryArrayList.add(category);
                    }
                    addCategoryMainScrean();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    // Добавлние категорий на экран
    private void addCategoryMainScrean() {
        LinearLayout layout_category = findViewById(R.id.layout_category);
        for (int item = 0; item < categoryArrayList.size(); item += 2) {
            View viewItem_category = View.inflate(this, R.layout.item_category, null);
            // Присвоение компонентов
            ImageView item_image = viewItem_category.findViewById(R.id.item_image);
            ImageView item1_image = viewItem_category.findViewById(R.id.item1_image);
            TextView txt_name = viewItem_category.findViewById(R.id.txt_name);
            TextView txt1_name = viewItem_category.findViewById(R.id.txt1_name);
            // Первыцй элемент
            Category category = categoryArrayList.get(item);
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .into(item_image);
            txt_name.setText(category.getName());
            // Обработка нажатия
            final Category finalCategory = category;
            item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, CategoryAndProductScreen.class);
                    intent.putExtra("id_item", finalCategory.getId_());
                    startActivity(intent);
                }
            });
            // Второй элемент
            category = categoryArrayList.get(item+1);
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .into(item1_image);
            txt1_name.setText(category.getName());
            // Обработка нажатия
            final Category finalCategory1 = category;
            item1_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, CategoryAndProductScreen.class);
                    intent.putExtra("id_item", finalCategory1.getId_());
                    startActivity(intent);
                }
            });
            // Вывод на экрна
            layout_category.addView(viewItem_category);
        }
    }
}