package com.example.ankas.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ankas.CategoryAndProduct;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Product;
import com.example.ankas.ProductActivity;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComplexProductAdapter extends RecyclerView.Adapter<ComplexProductAdapter.ItemHolder> {
    private final int TYPE_ITEM1 = 0;
    private final int TYPE_ITEM2 = 1;

    List<Product> mProductList;
    Context mContext;

    public ComplexProductAdapter(List<Product> mProductList, Context mContext) {
        this.mProductList = mProductList;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_main, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_main, parent, false);
                break;
        }
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_ITEM1: {
                holder.txt_title.setText("Товары");
            }
            break;
            case TYPE_ITEM2: {
                final Product product = mProductList.get(position - 1);
                // Заполнение компонентов
                holder.txt_name.setText(product.getName());
                holder.txt_price.setText(setPrice(product.getPrice()));
                Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                        .into(holder.img_product);
                holder.txt_brand.setText(product.getBrand_name() + ", " + product.getBrand_country());
                // Если цена 0
                if (product.getPrice() == 0) {
                    holder.txt_price.setVisibility(View.GONE);
                    holder.btn_by.setBackgroundResource(R.color.colorDarkPurple);
                    holder.btn_by.setText("Запросить цену");
                }
                // Есть ли товар в корзине
                if (Basket.checkProductBasket(product.getId_())) {
                    holder.btn_by.setText("В корзине");
                }
                // Обработка парехода на подробности о товаре
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, ProductActivity.class);
                        intent.putExtra("Id_", product.getId_())
                                .putExtra("Name", product.getName())
                                .putExtra("Price", product.getPrice())
                                .putExtra("Quantity", product.getQuantity())
                                .putExtra("Description", product.getDescription())
                                .putExtra("Name_image", product.getName_image())
                                .putExtra("Brand_country", product.getBrand_country())
                                .putExtra("Brand_name", product.getBrand_name());
                        mContext.startActivity(intent);
                    }
                });
                // Обработка добавления в корзину
                holder.btn_by.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (holder.btn_by.getText().toString()) {
                            case "Купить": {
                                // Добавление в корзину
                                holder.btn_by.setText("В корзине");
                                Basket.addProductBasket(mContext,
                                        product.getId_(),
                                        product.getName(),
                                        product.getName_image(),
                                        product.getPrice());
                                // Вызов диалога
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                View dialogView = View.inflate(mContext, R.layout.dialog_by, null);
                                ImageView img_product = dialogView.findViewById(R.id.img_product);
                                TextView txt_name = dialogView.findViewById(R.id.txt_name);
                                TextView txt_price = dialogView.findViewById(R.id.txt_price);
                                Button btn_resume = dialogView.findViewById(R.id.btn_resume);
                                Button btn_arrange = dialogView.findViewById(R.id.btn_arrange);
                                builder.setView(dialogView);
                                final Dialog dialog = builder.show();
                                // Показ иозражения и названия
                                Picasso.get().load("http://anndroidankas.h1n.ru/image/" + product.getName_image())
                                        .into(img_product);
                                txt_name.setText(product.getName());
                                txt_price.setText(setPrice(product.getPrice()));
                                // Перейти в корзину
                                btn_resume.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        intent.putExtra("ItemFragment", R.id.item_basket);
                                        mContext.startActivity(intent);
                                    }
                                });
                                // Продолжить покупки
                                btn_arrange.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                            break;
                            case "В корзине": {
                                Intent intent = new Intent(mContext, MainActivity.class);
                                intent.putExtra("ItemFragment", R.id.item_basket);
                                mContext.startActivity(intent);
                            }
                            break;
                            case "Запросить цену": {
                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                View dialogView = View.inflate(mContext, R.layout.dialog_request_price, null);
                                final LinearLayout layout_surname = dialogView.findViewById(R.id.layout_surname);
                                final LinearLayout layout__name = dialogView.findViewById(R.id.layout__name);
                                final LinearLayout layout_mail = dialogView.findViewById(R.id.layout_mail);
                                final EditText eText_surname = dialogView.findViewById(R.id.eText_surname);
                                final EditText eText_name = dialogView.findViewById(R.id.eText_name);
                                final EditText eText_mail = dialogView.findViewById(R.id.eText_mail);
                                final EditText eText_message = dialogView.findViewById(R.id.eText_message);
                                Button btn_push = dialogView.findViewById(R.id.btn_push);
                                Button btn_cancel = dialogView.findViewById(R.id.btn_cancel);
                                builder.setView(dialogView);
                                final Dialog dialog = builder.create();
                                // Отправка запроса
                                btn_push.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        int cheak = 0;
                                        String surname = eText_surname.getText().toString();
                                        String name = eText_name.getText().toString();
                                        String mail = eText_mail.getText().toString();
                                        String message = eText_message.getText().toString();
                                        // Фамилия
                                        if (surname != null && surname.length() >= 3) {
                                            cheak++;
                                            layout_surname.setBackgroundResource(R.drawable.border_gray);
                                        } else {
                                            layout_surname.setBackgroundResource(R.drawable.border_red);
                                        }
                                        // Имя
                                        if (name != null && name.length() >= 3) {
                                            cheak++;
                                            layout__name.setBackgroundResource(R.drawable.border_gray);
                                        } else {
                                            layout__name.setBackgroundResource(R.drawable.border_red);
                                        }
                                        // Отчество
                                        if (message != null && message.length() >= 3) {
                                            cheak++;
                                            layout_mail.setBackgroundResource(R.drawable.border_gray);
                                        } else {
                                            layout_mail.setBackgroundResource(R.drawable.border_red);
                                        }
                                        if (cheak >= 3) {
                                            // Отправка запроса
                                            Toast.makeText(mContext, "Запрос принят. Ожидайте ответа на почту.", Toast.LENGTH_LONG).show();
                                            dialog.cancel();
                                        } else {
                                            Toast.makeText(mContext, "Не все поля заполнены корректно.", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                // Закрытие диалога
                                btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                            break;
                        }
                    }
                });
            }
            break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ITEM1;
        return TYPE_ITEM2;
    }

    @Override
    public int getItemCount() {
        return mProductList.size() + 1;
    }
    // Элементы View компонентов
    public class ItemHolder extends RecyclerView.ViewHolder {
        // Заголовок
        TextView txt_title;
        // Компоненты
        ImageView img_product;
        TextView txt_name;
        TextView txt_price;
        Button btn_by;
        TextView txt_brand;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.txt_title);
            img_product = itemView.findViewById(R.id.img_product);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_price = itemView.findViewById(R.id.txt_price);
            btn_by = itemView.findViewById(R.id.btn_by);
            txt_brand = itemView.findViewById(R.id.txt_brand);
        }
    }

    // Конвертация цена
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
