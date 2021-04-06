package com.example.ankas.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.Adapter.ComplexMainActivityAdapter;
import com.example.ankas.Adapter.ComplexProductAdapter;
import com.example.ankas.CategoryAndProduct;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;
import com.example.ankas.R;

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

public class MainFragment extends Fragment {
    Context mContext;
    View MainFragmentView;
    // Конетент
    RecyclerView recyclerMain;
    ComplexMainActivityAdapter complexMainActivityAdapter;
    // Массивы
    List<String> mImageList;
    List<Category> mPopularCategory;
    static List<Product> mProductList;

    int MIN_PRODUCT = 0;
    int MAX_PRODUCT = 21;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainFragmentView = inflater.inflate(R.layout.main_fragment, null);
        mContext = MainFragmentView.getContext();
        // Обьявление массивов
        mImageList = new ArrayList<>();
        mPopularCategory = new ArrayList<>();
        mProductList = new ArrayList<>();
        // Обьявление контента
        recyclerMain = MainFragmentView.findViewById(R.id.recyclerMain);
        recyclerMain.setLayoutManager(new LinearLayoutManager(mContext));
        complexMainActivityAdapter = new ComplexMainActivityAdapter(mImageList, mPopularCategory, mProductList, mContext);
        recyclerMain.setAdapter(complexMainActivityAdapter);
        // Баннер
        new getImageBanner().execute("http://anndroidankas.h1n.ru/mobile-api/MainScreen/Banner");
        // Популярные категории
        new getPopularCategory().execute("http://anndroidankas.h1n.ru/mobile-api/MainScreen/PopularCategories");
        // Популярные товары
        new getPopularProduct().execute("http://anndroidankas.h1n.ru/mobile-api/MainScreen/PopularProduct/" + MIN_PRODUCT + "/" + MAX_PRODUCT);
        // Возрат view для отрисовки
        return MainFragmentView;
    }

    // Баннер
    private class getImageBanner extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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
                    JSONArray jsonArray = new JSONArray(result);
                    for (int position = 0; position < jsonArray.length(); position++) {
                        JSONObject jsonObjectImage = jsonArray.getJSONObject(position);
                        String nameImage = jsonObjectImage.getString("name_image");
                        mImageList.add(nameImage);
                    }
                    complexMainActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера.");
                Toast.makeText(mContext, "Не удалось получить ответ от сервера.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Получение популярных категорий категорий
    private class getPopularCategory extends AsyncTask<String, Void, String> {

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
            // Проверка ответа
            if (checkResult(result)) {
                try {
                    mPopularCategory.clear();
                    // Парсинг ответа от сервера
                    JSONArray jsonArrayCategory = new JSONArray(result);
                    for (int position = 0; position < jsonArrayCategory.length(); position++) {
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(position);
                        // Добавление товаров в лист
                        Category category = new Category(
                                jsonObjectCategory.getInt("id_"),
                                jsonObjectCategory.getString("name"),
                                jsonObjectCategory.getString("description"),
                                jsonObjectCategory.getString("image")
                        );
                        mPopularCategory.add(category);
                    }
                    // Оюновление компонентов в списке
                    complexMainActivityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера.");
                Toast.makeText(mContext, "Не удалось получить ответ от сервера.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Получение популярных товаров
    private class getPopularProduct extends AsyncTask<String, Void, String> {

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
            // Проверка ответа
            if (checkResult(result)) {
                try {
                    mProductList.clear();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayProduct = jsonObject.getJSONArray("Product");
                    for (int position = 0; position < jsonArrayProduct.length(); position++) {
                        JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject(position);
                        Product product = new Product(
                                jsonObjectProduct.getInt("id_"),
                                jsonObjectProduct.getString("name"),
                                jsonObjectProduct.getInt("price"),
                                jsonObjectProduct.getInt("quantity"),
                                null,
                                jsonObjectProduct.getString("brand_name"),
                                jsonObjectProduct.getString("brand_country"),
                                jsonObjectProduct.getString("name_image")
                        );
                        mProductList.add(product);
                    }
                    complexMainActivityAdapter.notifyDataSetChanged();
                    complexMainActivityAdapter.setProductList();
                    Log.d("Запрос товаров :", "Запрос выполнен");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера.");
                Toast.makeText(mContext, "Не удалось получить ответ от сервера.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Проверка ответа
    private static boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}
