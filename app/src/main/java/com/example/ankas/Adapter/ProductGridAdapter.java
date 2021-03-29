package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Product;
import com.example.ankas.R;

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
        return viewProduct;
    }
}
