package com.example.ankas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        Button btn_by = viewItem.findViewById(R.id.btn_by);
        TextView txt_brand = viewItem.findViewById(R.id.txt_brand);
        TextView txt_available = viewItem.findViewById(R.id.txt_available);
        // Заполнение элемента
        final Product product = mProductList.get(position);
        // Проверка на пустой элементт
        if(product.getId_() != 0) {
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
            btn_by.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                            .putExtra("Brand_country", product.getBrand_country())
                            .putExtra("Brand_name", product.getBrand_name());
                    mContext.startActivity(intent);
                }
            });
        } else {
            txt_available.setText("");
            txt_brand.setText("");
            txt_name.setText("");
            txt_price.setText("");
            btn_by.setVisibility(View.VISIBLE);
            Picasso.get().load("http://anndroidankas.h1n.ru/image/whiteSquare.png")
                    .placeholder(R.drawable.ico_small)
                    .into(image_product);
        }
        return viewItem;
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
