package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.User;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Вращение логотипа
        ImageView img_ico = findViewById(R.id.img_ico);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
        img_ico.setAnimation(animation);
        // Считывание данных пользователей
        User.getSystemList(SplashActivity.this);
        // Считывание товаров в корзине из памяти устройства
        Basket.getSystemList(this);
        // Таймер загрузки
        final int[] tick = {0};
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tick[0]++;
                if(tick[0] >= 3){
                    // Переход на главный экран
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    timer.cancel();
                    finish();
                }
            }
        },0,1);
    }
}