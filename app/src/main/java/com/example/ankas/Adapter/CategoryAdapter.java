package com.example.ankas.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankas.CategoryAndProduct;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Category;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private List<Category> mCategoryList;
    Context mContext;

    public CategoryAdapter(List<Category> mCategoryList, Context mContext) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCategoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCategoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCategoryList.get(position).getId_();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewItem = View.inflate(mContext, R.layout.item_category, null);
        // Присвоение компонентов
        TextView txt_name = viewItem.findViewById(R.id.txt_name);
        ImageView image_category = viewItem.findViewById(R.id.image_category);
        final Category category = mCategoryList.get(position);
        // Изменить цвет
        if (category.getId_() == 0)
            txt_name.setTextColor(Color.BLUE);
        txt_name.setText(category.getName());
        Picasso.get()
                .load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                .placeholder(R.drawable.ico_small)
                .into(image_category);
        // Обработка нажатия
        viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Посмотреть все категории
                if (category.getId_() == 0) {
                    MainActivity.selectItem(R.id.item_category);
                } else {
                    Intent intent = new Intent(mContext, CategoryAndProduct.class);
                    intent.putExtra("id_", category.getId_());
                    mContext.startActivity(intent);
                }
            }
        });
        return viewItem;
    }
}
