package com.example.ankas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.ankas.Fragments.BasketFragment;
import com.example.ankas.Fragments.CategoryFragment;
import com.example.ankas.Fragments.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    static BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Нижнее меню
        bottomNav();
    }

    // Настройки нижнего меню
    public void bottomNav() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.layout_content, new MainFragment()).commit();

        bottom_nav = findViewById(R.id.bottom_nav);
        bottom_nav.setOnNavigationItemSelectedListener(navSelectedListener);
    }

    // Обработчик нажатий нижнего меню
    BottomNavigationView.OnNavigationItemSelectedListener navSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_main:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_content, new MainFragment()).commit();
                            break;
                        case R.id.item_category:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_content, new CategoryFragment()).commit();
                            break;
                        case R.id.item_basket:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_content, new BasketFragment()).commit();
                            break;
                        case R.id.item_user:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.layout_content, new MainFragment()).commit();
                            break;
                    }
                    return true;
                }
            };

    public static void selectItem(int item) {
        bottom_nav.setSelectedItemId(item);
    }
}