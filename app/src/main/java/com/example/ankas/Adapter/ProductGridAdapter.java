package com.example.ankas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Product;
import com.example.ankas.ProductActivity;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductGridAdapter extends BaseAdapter {
    List<Product> mProductList;
    Context mContext;

    public ProductGridAdapter(List<Product> mProductList, Context mContext) {
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 3;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewProduct = View.inflate(mContext, R.layout.item_product, null);
        ImageView img_product = viewProduct.findViewById(R.id.img_product);
        TextView txt_name = viewProduct.findViewById(R.id.txt_name);
        TextView txt_price = viewProduct.findViewById(R.id.txt_price);
        TextView txt_brand = viewProduct.findViewById(R.id.txt_brand);
        final Product product = mProductList.get(position);
        Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                .placeholder(R.drawable.ico_small)
                .into(img_product);
        txt_name.setText(product.getName());
        txt_price.setText(setPrice(product.getPrice()));
        txt_brand.setText(product.getBrand_name() + ", " + product.getBrand_country());
        // Обработка парехода на подробности о товаре
        viewProduct.setOnClickListener(new View.OnClickListener() {
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
        return viewProduct;
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
