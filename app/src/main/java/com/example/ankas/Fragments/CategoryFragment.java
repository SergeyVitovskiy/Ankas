package com.example.ankas.Fragments;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.Adapter.CategoryAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Objects.Category;
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

public class CategoryFragment extends Fragment {
    ImageView img_Loading;
    LinearLayout layout_loading;
    Context context;
    View CategoryFragmentView;
    // Категории
    List<Category> categoryList;
    ExpandableHeightGridView grid_category;
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CategoryFragmentView = inflater.inflate(R.layout.category_fragment, null);
        context = CategoryFragmentView.getContext();
        loadingRotate();
        // Категории товаров
        categoryList = new ArrayList<>();
        grid_category = CategoryFragmentView.findViewById(R.id.grid_category);
        grid_category.setExpanded(true);
        categoryAdapter = new CategoryAdapter(categoryList, context);
        grid_category.setAdapter(categoryAdapter);
        new getCategory().execute("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/0");
        // Возрат view для отрисовки
        return CategoryFragmentView;
    }

    // Получение категорий
    private class getCategory extends AsyncTask<String, Void, String> {

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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkResult(result)) {
                try {
                    categoryList.clear();
                    // Парсинг ответа от сервера
                    JSONObject jsonObjectResult = new JSONObject(result);
                    JSONArray jsonArrayCategory = jsonObjectResult.getJSONArray("Category");
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
                    Log.d("--- Выполнено ---", "Ответ от сервера получен (Категории)");
                    categoryAdapter.notifyDataSetChanged();
                    layout_loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера");
            }
        }
    }

    // Проверка ответа
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }

    // Вращение загрузки
    private void loadingRotate(){
        layout_loading = CategoryFragmentView.findViewById(R.id.layout_loading);
        img_Loading = CategoryFragmentView.findViewById(R.id.img_Loading);
        Animation rotate_center = AnimationUtils.loadAnimation(context, R.anim.rotate_center);
        img_Loading.setAnimation(rotate_center);
    }
}
