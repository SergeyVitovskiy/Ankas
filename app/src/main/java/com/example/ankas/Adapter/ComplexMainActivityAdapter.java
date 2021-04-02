package com.example.ankas.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.Components.MySliderImage;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;
import com.example.ankas.ProductActivity;
import com.example.ankas.R;
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

public class ComplexMainActivityAdapter extends RecyclerView.Adapter<ComplexMainActivityAdapter.ItemHolder> {
    // Звонки
    private final int TYPE_ITEM_CALL = 0;
    // Баннер
    private final int TYPE_ITEM_BANNER = 1;
    // Категории (Название)
    private final int TYPE_ITEM_TITLE_POPULAR_CATEGORY = 2;
    // Категории
    private final int TYPE_ITEM_POPULAR_CATEGORY = 3;
    // Товары (Название)
    private final int TYPE_ITEM_TITLE_POPULAR_PRODUCT = 4;
    // Товары
    private final int TYPE_ITEM_POPULAR_PRODUCT = 5;
    // Товары
    private final int TYPE_ITEM_POPULAR_PRODUCT_BIG = 6;

    List<String> mImageList;
    List<Category> mPopularCategory;
    List<Product> mProductList;
    Context mContext;
    List<Integer> idConclusionProduct;
    int idProduct = 0;
    int position = 2;

    int MIN_PRODUCT = 0;
    int MAX_PRODUCT = 21;

    // Добавление популярных товаров
    public void setProductList() {
        // Вычисление кол-во элементов для вывода на экран
        idConclusionProduct.add(idProduct);
        while (idProduct < this.mProductList.size()) {
            idProduct += 3;
            if (position % 3 != 0) {
                idConclusionProduct.add(idProduct);
            } else {
                idConclusionProduct.add(idProduct);
                idProduct -= 2;
            }
            position++;
        }
    }

