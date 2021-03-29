package com.example.ankas.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.Components.MySliderImage;
import com.example.ankas.Objects.Category;
import com.example.ankas.Objects.Product;
import com.example.ankas.R;

import java.util.List;

public class ComplexMainActivityAdapter extends RecyclerView.Adapter<ComplexMainActivityAdapter.ItemHolder> {
    // Звонки
    private final int TYPE_ITEM_CALL = 0;
    // Баннер
    private final int TYPE_ITEM_BANNER = 1;
    // Категории (Название)
    private final int TYPE_ITEM_TITLE_POPULAR_CATEGORY = 2;
    // Категории
    private final int TYPE_ITEM_POPULAR_CATEGORY = 3;
    // Товары (Название)
    private final int TYPE_ITEM_TITLE_POPULAR_PRODUCT = 4;
    // Товары
    private final int TYPE_ITEM_POPULAR_PRODUCT = 5;
    // Товары
    private final int TYPE_ITEM_POPULAR_PRODUCT_BIG = 6;

    List<String> mImageList;
    List<Category> mPopularCategory;
    List<Product> mProductList;
    Context mContext;

    public ComplexMainActivityAdapter(List<String> mImageList, List<Category> mPopularCategory, List<Product> mProductList, Context mContext) {
        this.mImageList = mImageList;
        this.mPopularCategory = mPopularCategory;
        this.mProductList = mProductList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ComplexMainActivityAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_ITEM_CALL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call_back, parent, false);
                break;
            case TYPE_ITEM_BANNER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_image, parent, false);
                break;
            case TYPE_ITEM_TITLE_POPULAR_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
            case TYPE_ITEM_POPULAR_CATEGORY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_horizontal_slider, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
                break;
        }
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComplexMainActivityAdapter.ItemHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == TYPE_ITEM_CALL) {
            // Телефон
            holder.txt_tell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:+73517514012"));
                    mContext.startActivity(intent);
                }
            });
            // Заказать обратный звонок
            holder.txt_callBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View viewDialog = View.inflate(mContext, R.layout.dialog_call_back, null);
                    final LinearLayout layout_name = viewDialog.findViewById(R.id.layout_name);
                    final LinearLayout layout_tell = viewDialog.findViewById(R.id.layout_tell);
                    final TextView txt_name = viewDialog.findViewById(R.id.txt_name);
                    final TextView txt_tell = viewDialog.findViewById(R.id.txt_tell);
                    Button btn_call_back = viewDialog.findViewById(R.id.btn_call_back);
                    Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                    builder.setView(viewDialog);
                    final Dialog dialog = builder.create();
                    // Заказать звонок
                    btn_call_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String name = txt_name.getText().toString();
                            String tell = txt_tell.getText().toString();
                            int check = 0;
                            if (name.length() >= 3) {
                                check++;
                                layout_name.setBackgroundResource(R.drawable.border_gray);
                            } else {
                                layout_name.setBackgroundResource(R.drawable.border_red);
                            }
                            if (tell.length() >= 6) {
                                check++;
                                layout_tell.setBackgroundResource(R.drawable.border_gray);
                            } else {
                                layout_tell.setBackgroundResource(R.drawable.border_red);
                            }
                            if (check >= 2) {
                                dialog.cancel();
                                Toast.makeText(mContext, "Запрос на обратный звонок отправлен. Ожидайте звонка оператора", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(mContext, "Некорректное заполнение данных.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    // Отмена
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
        } else if (type == TYPE_ITEM_BANNER) {
            holder.slider_image.setListImage(mImageList);
        } else if (type == TYPE_ITEM_TITLE_POPULAR_CATEGORY) {
            holder.txt_title.setText("Популярные категории");
        } else if (type == TYPE_ITEM_POPULAR_CATEGORY) {
            CategoryHorizontalAdapter categoryHorizontalAdapter = new CategoryHorizontalAdapter(mPopularCategory, mContext);
            holder.recycler_horizontal_slider.setLayoutManager(
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, true));
            holder.recycler_horizontal_slider.setAdapter(categoryHorizontalAdapter);
        } else {
            holder.txt_title.setText("Позиция: " + position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ITEM_CALL;
        else if (position == 1) return TYPE_ITEM_BANNER;
        else if (position == 2) return TYPE_ITEM_TITLE_POPULAR_CATEGORY;
        else if (position == 3) return TYPE_ITEM_POPULAR_CATEGORY;
        else if (position == 999) return TYPE_ITEM_TITLE_POPULAR_PRODUCT;
        else if (position == 999) return TYPE_ITEM_POPULAR_PRODUCT;
        else return TYPE_ITEM_TITLE_POPULAR_PRODUCT;
    }

    @Override
    public int getItemCount() {
        // Телефоны, Баннер, Название (Популярные категории), Категории , Название (Популярные товары), товары
        return 100;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        // Названиея
        TextView txt_title;
        // Обратный зхвонок
        TextView txt_tell;
        TextView txt_callBack;
        // Слайдер изображений
        MySliderImage slider_image;
        // Горизонтальный слайдер
        RecyclerView recycler_horizontal_slider;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            // Название
            txt_title = itemView.findViewById(R.id.txt_title);
            // Обратный звонок
            txt_tell = itemView.findViewById(R.id.txt_tell);
            txt_callBack = itemView.findViewById(R.id.txt_callBack);
            // Слайдер изображений
            slider_image = itemView.findViewById(R.id.slider_image);
            // Горизонтальный слайдер
            recycler_horizontal_slider = itemView.findViewById(R.id.recycler_horizontal_slider);
            Log.d("position: ", "Присвоение компонентов");
        }
    }
}
