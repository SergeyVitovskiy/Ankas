package com.example.ankas.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageProductAdapter extends BaseAdapter {
    List<String> imageList = new ArrayList<>();
    Context mContext;

    public ImageProductAdapter(List<String> imageList, Context mContext) {
        this.imageList = imageList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewItem = View.inflate(mContext, R.layout.item_image, null);
        ImageView imageView = viewItem.findViewById(R.id.image);
        // Загрузка изображения
        Picasso.with(mContext)
                .load("http://anndroidankas.h1n.ru/image/" + imageList.get(position))
                .error(R.drawable.ico)
                .into(imageView);
        return viewItem;
    }
}
