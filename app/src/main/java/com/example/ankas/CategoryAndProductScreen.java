package com.example.ankas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankas.Class.Category;
import com.example.ankas.Class.Products;
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

public class CategoryAndProductScreen extends AppCompatActivity {

    // Иерархия переходов
    TextView txt_hierarchy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_and_product);
        // Получить категорию с другой формы
        int id_ = getIntent().getIntExtra("id_item", 0);
        // Получить и вывести иерархию переводов
        txt_hierarchy = findViewById(R.id.txt_hierarchy);
        txt_hierarchy.setText(getIntent().getStringExtra("hierarchy"));
        // Если не передалось значение
        if (id_ == 0) {
            Intent intent = new Intent(CategoryAndProductScreen.this, MainScreen.class);
            startActivity(intent);
        }
        // Запрос товаров или категорий
        new getCategoryAndProduct().execute(String.valueOf(id_));
        // Кнопки верхнего меню
        toolBarBtn();
    }

    // Получение товаров или категорий
    private class getCategoryAndProduct extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://anndroidankas.h1n.ru/mobile-api/Product/ProductOrCategory/" + strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                // Получение значений
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuffer result = new StringBuffer();
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
            if (!result.equals("")) {
                List<Category> categoriesArrayList = new ArrayList<>();
                List<Products> productsArrayList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    // Категории
                    if (jsonObject.getString("Param").equals("Category")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("Category");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectCategory = jsonArray.getJSONObject(i);
                            Category category = new Category(
                                    jsonObjectCategory.getInt("id_"),
                                    jsonObjectCategory.getString("name"),
                                    jsonObjectCategory.getString("description"),
                                    jsonObjectCategory.getString("image"));
                            categoriesArrayList.add(category);
                        }
                        addCategoryMainScrean(categoriesArrayList);
                    }
                    // Товары
                    else {
                        JSONArray jsonArray = jsonObject.getJSONArray("Product");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectProduct = jsonArray.getJSONObject(i);
                            Products products = new Products(
                                    jsonObjectProduct.getInt("id_"),
                                    jsonObjectProduct.getString("name"),
                                    jsonObjectProduct.getInt("price"),
                                    jsonObjectProduct.getInt("quantity"),
                                    jsonObjectProduct.getString("brand_name"),
                                    jsonObjectProduct.getString("brand_country"),
                                    jsonObjectProduct.getString("name_image")
                            );
                            productsArrayList.add(products);
                        }
                        addProductMainScreen(productsArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Добавлние категорий на экран
    private void addCategoryMainScrean(List<Category> categoryArrayList) {
        LinearLayout layout_items = findViewById(R.id.layout_items);
        for (int item = 0; item < categoryArrayList.size(); item += 2) {
            View viewItem_category = View.inflate(this, R.layout.item_category, null);
            // Присвоение компонентов
            ImageView item_image = viewItem_category.findViewById(R.id.item_image);
            ImageView item1_image = viewItem_category.findViewById(R.id.item1_image);
            TextView txt_name = viewItem_category.findViewById(R.id.txt_name);
            TextView txt1_name = viewItem_category.findViewById(R.id.txt1_name);
            // Первыцй элемент
            final Category category = categoryArrayList.get(item);
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .placeholder(R.drawable.ico_small)
                    .error(R.drawable.ico_small)
                    .into(item_image);
            txt_name.setText(category.getName());
            // Обработка нажатия
            item_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CategoryAndProductScreen.this, CategoryAndProductScreen.class);
                    intent.putExtra("id_item", category.getId_());
                    intent.putExtra("hierarchy", txt_hierarchy.getText().toString() + " > " + category.getName());
                    startActivity(intent);
                }
            });
            // Второй элемент
            if (categoryArrayList.size() > item + 1) {
                final Category category1 = categoryArrayList.get(item + 1);
                Picasso.with(this)
                        .load("http://anndroidankas.h1n.ru/image/" + category1.getImage())
                        .into(item1_image);
                txt1_name.setText(category1.getName());
                // Обработка нажатия
                item1_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CategoryAndProductScreen.this, CategoryAndProductScreen.class);
                        intent.putExtra("id_item", category1.getId_());
                        intent.putExtra("hierarchy", txt_hierarchy.getText().toString() + " > " + category1.getName());
                        startActivity(intent);
                    }
                });
            }
            // Если нет элемента
            else {
                item1_image.setImageResource(R.drawable.white_bg);
                txt1_name.setText("");
            }
            // Вывод на экрна
            layout_items.addView(viewItem_category);
        }
    }

    // Добавление товаров на экран
    private void addProductMainScreen(List<Products> productsArrayList) {
        LinearLayout layout_items = findViewById(R.id.layout_items);
        for (int item = 0; item < productsArrayList.size(); item += 2) {
            View viewItem_product = View.inflate(this, R.layout.item_product, null);
            // Обьявление компонетов
            TextView txt_name = viewItem_product.findViewById(R.id.txt_name);
            TextView txt1_name = viewItem_product.findViewById(R.id.txt_name);
            ImageView img_item = viewItem_product.findViewById(R.id.img_item);
            ImageView img1_item = viewItem_product.findViewById(R.id.img1_item);
            TextView txt_price = viewItem_product.findViewById(R.id.txt_price);
            TextView txt1_price = viewItem_product.findViewById(R.id.txt1_price);
            Button btn_by = viewItem_product.findViewById(R.id.btn_by);
            Button btn1_by = viewItem_product.findViewById(R.id.btn1_by);
            TextView txt_brand = viewItem_product.findViewById(R.id.txt_brand);
            TextView txt1_brand = viewItem_product.findViewById(R.id.txt1_brand);
            TextView txt_nal = viewItem_product.findViewById(R.id.txt_nal);
            TextView txt1_nal = viewItem_product.findViewById(R.id.txt1_nal);
            // Присвоение значений компанентам
            // Первый элемент
            final Products products = productsArrayList.get(item);
            txt_name.setText(products.getName());
            Picasso.with(this)
                    .load("http://anndroidankas.h1n.ru/image/" + products.getName_image())
                    .error(R.drawable.ico_small)
                    .into(img_item);
            // Кореция вывод цены
            StringBuffer price = new StringBuffer(products.getPrice() + " ₽");
            if (price.length() >= 6)
                price = price.insert((price.length() - 5), " ");
            txt_price.setText(price);
            txt_brand.setText(products.getBrand_name() + "," + products.getBrand_country());
            if (products.getQuantity() >= 1)
                txt_nal.setText("В наличии");
            else
                txt_nal.setText("Под заказ");
            img_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CategoryAndProductScreen.this, ProductScreen.class);
                    intent.putExtra("id_item",products.getId_());
                    startActivity(intent);
                }
            });
            btn_by.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBasket();
                    addBasket(products.getId_());
                }
            });
            // Второй элемент
            if (productsArrayList.size() > item + 1) {
                final Products products1 = productsArrayList.get(item + 1);
                txt1_name.setText(products1.getName());
                Picasso.with(this)
                        .load("http://anndroidankas.h1n.ru/image/" + products1.getName_image())
                        .into(img1_item);
                price = new StringBuffer(products1.getPrice() + " ₽");
                if (price.length() >= 6)
                    price = price.insert((price.length() - 5), " ");
                txt1_price.setText(price);
                txt1_brand.setText(products1.getBrand_name() + "," + products1.getBrand_country());
                if (products1.getQuantity() >= 1)
                    txt1_nal.setText("В наличии");
                else
                    txt1_nal.setText("Под заказ");
                img1_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CategoryAndProductScreen.this, ProductScreen.class);
                        intent.putExtra("id_item",products.getId_());
                        startActivity(intent);
                    }
                });
                layout_items.addView(viewItem_product);
                btn1_by.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBasket();
                        addBasket(products1.getId_());
                    }
                });
            }
        }
    }

    // Добавление товара в корзину
    private void addBasket(int id_) {
        Log.d("Products: ", "product add Basket. id: " + id_);
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
                Intent intent = new Intent(CategoryAndProductScreen.this, BasketScreen.class);
                startActivity(intent);
            }
        });
        // Показать диалог
        dialog.show();
    }

    // Кнопки верхнего меню
    private void toolBarBtn(){
        ImageView btn_basket = findViewById(R.id.btn_basket);
        btn_basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoryAndProductScreen.this, BasketScreen.class);
                startActivity(intent);
            }
        });
    }
}