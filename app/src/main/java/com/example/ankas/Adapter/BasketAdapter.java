package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.Fragments.BasketFragment;
import com.example.ankas.Objects.Basket;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BasketAdapter extends BaseAdapter {

    List<Basket> mBasketList;
    Context mContext;

    public BasketAdapter(List<Basket> mBasketList, Context mContext) {
        this.mBasketList = mBasketList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mBasketList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBasketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mBasketList.get(position).getId_();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View viewItemBasket = View.inflate(mContext, R.layout.item_basket, null);
        // Присвоение компонентов
        ImageView image = viewItemBasket.findViewById(R.id.image);
        TextView txt_name = viewItemBasket.findViewById(R.id.txt_name);
        TextView txt_price = viewItemBasket.findViewById(R.id.txt_price);
        TextView txt_minus = viewItemBasket.findViewById(R.id.txt_minus);
        TextView txt_plus = viewItemBasket.findViewById(R.id.txt_plus);
        final TextView txt_quantity = viewItemBasket.findViewById(R.id.txt_quantity);
        final TextView txt_sumPrice = viewItemBasket.findViewById(R.id.txt_sumPrice);
        TextView txt_delete = viewItemBasket.findViewById(R.id.txt_delete);
        // Вывод информации
        final Basket basket = mBasketList.get(position);
        txt_name.setText(basket.getName());
        txt_price.setText(setPrice(basket.getPrice()));
        txt_quantity.setText(String.valueOf(basket.getQuantity()));
        txt_sumPrice.setText(setPrice(basket.getSumPrice()));
        Picasso.get()
                .load("http://anndroidankas.h1n.ru/image/" + basket.getImage())
                .placeholder(R.drawable.ico_small)
                .into(image);
        // Обработка нажатий
        txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Basket.getBasketList().get(position).getQuantity() > 1) {
                    Basket.getBasketList().get(position).setQuantity(mContext,-1);
                    updatePrice(txt_quantity, position, txt_sumPrice);
                }
            }
        });
        txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.getBasketList().get(position).setQuantity(mContext,1);
                updatePrice(txt_quantity, position, txt_sumPrice);
            }
        });
        // Удаление товара
        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.deleteItemBasket(mContext,position);
                BasketFragment.checkVisibleWindowNullBasket();
            }
        });
        return viewItemBasket;
    }

    // Изменение цены
    private void updatePrice(TextView txt_quantity, int position, TextView txt_sumPrice){
        txt_quantity.setText(
                String.valueOf(Basket.getBasketList().get(position).getQuantity()));
        txt_sumPrice.setText(setPrice(Basket.getBasketList().get(position).getSumPrice()));
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
