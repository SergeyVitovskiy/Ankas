package com.example.ankas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasketFragmentView = inflater.inflate(R.layout.basket_fragment, null);
        context = BasketFragmentView.getContext();
        addProdctBasket();
        windowNullBasket();
        return BasketFragmentView;
    }
    // Вывод информации о товарах в корзине
    private void addProdctBasket(){
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
}
