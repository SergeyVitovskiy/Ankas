package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Class.Basket;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BasketScreen extends AppCompatActivity {
    LinearLayout linear_basket;
    // Текстовые поля доставки
    EditText txt_address;
    EditText txt_details_address;
    EditText txt_surname;
    EditText txt_name;
    EditText txt_mail;
    EditText txt_tell;
    EditText txt_note;
    // Тип доставки
    String delivery = "pickup";
    // Диалог загрузки
    Dialog dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_screen);
        // Обьявление компонетнов
        linear_basket = findViewById(R.id.linear_basket);
        txt_address = findViewById(R.id.txt_address);
        txt_details_address = findViewById(R.id.txt_details_address);
        txt_surname = (EditText) findViewById(R.id.txt_surname);
        txt_name = (EditText) findViewById(R.id.txt_name);
        txt_mail = (EditText) findViewById(R.id.txt_mail);
        txt_tell = (EditText) findViewById(R.id.txt_tell);
        txt_note = (EditText) findViewById(R.id.txt_note);
        // Заполнение полей из память
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        txt_address.setText(sharedPreferences.getString("address", ""));
        txt_details_address.setText(sharedPreferences.getString("details_address", ""));
        txt_surname.setText(sharedPreferences.getString("surname", ""));
        txt_name.setText(sharedPreferences.getString("name", ""));
        txt_mail.setText(sharedPreferences.getString("mail", ""));
        txt_tell.setText(sharedPreferences.getString("tell", ""));
        // Кнопки получений товара
        btnDelivery();
        // Перечисление элементов
        basketItem();
        // Верхнее меню
        toolBarBtn();
        // Кнопки нижнего меню
        bottomMenu();
        // Создание заказа
        createOrder();
    }

    // Обновление элементов корзины
    private void basketItem() {
        // Удаление всех компонентов
        linear_basket.removeAllViews();
        // Добвление компонентов
        if (Basket.getCountBasket() != 0) {
            for (int i = 0; i < Basket.getCountBasket(); i++) {
                addBasketItem(i);
            }
        } else {
            LinearLayout layout_loading = findViewById(R.id.layout_loading);
            layout_loading.setVisibility(View.VISIBLE);
        }
    }

    // Добавление элемента
    private void addBasketItem(final int position) {
        // Общая стоимость товаров
        final TextView txt_sumProduct = findViewById(R.id.txt_sumProduct);
        final TextView txt_sum = findViewById(R.id.txt_sum);
        txt_sum.setText("Общая сумма товаров: " + replacePrice(String.valueOf(Basket.getSumPrice())));
        txt_sumProduct.setText("Итог: " + replacePrice(String.valueOf(Basket.getSumPrice())));
        View viewItemBasket = View.inflate(this, R.layout.item_basket, null);
        // Присвоение компонентов
        ImageView image = viewItemBasket.findViewById(R.id.image);
        TextView txt_name = viewItemBasket.findViewById(R.id.txt_name);
        TextView txt_price = viewItemBasket.findViewById(R.id.txt_price);
        final TextView txt_quantity = viewItemBasket.findViewById(R.id.txt_quantity);
        TextView txt_plus = viewItemBasket.findViewById(R.id.txt_plus);
        TextView txt_minus = viewItemBasket.findViewById(R.id.txt_minus);
        final TextView txt_sumPrice = viewItemBasket.findViewById(R.id.txt_sumPrice);
        // Вывод ифнормации
        final Basket basket = Basket.getBasketList().get(position);
        txt_name.setText(basket.getName());
        // Установка цены
        txt_price.setText(replacePrice(String.valueOf(basket.getPrice())));
        // Получение изображения
        Picasso.with(BasketScreen.this)
                .load("http://anndroidankas.h1n.ru/image/" + basket.getNameImage())
                .error(R.drawable.ico_small)
                .into(image);
        // Вывод кол-во товара
        txt_quantity.setText(String.valueOf(basket.getQuantity()));
        // Выбор кол-во товара
        txt_sumPrice.setText("Общая стоимость товара: " + Basket.getSumProduct(position));
        txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.setQuantityProduct(position, +1);
                Basket.setProductSystem(BasketScreen.this);
                txt_quantity.setText(String.valueOf(Basket.getBasketList().get(position).getQuantity()));
                txt_sumPrice.setText("Общая стоимость товара: " + Basket.getSumProduct(position));
                updateCount();
                // Общая стоимость товаров
                txt_sumProduct.setText("Общая стоимость заказа: " + replacePrice(String.valueOf(Basket.getSumPrice())));
                txt_sum.setText("Итор: " + replacePrice(String.valueOf(Basket.getSumPrice())));
            }
        });
        txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.setQuantityProduct(position, -1);
                if (Basket.getBasketList().get(position).getQuantity() == 0) {
                    Basket.deleteItemBasket(position);
                    Basket.setProductSystem(BasketScreen.this);
                    basketItem();
                    toolBarBtn();
                } else {
                    txt_quantity.setText(String.valueOf(Basket.getBasketList().get(position).getQuantity()));
                    txt_sumPrice.setText("Общая стоимость товара: " + Basket.getSumProduct(position));
                }
                Basket.setProductSystem(BasketScreen.this);
                updateCount();
                // Общая стоимость товаров
                txt_sumProduct.setText("Общая стоимость заказа: " + replacePrice(String.valueOf(Basket.getSumPrice())));
                txt_sum.setText("Итор: " + replacePrice(String.valueOf(Basket.getSumPrice())));
            }
        });
        // Переход к товару
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketScreen.this, ProductScreen.class);
                intent.putExtra("id_item", Basket.getBasketList().get(position).getId_());
                startActivity(intent);
            }
        });
        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketScreen.this, ProductScreen.class);
                intent.putExtra("id_item", Basket.getBasketList().get(position).getId_());
                startActivity(intent);
            }
        });
        linear_basket.addView(viewItemBasket);
    }

    // Обновление кол-ва товара на других формах
    private void updateCount() {
        MainScreen.updateCountBasket();
    }

    // Обновление общей цены товаров
    private String replacePrice(String price) {
        StringBuffer priceNew = new StringBuffer(price + " ₽");
        int count = 5;
        while (priceNew.length() > count) {
            priceNew = priceNew.insert((priceNew.length() - count), " ");
            count = count + 4;
        }
        return priceNew.toString();
    }

    // Кнопки получения товара
    private void btnDelivery() {
        final LinearLayout layout_delivery = findViewById(R.id.layout_delivery);
        final LinearLayout layout_delivery_maps = findViewById(R.id.layout_delivery_maps);
        // Кнопки выбора
        final LinearLayout linear_pickup = findViewById(R.id.linear_pickup);
        final LinearLayout layout_courier = findViewById(R.id.layout_courier);
        // Изображения
        final ImageView image_pickup = findViewById(R.id.image_pickup);
        final ImageView image_courier = findViewById(R.id.image_courier);
        // Самовывоз
        linear_pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_pickup.setBackgroundResource(R.drawable.border_green);
                layout_courier.setBackgroundResource(R.drawable.border_text_gray);
                delivery = "pickup";
                layout_delivery.setVisibility(View.GONE);
                layout_delivery_maps.setVisibility(View.VISIBLE);
                // Установка изображений
                image_pickup.setImageResource(R.drawable.point);
                image_courier.setImageResource(R.drawable.plane_true);
            }
        });
        // Доставка
        layout_courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_pickup.setBackgroundResource(R.drawable.border_text_gray);
                layout_courier.setBackgroundResource(R.drawable.border_green);
                delivery = "courier";
                layout_delivery.setVisibility(View.VISIBLE);
                layout_delivery_maps.setVisibility(View.GONE);
                // Установка изображений
                image_pickup.setImageResource(R.drawable.point_true);
                image_courier.setImageResource(R.drawable.plane);
            }
        });
    }

    // Создание заказа
    private void createOrder() {
        // Присвоение компонентов
        Button btn_order = findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Basket.getCountBasket() > 0) {
                    // Проверка ввода значений
                    int checkEditText = 0;
                    int maxCheckEditText = 4;
                    // Имя
                    if (!txt_name.getText().toString().equals("")) {
                        txt_name.setBackgroundResource(R.drawable.border_text_gray);
                        checkEditText++;
                    } else txt_name.setBackgroundResource(R.drawable.border_read);
                    // Фамилия
                    if (!txt_surname.getText().toString().equals("")) {
                        txt_surname.setBackgroundResource(R.drawable.border_text_gray);
                        checkEditText++;
                    } else txt_surname.setBackgroundResource(R.drawable.border_read);
                    // mail
                    if (!txt_mail.getText().toString().equals("") && txt_mail.getText().toString().matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")) {
                        txt_mail.setBackgroundResource(R.drawable.border_text_gray);
                        checkEditText++;
                    } else txt_mail.setBackgroundResource(R.drawable.border_read);
                    // Телефон
                    if (!txt_tell.getText().toString().equals("")) {
                        txt_tell.setBackgroundResource(R.drawable.border_text_gray);
                        checkEditText++;
                    } else txt_tell.setBackgroundResource(R.drawable.border_read);
                    // Доставка
                    if (delivery.equals("courier")) {
                        maxCheckEditText = 6;
                        // Адрес
                        if (!txt_address.getText().toString().equals("")) {
                            txt_address.setBackgroundResource(R.drawable.border_text_gray);
                            checkEditText++;
                        } else txt_address.setBackgroundResource(R.drawable.border_read);
                        // Дополнение к адресу
                        if (!txt_details_address.getText().toString().equals("")) {
                            txt_details_address.setBackgroundResource(R.drawable.border_text_gray);
                            checkEditText++;
                        } else txt_details_address.setBackgroundResource(R.drawable.border_read);
                    } else {
                        maxCheckEditText = 4;
                    }
                    // Отправка значений
                    if (checkEditText >= maxCheckEditText) {
                        // Сохранение данных пользователей
                        SharedPreferences.Editor sharedPreferencesEditor = getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
                        sharedPreferencesEditor.putString("name", txt_name.getText().toString()).commit();
                        sharedPreferencesEditor.putString("mail", txt_mail.getText().toString()).commit();
                        sharedPreferencesEditor.putString("surname", txt_surname.getText().toString()).commit();
                        sharedPreferencesEditor.putString("tell", txt_tell.getText().toString()).commit();
                        sharedPreferencesEditor.putString("surname", txt_surname.getText().toString()).commit();
                        sharedPreferencesEditor.putString("details_address", txt_details_address.getText().toString()).commit();
                        sharedPreferencesEditor.putString("address", txt_address.getText().toString()).commit();
                        new pushClient().execute(
                                txt_name.getText().toString(),
                                txt_surname.getText().toString(),
                                txt_tell.getText().toString(),
                                txt_mail.getText().toString(),
                                txt_note.getText().toString(),
                                delivery,
                                txt_address.getText().toString(),
                                txt_details_address.getText().toString());
                        // Показ диалога
                        dialogLoading();
                    } else
                        Toast.makeText(BasketScreen.this,
                                "Не все поля заполнены корректно", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(BasketScreen.this, "Ваша корзина пуста", Toast.LENGTH_LONG);
                }
            }
        });

    }

    // Отправка данных пользователя
    private class pushClient extends AsyncTask<String, Void, String> {

        String note;
        String receiving;
        String address;
        String details_address;

        @Override
        protected String doInBackground(String... strings) {
            try {
                note = strings[4];
                receiving = strings[5];
                address = strings[6];
                details_address = strings[7];
                // Передаваемые параметры
                Uri.Builder builderQuery = new Uri.Builder()
                        .appendQueryParameter("name", strings[0])
                        .appendQueryParameter("surname", strings[1])
                        .appendQueryParameter("telephone", strings[2])
                        .appendQueryParameter("mail", strings[3]);
                // Параметры подключений
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Order/Client");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                // Отправка значений
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(builderQuery.build().getEncodedQuery());
                writer.flush();
                writer.close();
                connection.connect();
                // Считывание значений
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!result.equals("null")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int client_id = jsonObject.getInt("id_");
                    if (receiving.equals("pickup"))
                        receiving = "Самовывоз";
                    else
                        receiving = "Доставка";
                    new pushOrder().execute(String.valueOf(client_id), note, receiving, address, details_address);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Создание заказа
    private class pushOrder extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                // Передаваемые параметры
                Uri.Builder builderQuery = new Uri.Builder()
                        .appendQueryParameter("id_client", strings[0])
                        .appendQueryParameter("note", strings[1])
                        .appendQueryParameter("receiving", strings[2])
                        .appendQueryParameter("address", strings[3])
                        .appendQueryParameter("details_address", strings[4]);
                // Параметры подключений
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Order/Orders");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();
                // Отправка значений
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(builderQuery.build().getEncodedQuery());
                writer.flush();
                writer.close();
                // Считывание значений
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (!result.equals("null")) {
                    JSONObject jsonObject = new JSONObject(result);
                    int id_ = jsonObject.getInt("id_");
                    // отправка товаров
                    for (int i = 0; i < Basket.getCountBasket(); i++) {
                        new pushProduct().execute(String.valueOf(id_),
                                String.valueOf(Basket.getBasketList().get(i).getId_()),
                                String.valueOf(Basket.getBasketList().get(i).getQuantity()));
                    }
                    // Удаление элеменетов из корзины
                    Basket.clearList();
                    basketItem();
                    Basket.setProductSystem(BasketScreen.this);
                    dialogLoading.cancel();
                    Toast.makeText(BasketScreen.this, "Заказ оформлен. Ожидайте звонка оператора", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Отправка товаров
    private class pushProduct extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                // Передаваемые параметры
                Uri.Builder builderQuery = new Uri.Builder()
                        .appendQueryParameter("id_orders", strings[0])
                        .appendQueryParameter("id_product", strings[1])
                        .appendQueryParameter("quantity", strings[2]);
                // Параметры подключений
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Order/OrderedProducts");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();
                // Отправка значений
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(connection.getOutputStream()));
                writer.write(builderQuery.build().getEncodedQuery());
                writer.flush();
                writer.close();
                // Считывание значений
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                StringBuffer result = new StringBuffer();
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

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                Log.d("Basket", "Push product basket: "
                        + jsonObject.getString("Answer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Кнопки верхнего меню
    private void toolBarBtn() {
        ImageView img_logo = findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketScreen.this, MainScreen.class);
                startActivity(intent);
            }
        });
        TextView txt_countBasket = findViewById(R.id.txt_countBasket);
        txt_countBasket.setText(String.valueOf(Basket.getCountBasket()));
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

    private void dialogLoading() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BasketScreen.this);
        View view = View.inflate(BasketScreen.this, R.layout.dialog_loading, null);
        builder.setView(view);
        dialogLoading = builder.create();
        dialogLoading.show();
    }

}