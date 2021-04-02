package com.example.ankas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.InfoActivity.ContactsActivity;
import com.example.ankas.MainActivity;
import com.example.ankas.R;

public class InfoFragment extends Fragment {
    Context mContext;
    View mInfoFragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInfoFragmentView = inflater.inflate(R.layout.info_fragment, null);
        mContext = mInfoFragmentView.getContext();
        // Кнопки меню
        btn_menu();
        return mInfoFragmentView;
    }

    private void btn_menu() {
        Button btn_category = mInfoFragmentView.findViewById(R.id.btn_category);
        Button btn_contacts = mInfoFragmentView.findViewById(R.id.btn_contacts);
        // Категории товаров
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.selectItem(R.id.item_category);
            }
        });
        // Контакты
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactsActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
}