    public ComplexMainActivityAdapter(List<String> mImageList, List<Category> mPopularCategory, List<Product> mProductList, Context mContext) {
        idConclusionProduct = new ArrayList<>();
        this.mImageList = mImageList;
        this.mPopularCategory = mPopularCategory;
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ComplexMainActivityAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM_CALL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item_call_back, parent, false);
                break;
            case TYPE_ITEM_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_image, parent, false);
                break;
            case TYPE_ITEM_TITLE_POPULAR_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
            case TYPE_ITEM_POPULAR_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_slider, parent, false);
                break;
            case TYPE_ITEM_TITLE_POPULAR_PRODUCT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
            case TYPE_ITEM_POPULAR_PRODUCT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
                break;
            case TYPE_ITEM_POPULAR_PRODUCT_BIG:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_main, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
        }
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ComplexMainActivityAdapter.ItemHolder holder, int position) {
        int type = getItemViewType(position);
        // Подгрузка
        if (position + 10 == getItemCount()) {
            MIN_PRODUCT = MAX_PRODUCT;
            MAX_PRODUCT += 21;
            Log.d("Получение: ", "Подгрузка, позиция: " + position);
            new getPopularProduct().execute("http://anndroidankas.h1n.ru/mobile-api/MainScreen/PopularProduct/" + MIN_PRODUCT + "/" + MAX_PRODUCT);
        }
        switch (type) {
            // Обратный звонок
            case TYPE_ITEM_CALL: {
                // Телефон
                holder.txt_tell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:+73517514012"));
                        mContext.startActivity(intent);
                    }
                });
                // Заказать обратный звонок
                holder.txt_callBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        View viewDialog = View.inflate(mContext, R.layout.dialog_call_back, null);
                        final LinearLayout layout_name = viewDialog.findViewById(R.id.layout_name);
                        final LinearLayout layout_tell = viewDialog.findViewById(R.id.layout_tell);
                        final TextView txt_name = viewDialog.findViewById(R.id.txt_name);
                        final TextView txt_tell = viewDialog.findViewById(R.id.txt_tell);
                        Button btn_call_back = viewDialog.findViewById(R.id.btn_call_back);
                        Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                        builder.setView(viewDialog);
                        final Dialog dialog = builder.create();
                        // Заказать звонок
                        btn_call_back.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String name = txt_name.getText().toString();
                                String tell = txt_tell.getText().toString();
                                int check = 0;
                                if (name.length() >= 3) {
                                    check++;
                                    layout_name.setBackgroundResource(R.drawable.border_gray);
                                } else {
                                    layout_name.setBackgroundResource(R.drawable.border_red);
                                }
                                if (tell.length() >= 6) {
                                    check++;
                                    layout_tell.setBackgroundResource(R.drawable.border_gray);
                                } else {
                                    layout_tell.setBackgroundResource(R.drawable.border_red);
                                }
                                if (check >= 2) {
                                    dialog.cancel();
                                    Toast.makeText(mContext, "Запрос на обратный звонок отправлен. Ожидайте звонка оператора", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, "Некорректное заполнение данных.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        // Отмена
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                        dialog.show();
                    }
                });
            }
            break;
            // Баннер
            case TYPE_ITEM_BANNER: {
                holder.slider_image.setListImage(mImageList);
            }
            break;
            // Название популярных категорий
            case TYPE_ITEM_TITLE_POPULAR_CATEGORY: {
                holder.txt_title.setText("Популярные категории");
            }
            break;
            // Популярные категории
            case TYPE_ITEM_POPULAR_CATEGORY: {
                CategoryHorizontalAdapter categoryHorizontalAdapter = new CategoryHorizontalAdapter(mPopularCategory, mContext);
                holder.recycler_horizontal_slider.setLayoutManager(
                        new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
                holder.recycler_horizontal_slider.setAdapter(categoryHorizontalAdapter);
            }
            break;
            // Название популярных товаров
            case TYPE_ITEM_TITLE_POPULAR_PRODUCT: {
                holder.txt_title.setText("Популярные товары");
            }
            break;
            // Популярные товары (Маленькие окна товаров)
            case TYPE_ITEM_POPULAR_PRODUCT: {
                try {
                    if (idConclusionProduct != null && idConclusionProduct.size() != 0) {
                        List<Product> productList = new ArrayList<>();
                        productList.add(mProductList.get(idConclusionProduct.get(position - 5)));
                        productList.add(mProductList.get(idConclusionProduct.get(position - 5) + 1));
                        productList.add(mProductList.get(idConclusionProduct.get(position - 5) + 2));
                        ProductGridAdapter productGridAdapter = new ProductGridAdapter(productList, mContext);
                        holder.grid.setAdapter(productGridAdapter);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            // Большое окно товаров
            case TYPE_ITEM_POPULAR_PRODUCT_BIG: {
                if (mProductList.size() != 0) {
                    final Product product = mProductList.get(idConclusionProduct.get(position - 5));
                    // Заполнение компонентов
                    holder.txt_name.setText(product.getName());
                    holder.txt_price.setText(setPrice(product.getPrice()));
                    Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                            .into(holder.img_product);
                    holder.txt_brand.setText(product.getBrand_name() + ", " + product.getBrand_country());
                    // Если цена 0
                    if (product.getPrice() == 0) {
                        holder.txt_price.setVisibility(View.GONE);
                        holder.btn_by.setBackgroundResource(R.color.colorDarkPurple);
                        holder.btn_by.setText("Запросить цену");
                    }
                    // Есть ли товар в корзине
                    if (Basket.checkProductBasket(product.getId_())) {
                        holder.btn_by.setBackgroundResource(R.color.colorGreen);
                        holder.btn_by.setText("В корзине");
                    }
                    // Обработка парехода на подробности о товаре
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, ProductActivity.class);
                            intent.putExtra("Id_", product.getId_())
                                    .putExtra("Name", product.getName())
                                    .putExtra("Price", product.getPrice())
                                    .putExtra("Quantity", product.getQuantity())
                                    .putExtra("Description", product.getDescription())
                                    .putExtra("Name_image", product.getName_image())
                                    .putExtra("Brand_country", product.getBrand_country())
                                    .putExtra("Brand_name", product.getBrand_name());
                            mContext.startActivity(intent);
                        }
                    });
                    // Обработка добавления в корзину
                    holder.btn_by.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            switch (holder.btn_by.getText().toString()) {
                                case "Купить": {
                                    // Добавление в корзину
                                    holder.btn_by.setText("В корзине");
                                    Basket.addProductBasket(mContext,
                                            product.getId_(),
                                            product.getName(),
                                            product.getName_image(),
                                            product.getPrice());
                                    // Вызов диалога
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    View dialogView = View.inflate(mContext, R.layout.dialog_by, null);
                                    ImageView img_product = dialogView.findViewById(R.id.img_product);
                                    TextView txt_name = dialogView.findViewById(R.id.txt_name);
                                    TextView txt_price = dialogView.findViewById(R.id.txt_price);
                                    Button btn_resume = dialogView.findViewById(R.id.btn_resume);
                                    Button btn_arrange = dialogView.findViewById(R.id.btn_arrange);
                                    builder.setView(dialogView);
                                    final Dialog dialog = builder.show();
                                    // Показ иозражения и названия
                                    Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                                            .placeholder(R.drawable.ico_small)
                                            .into(img_product);
                                    txt_name.setText(product.getName());
                                    txt_price.setText(setPrice(product.getPrice()));
                                    // Перейти в корзину
                                    btn_resume.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            intent.putExtra("ItemFragment", R.id.item_basket);
                                            mContext.startActivity(intent);
                                        }
                                    });
                                    // Продолжить покупки
                                    btn_arrange.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }
                                break;
                                case "В корзине": {
                                    Intent intent = new Intent(mContext, MainActivity.class);
                                    intent.putExtra("ItemFragment", R.id.item_basket);
                                    mContext.startActivity(intent);
                                }
                                break;
                                case "Запросить цену": {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    View dialogView = View.inflate(mContext, R.layout.dialog_request_price, null);
                                    final LinearLayout layout_surname = dialogView.findViewById(R.id.layout_surname);
                                    final LinearLayout layout__name = dialogView.findViewById(R.id.layout__name);
                                    final LinearLayout layout_mail = dialogView.findViewById(R.id.layout_mail);
                                    final EditText eText_surname = dialogView.findViewById(R.id.eText_surname);
                                    final EditText eText_name = dialogView.findViewById(R.id.eText_name);
                                    final EditText eText_mail = dialogView.findViewById(R.id.eText_mail);
                                    final EditText eText_message = dialogView.findViewById(R.id.eText_message);
                                    Button btn_push = dialogView.findViewById(R.id.btn_push);
                                    Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                                    builder.setView(dialogView);
                                    final Dialog dialog = builder.create();
                                    // Отправка запроса
                                    btn_push.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            int cheak = 0;
                                            String surname = eText_surname.getText().toString();
                                            String name = eText_name.getText().toString();
                                            String mail = eText_mail.getText().toString();
                                            String message = eText_message.getText().toString();
                                            // Фамилия
                                            if (surname != null && surname.length() >= 3) {
                                                cheak++;
                                                layout_surname.setBackgroundResource(R.drawable.border_gray);
                                            } else {
                                                layout_surname.setBackgroundResource(R.drawable.border_red);
                                            }
                                            // Имя
                                            if (name != null && name.length() >= 3) {
                                                cheak++;
                                                layout__name.setBackgroundResource(R.drawable.border_gray);
                                            } else {
                                                layout__name.setBackgroundResource(R.drawable.border_red);
                                            }
                                            // Отчество
                                            if (message != null && message.length() >= 3) {
                                                cheak++;
                                                layout_mail.setBackgroundResource(R.drawable.border_gray);
                                            } else {
                                                layout_mail.setBackgroundResource(R.drawable.border_red);
                                            }
                                            if (cheak >= 3) {
                                                // Отправка запроса
                                                Toast.makeText(mContext, "Запрос принят. Ожидайте ответа на почту.", Toast.LENGTH_LONG).show();
                                                dialog.cancel();
                                            } else {
                                                Toast.makeText(mContext, "Не все поля заполнены корректно.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                    // Закрытие диалога
                                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    dialog.show();
                                }
                                break;
                            }
                        }
                    });
                    break;
                }
            }
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ITEM_CALL;
        else if (position == 1) return TYPE_ITEM_BANNER;
        else if (position == 2) return TYPE_ITEM_TITLE_POPULAR_CATEGORY;
        else if (position == 3) return TYPE_ITEM_POPULAR_CATEGORY;
        else if (position == 4) return TYPE_ITEM_TITLE_POPULAR_PRODUCT;
        else if (position >= 4 && position % 3 != 1) return TYPE_ITEM_POPULAR_PRODUCT;
        else if (position >= 4 && position % 3 != 0) return TYPE_ITEM_POPULAR_PRODUCT_BIG;
        return 0;
    }

    @Override
    public int getItemCount() {
        // Телефоны, Баннер, Название (Популярные категории), Категории , Название (Популярные товары), товары
        int idConclusionProductSize = 0;
        if (idConclusionProduct != null && idConclusionProduct.size() != 0)
            idConclusionProductSize = idConclusionProduct.size() - 1;
        return idConclusionProductSize + 5;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        // Названиея
        TextView txt_title;
        // Обратный зхвонок
        TextView txt_tell;
        TextView txt_callBack;
        // Слайдер изображений
        MySliderImage slider_image;
        // Горизонтальный слайдер
        RecyclerView recycler_horizontal_slider;

        GridView grid;

        // Товары
        ImageView img_product;
        TextView txt_name;
        TextView txt_price;
        Button btn_by;
        TextView txt_brand;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            // Название
            txt_title = itemView.findViewById(R.id.txt_title);
            // Обратный звонок
            txt_tell = itemView.findViewById(R.id.txt_tell);
            txt_callBack = itemView.findViewById(R.id.txt_callBack);
            // Слайдер изображений
            slider_image = itemView.findViewById(R.id.slider_image);
            // Горизонтальный слайдер
            recycler_horizontal_slider = itemView.findViewById(R.id.recycler_horizontal_slider);
            // Таблица
            grid = itemView.findViewById(R.id.grid);

            // Товары
            txt_title = itemView.findViewById(R.id.txt_title);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_by = itemView.findViewById(R.id.btn_by);
            txt_brand = itemView.findViewById(R.id.txt_brand);
        }
    }

    // Конвертация цена
    private String setPrice(int price) {
        StringBuffer newPrice = new StringBuffer(String.valueOf(price) + " ₽");
        int position = 5;
        while (newPrice.length() > position) {
            newPrice = newPrice.insert((newPrice.length() - position), " ");
            position += 4;
        }
        return newPrice.toString();
    }

    // Получение популярных товаров
    private class getPopularProduct extends AsyncTask<String, Void, String> {

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
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArrayProduct = jsonObject.getJSONArray("Product");
                    for (int position = 0; position < jsonArrayProduct.length(); position++) {
                        JSONObject jsonObjectProduct = jsonArrayProduct.getJSONObject(position);
                        Product product = new Product(
                                jsonObjectProduct.getInt("id_"),
                                jsonObjectProduct.getString("name"),
                                jsonObjectProduct.getInt("price"),
                                jsonObjectProduct.getInt("quantity"),
                                null,
                                jsonObjectProduct.getString("brand_name"),
                                jsonObjectProduct.getString("brand_country"),
                                jsonObjectProduct.getString("name_image")
                        );
                        mProductList.add(product);
                    }
                    setProductList();
                    notifyDataSetChanged();
                    Log.d("Запрос товаров :", "Запрос выполнен");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(" --- Ошибка ---", "Нет или неверный ответ от сервера.");
                Toast.makeText(mContext, "Не удалось получить ответ от сервера.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Проверка ответа
    private static boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}
