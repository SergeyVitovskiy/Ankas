package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.example.ankas.Class.Basket;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Получение товаров из памяти устройства
        Basket.getProductSystem();

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(300);

        // Таймер смены экрана
        final Timer timer = new Timer();
        final int[] tick = {300};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("Log - ", "Tick: " + String.valueOf(tick[0]));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setProgress(300 - tick[0]);
                        if (tick[0] <= 0) {
                            timer.cancel();
                            Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                            startActivity(intent);

                        }
                    }
                });
                tick[0]--;
            }
        }, 0, 10);
    }
}