package com.example.ankas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainFragment extends Fragment {
    Context context;
    View MainFragmentView;
    // Категории
    List<Category> categoryList;
    ExpandableHeightGridView grid_category;
    CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainFragmentView = inflater.inflate(R.layout.main_fragment, null);
        context = MainFragmentView.getContext();
        // Баннер

        // Категории товаров
        categoryList = new ArrayList<>();
        grid_category = MainFragmentView.findViewById(R.id.grid_category);
        grid_category.setExpanded(true);
        categoryAdapter = new CategoryAdapter(categoryList, context);
        grid_category.setAdapter(categoryAdapter);
        new getCategory().execute("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/0");
        // Обратный звонок
        call();
        // Возрат view для отрисовки
        return MainFragmentView;
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
                    for (int position = 0; position < 8; position++) {
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(position);
                        Category category = new Category(
                                jsonObjectCategory.getInt("id_"),
                                jsonObjectCategory.getString("name"),
                                jsonObjectCategory.getString("description"),
                                jsonObjectCategory.getString("image")
                        );
                        categoryList.add(category);
                    }
                    Category category = new Category(
                            0,
                            "Просмотреть все категории",
                            null,
                            "point.jpg"
                    );
                    categoryList.add(category);
                    Log.d("--- Выполнено ---", "Ответ от сервера получен (Категории)");
                    categoryAdapter.notifyDataSetChanged();
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

    // Звонки
    private void call() {
        TextView txt_tell = MainFragmentView.findViewById(R.id.txt_tell);
        TextView txt_callBack = MainFragmentView.findViewById(R.id.txt_callBack);
        // Позвонить в компанию
        txt_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        // Заказать обратный звонок
        txt_callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View viewItemCall = View.inflate(context, R.layout.dialog_call_back, null);
                builder.setView(viewItemCall);
                Dialog dialogCallBack = builder.create();
                EditText txt_name = viewItemCall.findViewById(R.id.txt_name);
                EditText txt_tell = viewItemCall.findViewById(R.id.txt_tell);
                Button btn_arrange_dialog = viewItemCall.findViewById(R.id.btn_arrange_dialog);
                String name = txt_name.getText().toString();
                String tell = txt_tell.getText().toString();
                int check = 0;
                // Проверка имени
                if (name.length() > 2) {
                    check++;
                } else {
                    Toast.makeText(context, "Заполните имя", Toast.LENGTH_LONG).show();
                }
                // Проверка телефона
                if (tell.length() > 3){
                    check++;
                } else {
                    Toast.makeText(context, "Заполните номер телефона", Toast.LENGTH_LONG).show();
                }
                // Отправка данных на сервер
                if (check == 2){
                    Toast.makeText(context, "Запрос в обработке, ожидайте звонка", Toast.LENGTH_LONG).show();
                }
                dialogCallBack.show();
            }
        });
    }
}
