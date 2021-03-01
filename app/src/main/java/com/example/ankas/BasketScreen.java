package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ankas.Class.Basket;
import com.squareup.picasso.Picasso;

public class BasketScreen extends AppCompatActivity {
    LinearLayout linear_basket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_screen);
        // Элемент добавления
        linear_basket = findViewById(R.id.linear_basket);
        // Перечисление элементов
        basketItem();
        // Кнопки нижнего меню
        bottomMenu();
    }
    private void basketItem(){
        if(Basket.getCountBasket()!=0) {
            linear_basket.removeAllViews();
            for (int i = 0; i < Basket.getCountBasket(); i++) {
                addBasketItem(i);
            }
        }
        else
        {
            LinearLayout layout_loading = findViewById(R.id.layout_loading);
            layout_loading.setVisibility(View.VISIBLE);
        }
    }
    private void addBasketItem(final int position) {
        // Общая стоимость товаров
        final TextView txt_sumProduct = findViewById(R.id.txt_sumProduct);
        final TextView txt_sum = findViewById(R.id.txt_sum);
        txt_sum.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
        txt_sumProduct.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
        View viewItemBasket = View.inflate(this, R.layout.item_basket, null);
        // Присвоение компонентов
        ImageView image = viewItemBasket.findViewById(R.id.image);
        TextView txt_name = viewItemBasket.findViewById(R.id.txt_name);
        TextView txt_price = viewItemBasket.findViewById(R.id.txt_price);
        final TextView txt_quantity = viewItemBasket.findViewById(R.id.txt_quantity);
        TextView txt_plus = viewItemBasket.findViewById(R.id.txt_plus);
        TextView txt_minus = viewItemBasket.findViewById(R.id.txt_minus);
        final TextView txt_sumPrice = viewItemBasket.findViewById(R.id.txt_sumPrice);
        // Вывод ифнормации
        final Basket basket = Basket.getBasketList().get(position);
        txt_name.setText(basket.getName());
        // Установка цены
        txt_price.setText(replacePrice(String.valueOf(basket.getPrice())));
        // Получение изображения
        Picasso.with(BasketScreen.this)
                .load("http://anndroidankas.h1n.ru/image/" + basket.getNameImage())
                .error(R.drawable.ico_small)
                .into(image);
        // Вывод кол-во товара
        txt_quantity.setText(String.valueOf(basket.getQuantity()));
        // Выбор кол-во товара
        txt_sumPrice.setText("Общая стоимость товара: " + Basket.getSumProduct(position));
        txt_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.setQuantityProduct(position, +1);
                Basket.setProductSystem(BasketScreen.this);
                txt_quantity.setText(String.valueOf(Basket.getBasketList().get(position).getQuantity()));
                txt_sumPrice.setText("Общая стоимость товара: "+Basket.getSumProduct(position));
                txt_sumProduct.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
                txt_sum.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
            }
        });
        txt_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Basket.setQuantityProduct(position, -1);
                if(Basket.getBasketList().get(position).getQuantity() == 0){
                    Basket.deleteItemBasket(position);
                    basketItem();
                }
                else {
                    txt_quantity.setText(String.valueOf(Basket.getBasketList().get(position).getQuantity()));
                    txt_sumPrice.setText("Общая стоимость товара: "+Basket.getSumProduct(position));
                }
                Basket.setProductSystem(BasketScreen.this);
                // Общая стоимость товаров
                txt_sumProduct.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
                txt_sum.setText(replacePrice(String.valueOf(Basket.getSumPrice())));
            }
        });
        // Переход к товару
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketScreen.this, ProductScreen.class);
                intent.putExtra("id_item", Basket.getBasketList().get(position).getId_());
                startActivity(intent);
            }
        });
        txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BasketScreen.this, ProductScreen.class);
                intent.putExtra("id_item", Basket.getBasketList().get(position).getId_());
                startActivity(intent);
            }
        });
        linear_basket.addView(viewItemBasket);
    }

    private String replacePrice(String price){
        StringBuffer priceNew = new StringBuffer(price + " ₽");
        int count = 5;
        while (priceNew.length()>count){
            priceNew = priceNew.insert((priceNew.length() - count), " ");
            count= count + 4;
        }
        return priceNew.toString();
    }
    // Нижнее меню
    private void bottomMenu(){
        // Банки
        ImageView image_applePay = findViewById(R.id.image_applePay);
        image_applePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apple.com/ru/apple-pay/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_googlePay = findViewById(R.id.image_googlePay);
        image_googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pay.google.com/intl/ru_ru/about/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_mastercard = findViewById(R.id.image_mastercard);
        image_mastercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mastercard.ru/ru-ru.html"));
                startActivity(browserIntent);
            }
        });
        ImageView iamge_visa = findViewById(R.id.iamge_visa);
        iamge_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.visa.com.ru/"));
                startActivity(browserIntent);
            }
        });
        // Соц сетия
        ImageView image_VK = findViewById(R.id.image_VK);
        image_VK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/ankas_ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_YouTube = findViewById(R.id.image_YouTube);
        image_YouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Inst = findViewById(R.id.image_Inst);
        image_Inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ankas.ru/?hl=ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Facebook = findViewById(R.id.image_Facebook);
        image_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ankas.ru/"));
                startActivity(browserIntent);
            }
        });
    }
}