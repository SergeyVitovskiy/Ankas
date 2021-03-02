package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterViewFlipper;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Adapter.FlipperBannerAdapter;
import com.example.ankas.Class.Basket;
import com.example.ankas.Class.Category;
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

public class MainScreen extends AppCompatActivity {

    private List<Category> categoryArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Звноки
        call();
        // Получить баннеры
        new getBanner().execute();
        // Получение категорий
        new getCategory().execute();
        // Кнопки верхнего меню
        toolBarBtn();
        // Кнопки нижнего меню
        bottomMenu();
    }

    // Звонки
    private void call() {
        // Позвонить компании
        TextView txt_tell = findViewById(R.id.txt_tell);
        txt_tell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+73517514012"));
                startActivity(intent);
            }
        });
        // Заказать обрытный звонок
        TextView txt_callBack = findViewById(R.id.txt_callBack);
        txt_callBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Создание диалога
                final Context context = MainScreen.this;
                View viewItem = View.inflate(context, R.layout.dialog_call_back, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(viewItem);
                final Dialog dialog = builder.create();
                // Обьявление компонентов диалога
                TextView call = viewItem.findViewById(R.id.call);
                final EditText eText_phone = findViewById(R.id.eText_phone);
                TextView dialog_cancle = viewItem.findViewById(R.id.dialog_cancle);
                // Запросить обрытный звноко
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!eText_phone.getText().toString().equals("") &&
                                eText_phone.getText().toString().length() >=5) {
                            dialog.cancel();
                            Toast.makeText(context, "Заявка оформлена, оператор позвонит в течении 15 минут", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Неверно указан номер телефона", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                // Закрыть диалого
                dialog_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    // Получить баннер
    private class getBanner extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/MainScreen/Banner");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

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
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                // Созлание и заполние листа с названиями изображений
                List<String> bannerArrayList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    bannerArrayList.add(jsonObject.getString("name_image"));
                }
                banner(bannerArrayList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Получить категории
    private class getCategory extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Подключение
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/0");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Обработка ответа
            if (!result.equals("")) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayCategory = jsonObject.getJSONArray("Category");
                    for (int i = 0; i < jsonArrayCategory.length(); i++) {
                        JSONObject jsonObjectCategory = jsonArrayCategory.getJSONObject(i);
                        // Запись в класс
                        Category category = new Category(
                                jsonObjectCategory.getInt("id_"),
                                jsonObjectCategory.getString("name"),
                                jsonObjectCategory.getString("description"),
                                jsonObjectCategory.getString("image")
                        );
                        categoryArrayList.add(category);
                    }
                    addCategoryMainScrean();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Добавлние категорий на экран
    private void addCategoryMainScrean() {
        LinearLayout layout_category = findViewById(R.id.layout_category);
        for (int item = 0; item < categoryArrayList.size(); item += 2) {
            View viewItem_category = View.inflate(this, R.layout.item_category, null);
            // Присвоение компонентов
            ImageView item_image = viewItem_category.findViewById(R.id.item_image);
            ImageView item1_image = viewItem_category.findViewById(R.id.item1_image);
            TextView txt_name = viewItem_category.findViewById(R.id.txt_name);
            TextView txt1_name = viewItem_category.findViewById(R.id.txt1_name);
            // Первыцй элемент
            Category category = categoryArrayList.get(item);
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .error(R.drawable.ico_small)
                    .into(item_image);
            txt_name.setText(category.getName());
            // Обработка нажатия
            final Category finalCategory = category;
            item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, CategoryAndProductScreen.class);
                    intent.putExtra("id_item", finalCategory.getId_());
                    intent.putExtra("hierarchy", finalCategory.getName());
                    startActivity(intent);
                }
            });
            // Второй элемент
            category = categoryArrayList.get(item + 1);
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .error(R.drawable.ico_small)
                    .into(item1_image);
            txt1_name.setText(category.getName());
            // Обработка нажатия
            final Category finalCategory1 = category;
            item1_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, CategoryAndProductScreen.class);
                    intent.putExtra("id_item", finalCategory1.getId_());
                    intent.putExtra("hierarchy", finalCategory1.getName());
                    startActivity(intent);
                }
            });
            // Вывод на экрна
            layout_category.addView(viewItem_category);
        }
    }

    // Кнопки верхнего меню
    private void toolBarBtn() {
        ImageView btn_basket = findViewById(R.id.btn_basket);
        btn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, BasketScreen.class);
                startActivity(intent);
            }
        });
        TextView txt_countBasket = findViewById(R.id.txt_countBasket);
        txt_countBasket.setText(String.valueOf(Basket.getCountBasketSystem(MainScreen.this)));
    }

    // Банер
    private void banner(List<String> bannerArrayList) {
        // Вывод изображений на экран
        final AdapterViewFlipper img_banner = findViewById(R.id.img_banner);
        img_banner.setAdapter(new FlipperBannerAdapter(MainScreen.this, bannerArrayList));
        img_banner.setFlipInterval(5000);
        img_banner.startFlipping();
        ImageView btn_bannerRight = findViewById(R.id.btn_bannerRight);
        ImageView btn_bannerLeft = findViewById(R.id.btn_bannerLeft);
        // Дальше
        btn_bannerRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_banner.showNext();
            }
        });
        // Вернуться назад
        btn_bannerLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_banner.showPrevious();
            }
        });
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