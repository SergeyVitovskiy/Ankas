package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Adapter.CharacteristicAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Components.MySliderImage;
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
import java.util.Timer;
import java.util.TimerTask;

public class ProductActivity extends AppCompatActivity {
    MySliderImage slider_image;
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
        // Получени id
        int id_ = getIntent().getIntExtra("Id_", 0);
        // Получение изображения
        new getImage().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Image/" + id_);
        // Получение информации о товаре
        new getInfoProduct().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Product/" + id_);
        // Получение характеристики
        new getSpecifications().execute("http://anndroidankas.h1n.ru/mobile-api/Product/Characteristics/" + id_);
        // Получение и показ основной ифнормации о товаре
        // Получение значений с предыдущей формы
        String name = getIntent().getStringExtra("Name");
        int quantity = getIntent().getIntExtra("Quantity", 0);
        int price = getIntent().getIntExtra("Price", 0);
        String description = getIntent().getStringExtra("Description");
        String name_image = getIntent().getStringExtra("Name_image");
        List<String> image = new ArrayList<>();
        image.add(name_image);
        // Обьявление компонентов
        declaringComponents();
        // Вывод информации
        slider_image.setListImage(image);
        txt_name.setText(name);
        txt_quantity.setText(quantity + "шт.");
        txt_price.setText("Цена: " + setPrice(price));
        txt_idProduct.setText("Код товара: " + id_);
        txt_brand.setText(getIntent().getStringExtra("Brand_name")
                + ", " + getIntent().getStringExtra("Brand_country"));
        // Есть ли товар в корзине
        if (Basket.checkProductBasket(id_))
            btn_by.setText("В корзине");
        else
            btn_by.setText("Купить");
        // Описание товара
        if (description != null && !description.equals(""))
            txt_description.setText(Html.fromHtml(description));
        // Верхнее меню
        toolbar();
        // Нижнее меню
        bottomMenu();
        // Сообщение об ошибке
        messageError();
    }

    // Обьявление компонентов
    private void declaringComponents() {
        slider_image = findViewById(R.id.slider_image);
        txt_name = findViewById(R.id.txt_name);
        txt_quantity = findViewById(R.id.txt_quantity);
        txt_price = findViewById(R.id.txt_price);
        txt_idProduct = findViewById(R.id.txt_idProduct);
        txt_brand = findViewById(R.id.txt_brand);
        txt_description = findViewById(R.id.txt_description);
        grid_specifications = findViewById(R.id.grid_specifications);
        btn_by = findViewById(R.id.btn_by);
    }

    // Верхнее меню
    private void toolbar() {
        ImageView img_logo = findViewById(R.id.img_logo);
        ImageView img_search = findViewById(R.id.img_search);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, MainActivity.class);
                intent.putExtra("ItemFragment", R.id.item_main);
                startActivity(intent);
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // Изображения
    private class getImage extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Получение ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
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
            // Парсинг ответа
            if (checkResult(result)) {
                try {
                    List<String> listImage = new ArrayList<>();
                    JSONArray jsonArray = new JSONArray(result);
                    for (int position = 0; position < jsonArray.length(); position++) {
                        JSONObject jsonObjectImage = jsonArray.getJSONObject(position);
                        String nameImage = jsonObjectImage.getString("name_image");
                        listImage.add(nameImage);
                    }
                    slider_image.setListImage(listImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Получение информации о товаре
    private class getInfoProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                // Считывание ответа
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
            return "null";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
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
            if (checkResult(result)) {
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

    // Сообщение об ошибке
    private void messageError() {
        TextView txt_messageError = findViewById(R.id.txt_messageError);
        txt_messageError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                final View viewItem = View.inflate(ProductActivity.this, R.layout.dialog_error_message, null);
                builder.setView(viewItem);
                final Dialog dialog = builder.create();
                // Отправить запрос
                Button btn_message_dialog = viewItem.findViewById(R.id.btn_message_dialog);
                btn_message_dialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView txt_mail = viewItem.findViewById(R.id.txt_mail);
                        TextView txt_message = viewItem.findViewById(R.id.txt_message);
                        String mail = txt_mail.getText().toString();
                        String message = txt_message.getText().toString();
                        int check = 0;
                        if (mail.length() > 6 && mail.matches("[a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{1,5}"))
                            check++;
                        else {
                            // Ошибка mail
                            Toast.makeText(ProductActivity.this, "Некорректный E-mail", Toast.LENGTH_LONG).show();
                        }
                        if (message.length() >= 10) {
                            check++;
                        } else {
                            // шибка сообщение
                            Toast.makeText(ProductActivity.this, "Опишите ошибку (мин. 10 символов)", Toast.LENGTH_LONG).show();
                        }
                        if (check >= 2) {
                            // Закрыть диалог
                            dialog.cancel();
                            dialogPushMessageError();
                        }
                    }
                });
                // Закрыть окно
                Button btn_cancle = viewItem.findViewById(R.id.btn_cancle);
                btn_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                // Вывод окна
                dialog.show();
            }
        });

    }

    // Диалог оформления заказа
    private void dialogPushMessageError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewItem = View.inflate(this, R.layout.dialog_loading_order, null);
        TextView txt_name = viewItem.findViewById(R.id.txt_name);
        ImageView img_ico = viewItem.findViewById(R.id.img_ico);
        txt_name.setText("Отправка...");
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
        img_ico.setAnimation(animation);
        builder.setView(viewItem);
        final Dialog dialog = builder.create();
        dialog.show();
        final int[] tick = {0};
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tick[0]++;
                        if (tick[0] >= 2) {
                            dialog.cancel();
                            timer.cancel();
                            Toast.makeText(ProductActivity.this, "Сообщение отправлено", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    // Проверка ответа
    private boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }

    // Нижнее меню
    private void bottomMenu() {
        // Банки
        ImageView image_applePay = findViewById(R.id.image_applePay);
        image_applePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apple.com/ru/apple-pay/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_googlePay = findViewById(R.id.image_googlePay);
        image_googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pay.google.com/intl/ru_ru/about/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_mastercard = findViewById(R.id.image_mastercard);
        image_mastercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mastercard.ru/ru-ru.html"));
                startActivity(browserIntent);
            }
        });
        ImageView iamge_visa = findViewById(R.id.iamge_visa);
        iamge_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.visa.com.ru/"));
                startActivity(browserIntent);
            }
        });
        // Соц сетия
        ImageView image_VK = findViewById(R.id.image_VK);
        image_VK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/ankas_ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_YouTube = findViewById(R.id.image_YouTube);
        image_YouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Inst = findViewById(R.id.image_Inst);
        image_Inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ankas.ru/?hl=ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Facebook = findViewById(R.id.image_Facebook);
        image_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ankas.ru/"));
                startActivity(browserIntent);
            }
        });
    }
}