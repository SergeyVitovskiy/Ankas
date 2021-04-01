package com.example.ankas.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
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

import com.example.ankas.Components.MySliderImage;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.Characteristic;
import com.example.ankas.Objects.Product;
import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

public class ComplexProductActivityAdapter extends RecyclerView.Adapter<ComplexProductActivityAdapter.HolderItem> {
    Context mContext;
    Product mProduct;
    List<String> mListImage;
    List<Characteristic> mListCharacteristic;

    // Конструктор


    public ComplexProductActivityAdapter(Context mContext, Product mProduct, List<String> mListImage, List<Characteristic> mListCharacteristic) {
        this.mContext = mContext;
        this.mProduct = mProduct;
        this.mListImage = mListImage;
        this.mListCharacteristic = mListCharacteristic;
    }

    // Название
    private final int TYPE_ITEM_NAME = 0;
    // Слайдер изображений
    private final int TYPE_ITEM_IMAGE = 1;
    // Наличие, доставка, самовывоз, цена, купить
    private final int TYPE_ITEM_PRICE_AND_BY = 2;
    // Описание товара (Название)
    private final int TYPE_ITEM_DESCRIPTION_NAME = 3;
    // Описание товара
    private final int TYPE_ITEM_DESCRIPTION = 4;
    // Характеристики (Название)
    private final int TYPE_ITEM_CHARACTERISTICS_NAME = 5;
    // Характеристики
    private final int TYPE_ITEM_CHARACTERISTICS = 6;
    // Текстовка
    private final int TYPE_ITEM_TEXT = 7;

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            // Название
            case TYPE_ITEM_NAME: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            }
            break;
            // Изображения
            case TYPE_ITEM_IMAGE: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slider_image, parent, false);
            }
            break;
            // Наличие, доставка, самовывоз, цена, купить
            case TYPE_ITEM_PRICE_AND_BY: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_prica_and_by, parent, false);
            }
            break;
            // Описание товаров (Название)
            case TYPE_ITEM_DESCRIPTION_NAME: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            }
            break;
            // Описание
            case TYPE_ITEM_DESCRIPTION: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt_html, parent, false);
            }
            break;
            // Характеристикаи товаров (Название)
            case TYPE_ITEM_CHARACTERISTICS_NAME: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            }
            break;
            // Характеристики
            case TYPE_ITEM_CHARACTERISTICS: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_characteristic, parent, false);
            }
            break;
            default: {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_title, parent, false);
            }
            break;
        }
        return new HolderItem(view);
    }


    public void setProduct(Product mProduct) {
        this.mProduct = mProduct;
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderItem holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            // название
            case TYPE_ITEM_NAME: {
                holder.txt_title.setText(mProduct.getName());
            }
            break;
            // Изображения
            case TYPE_ITEM_IMAGE: {
                if (mListImage != null && mListImage.size() != 0)
                    holder.slider_image.setListImage(mListImage);
            }
            break;
            // Наличие, доставка, самовывоз, цена, купить
            case TYPE_ITEM_PRICE_AND_BY: {
                // Код товара и брэнд
                holder.txt_kod_product.setText("Код товара: " + mProduct.getId_());
                holder.txt_brand.setText(mProduct.getBrand_name()
                        + ", " + mProduct.getBrand_country());
                holder.txt_quantity.setText(mProduct.getQuantity() + " шт.");
                // Цена
                holder.txt_price.setText(setPrice(mProduct.getPrice()));
                // Купть
                holder.btn_by.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switch (holder.btn_by.getText().toString()) {
                            case "Купить": {
                                // Добавление в корзину
                                holder.btn_by.setText("В корзине");
                                Basket.addProductBasket(mContext,
                                        mProduct.getId_(),
                                        mProduct.getName(),
                                        mProduct.getName_image(),
                                        mProduct.getPrice());
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
                                Picasso.get().load("http://anndroidankas.h1n.ru/image/" + mProduct.getName_image())
                                        .placeholder(R.drawable.ico_small)
                                        .into(img_product);
                                txt_name.setText(mProduct.getName());
                                txt_price.setText(setPrice(mProduct.getPrice()));
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
            // Описание товара (Название)
            case TYPE_ITEM_DESCRIPTION_NAME: {
                holder.txt_title.setText("Описание товара");
            }
            break;
            // Описание товара
            case TYPE_ITEM_DESCRIPTION: {
                if (mProduct.getDescription() != null && mProduct.getDescription() != "" && mProduct.getDescription() != "null") {
                    String description = mProduct.getDescription();
                    description.replaceAll("<span style=\\\"font-weight:bold\\\">", "<h6>")
                            .replaceAll("</span>", "</h6>");
                    holder.txt_html.setHtml(description, new HtmlAssetsImageGetter(holder.txt_html));
                } else {
                    holder.txt_html.setText("У данного товара нет описания");
                }
            }
            break;
            // Характеристики товаров (Название)
            case TYPE_ITEM_CHARACTERISTICS_NAME: {
                holder.txt_title.setText("Характеристики товаров товара");
            }
            break;
            // Характеристики товаров
            case TYPE_ITEM_CHARACTERISTICS: {
                if (mListCharacteristic.size() == 0) {
                    holder.txt_name.setText("");
                    holder.txt_characteristic.setText("У данного товара нет характеристик");
                } else {
                    Characteristic characteristic = mListCharacteristic.get(position - 6);
                    holder.txt_name.setText(characteristic.getNameCharacteristic());
                    holder.txt_characteristic.setText(characteristic.getCharacteristic());
                }
            }
            break;
            default: {
            }
            break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_ITEM_NAME;
        else if (position == 1) return TYPE_ITEM_IMAGE;
        else if (position == 2) return TYPE_ITEM_PRICE_AND_BY;
        else if (position == 3) return TYPE_ITEM_DESCRIPTION_NAME;
        else if (position == 4) return TYPE_ITEM_DESCRIPTION;
        else if (position == 5) return TYPE_ITEM_CHARACTERISTICS_NAME;
        else if (position >= 6 && position <= (mListCharacteristic.size() + 5))
            return TYPE_ITEM_CHARACTERISTICS;
        return TYPE_ITEM_TEXT;
    }

    @Override
    public int getItemCount() {
        if (mListCharacteristic.size() == 0)
            return 7;
        else
            return 6 + mListCharacteristic.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        // Заголовок
        TextView txt_title;
        // Слайдер изображений
        MySliderImage slider_image;
        // Наличие, доставка, самовывоз, цена, купить
        TextView txt_quantity;
        TextView txt_price;
        TextView txt_kod_product;
        TextView txt_brand;
        Button btn_by;
        // Описание
        HtmlTextView txt_html;
        // Характеристики
        TextView txt_name;
        TextView txt_characteristic;

        public HolderItem(@NonNull View itemView) {
            super(itemView);
            // Заголовок
            txt_title = itemView.findViewById(R.id.txt_title);
            // Слайдер изображений
            slider_image = itemView.findViewById(R.id.slider_image);
            // Наличие, доставка, самовывоз, цена, купить
            txt_quantity = itemView.findViewById(R.id.txt_quantity);
            txt_price = itemView.findViewById(R.id.txt_price);
            txt_kod_product = itemView.findViewById(R.id.txt_kod_product);
            txt_brand = itemView.findViewById(R.id.txt_brand);
            btn_by = itemView.findViewById(R.id.btn_by);
            // Описание
            txt_html = itemView.findViewById(R.id.txt_html);
            // Характеристики
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_characteristic = itemView.findViewById(R.id.txt_characteristic);
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
