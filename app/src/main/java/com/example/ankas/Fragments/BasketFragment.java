package com.example.ankas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.Adapter.BasketAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.R;

public class BasketFragment extends Fragment {
    static LinearLayout layout_windowBasketNull;
    View BasketFragmentView;
    Context context;
    ExpandableHeightGridView grid_basket;
    static BasketAdapter basketAdapter;
    String receivingProduct;
    // Сумма заказа
    static TextView txt_sumProductPrice;
    static TextView sumPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasketFragmentView = inflater.inflate(R.layout.basket_fragment, null);
        context = BasketFragmentView.getContext();
        addProdctBasket();
        windowNullBasket();
        receivingProduct();
        // Сумма товаров
        txt_sumProductPrice = BasketFragmentView.findViewById(R.id.txt_sumProductPrice);
        sumPrice = BasketFragmentView.findViewById(R.id.sumPrice);
        sumPrice();
        return BasketFragmentView;
    }
    public static void sumPrice(){
        txt_sumProductPrice.setText("Общая стоимость товаров: " + setPrice(Basket.getSumProduct()));
        sumPrice.setText("Итог: " + setPrice(Basket.getSumProduct()));
    }
    // Выбор способа получения
    private void receivingProduct() {
        receivingProduct = "pickUp";
        final LinearLayout layout_pickUp = BasketFragmentView.findViewById(R.id.layout_pickUp);
        final LinearLayout layout_delivery = BasketFragmentView.findViewById(R.id.layout_delivery);
        // Самовывоз
        layout_pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_pickUp.setBackgroundResource(R.drawable.border_green);
                layout_delivery.setBackgroundResource(R.drawable.border_gray);
                receivingProduct = "pickUp";
            }
        });
        // Доставка
        layout_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_pickUp.setBackgroundResource(R.drawable.border_gray);
                layout_delivery.setBackgroundResource(R.drawable.border_green);
                receivingProduct = "delivery";
            }
        });
    }

    // Оформление заказа
    private void createOrder(){
        TextView txt_surname;
        TextView txt_name;
        TextView txt_mail;
        TextView txt_tell;

        TextView txt_note;
    }

    // Вывод информации о товарах в корзине
    private void addProdctBasket() {
        grid_basket = BasketFragmentView.findViewById(R.id.grid_basket);
        grid_basket.setExpanded(true);
        basketAdapter = new BasketAdapter(Basket.getBasketList(), context);
        grid_basket.setAdapter(basketAdapter);
    }

    // Окно при пустой корзине
    private void windowNullBasket() {
        layout_windowBasketNull = BasketFragmentView.findViewById(R.id.layout_windowBasketNull);
        // Переход в корзину
        Button btn_busket = BasketFragmentView.findViewById(R.id.btn_busket);
        btn_busket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.selectItem(R.id.item_category);
            }
        });
        checkVisibleWindowNullBasket();
    }

    // Скрыть или показать диалог
    public static void checkVisibleWindowNullBasket() {
        if (Basket.getSizeBasket() == 0) {
            layout_windowBasketNull.setVisibility(View.VISIBLE);
        } else {
            layout_windowBasketNull.setVisibility(View.GONE);
        }
        basketAdapter.notifyDataSetChanged();
    }

    // конвертация цена
    private static String setPrice(int price) {
        StringBuffer newPrice = new StringBuffer(String.valueOf(price) + " ₽");
        int position = 5;
        while (newPrice.length() > position) {
            newPrice = newPrice.insert((newPrice.length() - position), " ");
            position += 4;
        }
        return newPrice.toString();
    }
}
