package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class BasketScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_screen);
        LinearLayout linear_basket = findViewById(R.id.linear_basket);
        for (int i = 0;i<10;i++){
            View viewItemBasket = View.inflate(this, R.layout.item_basket, null);
            linear_basket.addView(viewItemBasket);
        }
    }
}