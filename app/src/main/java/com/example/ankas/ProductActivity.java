package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.example.ankas.Adapter.CharacteristicAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Objects.Characteristic;

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
        // Получение id
        int id_ = getIntent().getIntExtra("Id_", 0);
        // Вывод информации
        txt_name.setText(getIntent().getStringExtra("Name"));
        txt_quantity.setText((getIntent().getIntExtra("Quantity", 0) + "шт."));
        txt_price.setText(setPrice(getIntent().getIntExtra("Price", 0)));
        txt_idProduct.setText("Код товара: " + id_);
        txt_brand.setText(getIntent().getStringExtra("Brand_name")
                + ", " + getIntent().getStringExtra("Brand_country"));
        String description = getIntent().getStringExtra("Description");
        if (!description.equals(null) && !description.equals(""))
            txt_description.setText(Html.fromHtml(description));
        new getSpecifications().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Characteristics/" + id_);
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