package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        // Таймер смены экрана
        final Timer timer = new Timer();
        final int[] tick = {3};
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("Log - ","Tick: " + String.valueOf(tick[0]));
                if(tick[0] <= 0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timer.cancel();
                            Intent intent = new Intent(SplashScreen.this, MainScreen.class);
                            startActivity(intent);
                        }
                    });
                }
                tick[0]--;
            }
        },0,1000);
    }
}