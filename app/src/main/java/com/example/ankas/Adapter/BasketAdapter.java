package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.Objects.Basket;
import com.example.ankas.R;

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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewItem = View.inflate(mContext, R.layout.item_basket, null);
        ImageView image = viewItem.findViewById(R.id.image);
        TextView txt_name = viewItem.findViewById(R.id.txt_name);
        return viewItem;
    }
}
