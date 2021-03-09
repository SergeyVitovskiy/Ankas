package com.example.ankas.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.MainActivity;
import com.example.ankas.R;

public class BasketFragment extends Fragment {
    View BasketFragmentView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasketFragmentView = inflater.inflate(R.layout.basket_fragment, null);
        context = BasketFragmentView.getContext();

        // Переход в корзину
        Button btn_busket = BasketFragmentView.findViewById(R.id.btn_busket);
        btn_busket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.selectItem(R.id.item_category);
            }
        });

        return BasketFragmentView;
    }
}
