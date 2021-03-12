package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.Adapter.CharacteristicAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Characteristic;
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

public class ProductActivity extends AppCompatActivity {
    TextView txt_name;
    TextView txt_quantity;
    TextView txt_price;
    TextView txt_idProduct;
    TextView txt_brand;
    TextView txt_description;
    Button btn_by;
    ExpandableHeightGridView grid_specifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        // Обьявление компонентов
        txt_name = findViewById(R.id.txt_name);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_price = findViewById(R.id.txt_price);
        txt_idProduct = findViewById(R.id.txt_idProduct);
        txt_brand = findViewById(R.id.txt_brand);
        txt_description = findViewById(R.id.txt_description);
        grid_specifications = findViewById(R.id.grid_specifications);
        btn_by = findViewById(R.id.btn_by);
        // Получение значений с предыдущей формы
        int id_ = getIntent().getIntExtra("Id_", 0);
        String name = getIntent().getStringExtra("Name");
        int quantity = getIntent().getIntExtra("Quantity", 0);
        int price = getIntent().getIntExtra("Price", 0);
        String description = getIntent().getStringExtra("Description");
        String name_image = getIntent().getStringExtra("Name_image");
        // Вывод информации
        txt_name.setText(name);
        txt_quantity.setText(quantity + "шт.");
        txt_price.setText(setPrice(price));
        txt_idProduct.setText("Код товара: " + id_);
        txt_brand.setText(getIntent().getStringExtra("Brand_name")
                + ", " + getIntent().getStringExtra("Brand_country"));
        // Описание товара
        if (!description.equals(null) && !description.equals(""))
            txt_description.setText(Html.fromHtml(description));
        // Получение характеристики
        new getSpecifications().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Characteristics/" + id_);
        // Кнопка покупки товара
        btn_by(id_, name, name_image, price);
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
            try {
                List<Characteristic> characteristicList = new ArrayList<>();
                JSONArray jsonArraySpecifications = new JSONArray(result);
                for (int position = 0; position < jsonArraySpecifications.length(); position++) {
                    JSONObject jsonObjectCharacteristic
                            = jsonArraySpecifications.getJSONObject(position);
                    Characteristic characteristic = new Characteristic(
                            jsonObjectCharacteristic.getString("name"),
                            jsonObjectCharacteristic.getString("characteristic")
                    );
                    characteristicList.add(characteristic);
                }
                CharacteristicAdapter characteristicAdapter =
                        new CharacteristicAdapter(characteristicList, ProductActivity.this);
                grid_specifications.setExpanded(true);
                grid_specifications.setAdapter(characteristicAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Диалоговое окно покупки товаров
    private void btn_by(final int id, final String name, final String image, final int price) {
        // Кнопка покупки
        btn_by.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Добавление товара в корзину
                Basket.addProductBasket(id, name, image, price);
                final Context context = ProductActivity.this;
                // Создание и присвоение макета к диалогу
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View viewItemDialog = View.inflate(context, R.layout.dialog_by, null);
                builder.setView(viewItemDialog);
                final Dialog dialogBy = builder.create();
                TextView txt_name_dialog = viewItemDialog.findViewById(R.id.txt_name_dialog);
                ImageView image_dialog = viewItemDialog.findViewById(R.id.image_dialog);
                // Вывод данных
                txt_name_dialog.setText(name);
                Picasso.get().load("http://anndroidankas.h1n.ru/image/" + image)
                        .placeholder(R.drawable.ico_small)
                        .into(image_dialog);
                // Продолжить покупки
                Button btn_resume_dialog = viewItemDialog.findViewById(R.id.btn_resume_dialog);
                btn_resume_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBy.cancel();
                    }
                });
                // Перейти к оформлению
                Button btn_arrange_dialog = viewItemDialog.findViewById(R.id.btn_arrange_dialog);
                btn_arrange_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBy.cancel();
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("ItemFragment", R.id.item_basket);
                        startActivity(intent);
                    }
                });
                // Вывод диалога
                dialogBy.show();
            }
        });
    }

    // конвертация цена
    private String setPrice(int price) {
        StringBuffer newPrice = new StringBuffer(String.valueOf(price) + " ₽");
        int position = 5;
        while (newPrice.length() > position) {
            newPrice = newPrice.insert((newPrice.length() - position), " ");
            position += 4;
        }
        return newPrice.toString();
    }
}