package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.ankas.Objects.Category;

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

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return null;
    }
}
