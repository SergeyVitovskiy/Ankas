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
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryHorizontalAdapter extends RecyclerView.Adapter<CategoryHorizontalAdapter.HolderItem> {
    List<Category> mCategoryList;
    Context mContext;

    public CategoryHorizontalAdapter(List<Category> mCategoryList, Context mContext) {
        this.mCategoryList = mCategoryList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_main, parent, false);
        return new HolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        if(mCategoryList.size() != 0) {
            final Category category = mCategoryList.get(position);
            holder.txt_name.setText(category.getName());
            Picasso.get().load("http://anndroidankas.h1n.ru/image/" + category.getImage())
                    .into(holder.img_category);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CategoryAndProduct.class);
                    intent.putExtra("id_", category.getId_());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        ImageView img_category;
        TextView txt_name;

        public HolderItem(@NonNull View itemView) {
            super(itemView);
            img_category = itemView.findViewById(R.id.img_category);
            txt_name = itemView.findViewById(R.id.txt_name);
        }
    }
}
