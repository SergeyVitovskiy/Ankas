package com.example.ankas.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.CategoryAndProduct;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexCategoryAdapter extends RecyclerView.Adapter<ComplexCategoryAdapter.ItemHolder> {
    // Название вормы
    private final int TYPE_ITEM1 = 0;
    // Категории
    private final int TYPE_ITEM2 = 1;

    List<Category> mCategoryList;
    Context mContext;

    // Конструктор
    public ComplexCategoryAdapter(Context mContext, List<Category> mCategoryList) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
            case TYPE_ITEM2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
                break;
        }
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            // Обрабока заголовка
            case TYPE_ITEM1:
                holder.txt_title.setText("Категории товаров");
                break;
            // Обработка элемента (категории)
            case TYPE_ITEM2:
                final Category category = mCategoryList.get(position - 1);
                holder.txt_name.setText(category.getName());
                Picasso.get().load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                        .into(holder.img_category);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCategoryAndProduct(category.getId_());
                    }
                });
                break;
        }
    }

    private void openCategoryAndProduct(int id){
        Intent intent = new Intent(mContext, CategoryAndProduct.class);
        intent.putExtra("id_", id);
        mContext.startActivity(intent);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ITEM1;
        return TYPE_ITEM2;
    }

    @Override
    public int getItemCount() {
        // Кол-во элементов в листе + название
        return mCategoryList.size() + 1;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        // Заголовок
        TextView txt_title;
        // Компоненты элементов
        ImageView img_category;
        TextView txt_name;

        // Присвоение компонентов
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            img_category = itemView.findViewById(R.id.img_category);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
