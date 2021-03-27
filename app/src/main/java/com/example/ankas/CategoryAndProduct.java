package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ankas.Adapter.ComplexCategoryAdapter;
import com.example.ankas.Adapter.ComplexProductAdapter;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;

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

public class CategoryAndProduct extends AppCompatActivity {
    RecyclerView recyclerCategoryAndProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_product);
        // Получени id
        int id_ = getIntent().getIntExtra("id_", 0);
        recyclerCategoryAndProduct = findViewById(R.id.recyclerCategoryAndProduct);
        recyclerCategoryAndProduct.setLayoutManager(
                new LinearLayoutManager(CategoryAndProduct.this));
        // Запрос на сервер для получения товаров или услуг
        new getCategoryAndProduct().execute("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/" + id_);
        // Верхнее меню
        toolbar();
    }

    // Верхнее меню
    private void toolbar() {
        ImageView img_logo = findViewById(R.id.img_logo);
        ImageView img_search = findViewById(R.id.img_search);
        // Переход в главное меню
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryAndProduct.this, MainActivity.class);
                intent.putExtra("ItemFragment", R.id.item_main);
                startActivity(intent);
            }
        });
        // Переход в поиск
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryAndProduct.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // Получение товаров или категорий
    private class getCategoryAndProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                // Подключение
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // Считывание ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuffer result = new StringBuffer();
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

        // Парсинг ответа
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Проверка ответа
            if (checkResult(result)) {
                try {
                    // Парсинг ответа от сервера
                    JSONObject jsonObjectResult = new JSONObject(result);
                    String param = jsonObjectResult.getString("Param");
                    if (param.equals("Category")) {
                        // Категории товаров
                        JSONArray jsonArrayCategory = jsonObjectResult.getJSONArray("Category");
                        parseCategory(jsonArrayCategory);
                    } else if (param.equals("Product")) {
                        // Товары
                        JSONArray jsonArrayProduct = jsonObjectResult.getJSONArray("Product");
                        parseProduct(jsonArrayProduct);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера");
                Toast.makeText(CategoryAndProduct.this, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT);
            }
        }
    }

    // Парсинг категорий
    private void parseCategory(JSONArray jsonArrayCategory) throws JSONException {
        List<Category> categoryList = new ArrayList<>();
        for (int position = 0; position < jsonArrayCategory.length(); position++) {
            JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(position);
            Category category = new Category(
                    jsonObjectCategory.getInt("id_"),
                    jsonObjectCategory.getString("name"),
                    jsonObjectCategory.getString("description"),
                    jsonObjectCategory.getString("image")
            );
            categoryList.add(category);
        }
        ComplexCategoryAdapter complexCategoryAdapter =
                new ComplexCategoryAdapter(CategoryAndProduct.this, categoryList);
        recyclerCategoryAndProduct.setAdapter(complexCategoryAdapter);
    }

    // Парсинг товаров
    private void parseProduct(JSONArray jsonArrayProduct) throws JSONException {
        List<Product> productList = new ArrayList<>();
        for (int position = 0; position < jsonArrayProduct.length(); position++) {
            JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject(position);
            Product product = new Product(
                    jsonObjectProduct.getInt("id_"),
                    jsonObjectProduct.getString("name"),
                    jsonObjectProduct.getInt("price"),
                    jsonObjectProduct.getInt("quantity"),
                    jsonObjectProduct.getString("description"),
                    jsonObjectProduct.getString("brand_name"),
                    jsonObjectProduct.getString("brand_country"),
                    jsonObjectProduct.getString("name_image")
            );
            productList.add(product);
        }
        ComplexProductAdapter complexProductAdapter =
                new ComplexProductAdapter(productList, CategoryAndProduct.this);
        recyclerCategoryAndProduct.setAdapter(complexProductAdapter);
    }

    // Проверка ответа от сервера
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }

}