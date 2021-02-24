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

public class FlipperBannerAdapter extends BaseAdapter {
    private List<String> imageNameList = new ArrayList<>();
    Context mContext;

    public FlipperBannerAdapter(Context mContext, List<String> imageNameList) {
        this.imageNameList = imageNameList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View viewImage = View.inflate(mContext, R.layout.item_image, null);
        ImageView imageView = viewImage.findViewById(R.id.image);
        // Загрузка изображения
        Picasso.with(mContext)
                .load("http://anndroidankas.h1n.ru/image/" + imageNameList.get(position))
                .error(R.drawable.ico)
                .into(imageView);
        return viewImage;
    }
}
