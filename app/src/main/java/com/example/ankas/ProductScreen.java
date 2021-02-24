package com.example.ankas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankas.Adapter.ImageProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_screen);
        int idProduct = getIntent().getIntExtra("id_item", 0);
        new getProduct().execute(String.valueOf(idProduct));
        new getCharacteristic().execute(String.valueOf(idProduct));
        new getImage().execute(String.valueOf(idProduct));
        // Кнопки верхнего меню
        toolBarBtn();
        // Кнопка купить
        Button btn_by = findViewById(R.id.btn_by);
        btn_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBasket();
            }
        });
    }

    // Информация о товаре
    private class getProduct extends AsyncTask<String, Void, String> {

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
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObjectProduct = jsonArray.getJSONObject(0);
                TextView txt_name = findViewById(R.id.txt_name);
                TextView txt_codProduct = findViewById(R.id.txt_codProduct);
                TextView txt_price = findViewById(R.id.txt_price);
                TextView txt_brand = findViewById(R.id.txt_brand);
                HtmlTextView txt_description = findViewById(R.id.txt_description);
                // Корректирование описания
                String description = jsonObjectProduct.getString("description");
                // Есть ли описание у товара
                if (description.equals("")) {
                    LinearLayout layout_description = findViewById(R.id.layout_description);
                    layout_description.setVisibility(View.GONE);
                } else {
                    description = description
                            .replaceAll("<span style=\\\"font-weight:bold\\\">", "<h6>")
                            .replaceAll("</span>", "</h6>");
                }
                // Кореция вывод цены
                StringBuffer price = new StringBuffer(jsonObjectProduct.getString("price") + " ₽");
                if (price.length() >= 6)
                    price = price.insert((price.length() - 5), " ");
                if (price.length() >= 9)
                    price = price.insert((price.length() - 9), " ");
                txt_name.setText(jsonObjectProduct.getString("name"));
                txt_codProduct.setText("код на сайте: " + jsonObjectProduct.getString("id_"));
                txt_price.setText(price);
                txt_brand.setText(jsonObjectProduct.getString("brand_name") + "," + jsonObjectProduct.getString("brand_country"));
                txt_description.setHtml(description, new HtmlHttpImageGetter(txt_description));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // Изображения
    private class getImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/Image/" + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // Считывание
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            List<String> imageList = new ArrayList<>();
            if (!result.equals("")) {
                try {
                    JSONArray jsonArrayImage = new JSONArray(result);
                    for (int i = 0; i < jsonArrayImage.length(); i++) {
                        JSONObject jsonObject = jsonArrayImage.getJSONObject(i);
                        imageList.add(jsonObject.getString("name_image"));
                        addImage(imageList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Добавление изображений
    private void addImage(List<String> imageList) {
        final AdapterViewFlipper imageFlipper = findViewById(R.id.imageFlipper);
        ImageProductAdapter imageProductAdapter = new ImageProductAdapter(imageList, ProductScreen.this);
        imageFlipper.setAdapter(imageProductAdapter);
        // Кнопки перелистывания изображений
        ImageView image_left = findViewById(R.id.image_left);
        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFlipper.showPrevious();
            }
        });
        ImageView image_right = findViewById(R.id.image_right);
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageFlipper.showNext();
            }
        });
        if (imageFlipper.getCount() <= 1) {
            image_left.setVisibility(View.GONE);
            image_right.setVisibility(View.GONE);
        }
    }

    // Характеристики
    private class getCharacteristic extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/Characteristics/" + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // Считывание
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
            if(!result.equals("")) {
                try {
                    LinearLayout layout_characteristic = findViewById(R.id.layout_characteristic);
                    // Вывод информации
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        View item_characteristics = View.inflate(ProductScreen.this, R.layout.item_characteristics, null);
                        TextView txt_name_characteristics = item_characteristics.findViewById(R.id.txt_name_characteristics);
                        TextView txt_characteristics = item_characteristics.findViewById(R.id.txt_characteristics);
                        txt_name_characteristics.setText(jsonObject.getString("name"));
                        txt_characteristics.setText(jsonObject.getString("characteristic"));
                        layout_characteristic.addView(item_characteristics);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                LinearLayout layout_characteristics = findViewById(R.id.layout_characteristics);
                layout_characteristics.setVisibility(View.GONE);
            }
        }
    }

    // Дилог при нажатии 'купить'
    private void dialogBasket() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View viewDialog = View.inflate(this, R.layout.dialog_basket, null);
        // Обьявление компонетов
        Button btn_basket = viewDialog.findViewById(R.id.btn_basket);
        Button btn_close = viewDialog.findViewById(R.id.btn_close);
        dialogBuilder.setView(viewDialog);
        final Dialog dialog = dialogBuilder.create();
        // Присвоение кнопок
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        btn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                Intent intent = new Intent(ProductScreen.this, BasketScreen.class);
                startActivity(intent);
            }
        });
        // Показать диалог
        dialog.show();
    }

    // Кнопки верхнего меню
    private void toolBarBtn() {
        ImageView btn_basket = findViewById(R.id.btn_basket);
        btn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductScreen.this, BasketScreen.class);
                startActivity(intent);
            }
        });
    }
}