package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Adapter.CategoryAdapter;
import com.example.ankas.Adapter.ProductAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Fragments.MainFragment;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryAndProduct extends AppCompatActivity {
    List<Category> categoryList;
    List<Product> productList;
    ExpandableHeightGridView grid_categoryAndProduct;
    // Название
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_product);
        // Верхнее меню
        toolbar();
        grid_categoryAndProduct = findViewById(R.id.grid_categoryAndProduct);
        txt_title = findViewById(R.id.txt_title);
        int id_ = getIntent().getIntExtra("id_", 0);
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        new getCategoryAndProduct().execute("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/" + id_);
    }
    // Верхнее меню
    private void toolbar() {
        ImageView img_logo = findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryAndProduct.this, MainActivity.class);
                intent.putExtra("ItemFragment", R.id.item_main);
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
            return "null";
        }

        // Парсинг ответа
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkResult(result)) {
                try {
                    categoryList.clear();
                    productList.clear();
                    // Парсинг ответа от сервера
                    JSONObject jsonObjectResult = new JSONObject(result);
                    if (jsonObjectResult.getString("Param").equals("Category")) {
                        // Категории товаров
                        JSONArray jsonArrayCategory = jsonObjectResult.getJSONArray("Category");
                        parseCategory(jsonArrayCategory);
                    } else if (jsonObjectResult.getString("Param").equals("Product")) {
                        // Товары
                        JSONArray jsonArrayProduct = jsonObjectResult.getJSONArray("Product");
                        parseProduct(jsonArrayProduct);
                    }
                    Log.d("--- Выполнено ---", "Ответ от сервера получен (Категории)");
                    grid_categoryAndProduct.setExpanded(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера");
            }
        }
    }

    // Парсинг категорий
    private void parseCategory(JSONArray jsonArrayCategory) throws JSONException {
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
        // Усли не хватает на заполнение
        while ((categoryList.size() % 3) != 0) {
            Category category = new Category(
                    0,
                    "null",
                    "null",
                    "null"
            );
            categoryList.add(category);
        }
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, CategoryAndProduct.this);
        grid_categoryAndProduct.setAdapter(categoryAdapter);
        txt_title.setText("Категории товаров");
    }

    // Парсинг товаров
    private void parseProduct(JSONArray jsonArrayProduct) throws JSONException {
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
        // Усли не хватает на заполнение
        while ((productList.size() % 2) != 0) {
            Product product = new Product(
                    0,
                    "null",
                    0,
                    0,
                    "null",
                    "null",
                    "null",
                    "null"
            );
            productList.add(product);
        }
        ProductAdapter productAdapter = new ProductAdapter(productList, CategoryAndProduct.this);
        grid_categoryAndProduct.setAdapter(productAdapter);
        grid_categoryAndProduct.setNumColumns(2);
        grid_categoryAndProduct.setHorizontalSpacing(0);
        grid_categoryAndProduct.setVerticalSpacing(0);
        txt_title.setText("Товары");
        sortingAndFiltering(productAdapter, productList);
    }

    // Проверка ответа от сервера
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }

    // Сортировка и фильтр
    private void sortingAndFiltering(ProductAdapter productAdapter, final List<Product> productList) {
        RelativeLayout layout_sorting_and_filtering = findViewById(R.id.layout_sorting_and_filtering);
        layout_sorting_and_filtering.setVisibility(View.VISIBLE);
        TextView txt_filtering = findViewById(R.id.txt_filtering);
        TextView txt_sorting = findViewById(R.id.txt_sorting);
        txt_sorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = CategoryAndProduct.this;
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.inflate(R.menu.menu_sorting);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.txt_popularity:
                                break;
                            case R.id.txt_ascending:
                                break;
                            case R.id.txt_descending:
                                break;
                            case R.id.txt_nameCompany:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
}