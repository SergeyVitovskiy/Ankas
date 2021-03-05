package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.ankas.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Отркытие стартового фрагмента
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new MainFragment()).commit();
    }
}