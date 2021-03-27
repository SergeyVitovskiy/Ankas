package com.example.ankas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.Adapter.ComplexCategoryAdapter;
import com.example.ankas.CategoryAndProduct;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Category;
import com.example.ankas.R;
import com.example.ankas.SearchActivity;

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

public class CategoryFragment extends Fragment {
    Context mContext;
    View mCategoryFragmentView;
    // Категории
    List<Category> mCategoryList;
    RecyclerView recyclerCategory;
    ComplexCategoryAdapter complexCategoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mCategoryFragmentView = inflater.inflate(R.layout.category_fragment, null);
        mContext = mCategoryFragmentView.getContext();
        // Категории товаров
        mCategoryList = new ArrayList<>();
        recyclerCategory = mCategoryFragmentView.findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(mContext));
        complexCategoryAdapter = new ComplexCategoryAdapter(mContext, mCategoryList);
        recyclerCategory.setAdapter(complexCategoryAdapter);
        // Получение категорий
        new getCategory().execute("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/0");
        // Возрат view для отрисовки
        return mCategoryFragmentView;
    }

    // Получение категорий
    private class getCategory extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
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
                    mCategoryList.clear();
                    // Парсинг ответа от сервера
                    JSONObject jsonObjectResult = new JSONObject(result);
                    JSONArray jsonArrayCategory = jsonObjectResult.getJSONArray("Category");
                    for (int position = 0; position < jsonArrayCategory.length(); position++) {
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(position);
                        // Добавление товаров в лист
                        Category category = new Category(
                                jsonObjectCategory.getInt("id_"),
                                jsonObjectCategory.getString("name"),
                                jsonObjectCategory.getString("description"),
                                jsonObjectCategory.getString("image")
                        );
                        mCategoryList.add(category);
                    }
                    // Оюновление компонентов в списке
                    complexCategoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера");
                Toast.makeText(mContext, "Не удалось получить ответ от сервера", Toast.LENGTH_SHORT);
            }
        }
    }

    // Проверка ответа
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}
