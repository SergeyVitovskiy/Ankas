package com.example.ankas.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.R;

public class InfoFragment extends Fragment {
    Context context;
    View InfoFragmentView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        InfoFragmentView = inflater.inflate(R.layout.info_fragment, null);
        context = InfoFragmentView.getContext();
        // Нижнее меню
        bottomMenu();
        return InfoFragmentView;
    }

    // Нижнее меню
    private void bottomMenu() {
        // Банки
        ImageView image_applePay = InfoFragmentView.findViewById(R.id.image_applePay);
        image_applePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apple.com/ru/apple-pay/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_googlePay = InfoFragmentView.findViewById(R.id.image_googlePay);
        image_googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pay.google.com/intl/ru_ru/about/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_mastercard = InfoFragmentView.findViewById(R.id.image_mastercard);
        image_mastercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mastercard.ru/ru-ru.html"));
                startActivity(browserIntent);
            }
        });
        ImageView iamge_visa = InfoFragmentView.findViewById(R.id.iamge_visa);
        iamge_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.visa.com.ru/"));
                startActivity(browserIntent);
            }
        });
        // Соц сетия
        ImageView image_VK = InfoFragmentView.findViewById(R.id.image_VK);
        image_VK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/ankas_ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_YouTube = InfoFragmentView.findViewById(R.id.image_YouTube);
        image_YouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Inst = InfoFragmentView.findViewById(R.id.image_Inst);
        image_Inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ankas.ru/?hl=ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Facebook = InfoFragmentView.findViewById(R.id.image_Facebook);
        image_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ankas.ru/"));
                startActivity(browserIntent);
            }
        });
    }
}
