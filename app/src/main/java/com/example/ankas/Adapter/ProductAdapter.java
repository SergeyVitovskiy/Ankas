package com.example.ankas.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;
import com.example.ankas.ProductActivity;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private List<Product> mProductList;
    Context mContext;

    public ProductAdapter(List<Product> mProductList, Context mContext) {
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mProductList.size();
    }

    @Override
    public Object getItem(int position) {
        return mProductList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mProductList.get(position).getId_();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View viewItem = View.inflate(mContext, R.layout.item_product, null);
        TextView txt_name = viewItem.findViewById(R.id.txt_name);
        ImageView image_product = viewItem.findViewById(R.id.image_product);
        TextView txt_price = viewItem.findViewById(R.id.txt_price);
        final Button btn_by = viewItem.findViewById(R.id.btn_by);
        TextView txt_brand = viewItem.findViewById(R.id.txt_brand);
        TextView txt_available = viewItem.findViewById(R.id.txt_available);
        // Заполнение элемента
        final Product product = mProductList.get(position);
        // Проверка на пустой элементт
        if (product.getId_() != 0) {
            txt_name.setText(product.getName());
            Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                    .placeholder(R.drawable.ico_small)
                    .into(image_product);
            txt_price.setText(setPrice(product.getPrice()));
            txt_brand.setText(product.getBrand_name() + ", " + product.getBrand_country());
            // Кол-во товаров
            if (product.getQuantity() > 0) {
                txt_available.setText("В наличии");
            } else {
                txt_available.setText("Под заказ");
            }
            // Есть ли товар в корзине
            if (Basket.checkProductBasket(product.getId_()))
                btn_by.setText("В корзине");
            else
                btn_by.setText("Купить");
            // Купить товар или перейти в корзину
            btn_by.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Упить или перейти в корзину
                    if(btn_by.getText().toString().equals("Купить")) {
                        btn_by.setText("В корзине");
                        btn_by(product.getId_(),
                                product.getName(),
                                product.getName_image(),
                                product.getPrice());
                    } else
                    {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.putExtra("ItemFragment", R.id.item_basket);
                        mContext.startActivity(intent);
                        btn_by.setText("Купить");
                    }
                }
            });
            // Переход к подробностям о товаре
            image_product.setOnClickListener(new View.OnClickListener() {
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
        } else {
            // Пустое поля для красивого вывода
            // Пустое поле
            txt_available.setText("");
            txt_brand.setText("");
            txt_name.setText("");
            txt_price.setText("");
            btn_by.setVisibility(View.VISIBLE);
            Picasso.get().load("http://anndroidankas.h1n.ru/image/whiteSquare.png")
                    .placeholder(R.drawable.ico_small)
                    .into(image_product);
        }
        // Возрат элемента обратно
        return viewItem;
    }

    // Диалоговое окно покупки товаров
    private void btn_by(int id, String name, String image, int price) {
        // Добавление товара в корзину
        Basket.addProductBasket(mContext, id, name, image, price);
        // Создание и присвоение макета к диалогу
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View viewItemDialog = View.inflate(mContext, R.layout.dialog_by, null);
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
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra("ItemFragment", R.id.item_basket);
                mContext.startActivity(intent);
            }
        });
        // Вывод диалога
        dialogBy.show();
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
}