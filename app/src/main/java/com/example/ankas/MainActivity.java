package com.example.ankas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ankas.Fragments.BasketFragment;
import com.example.ankas.Fragments.CategoryFragment;
import com.example.ankas.Fragments.InfoFragment;
import com.example.ankas.Fragments.MainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    static BottomNavigationView bottom_nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Верхнее меню
        toolbar();
        // Нижнее меню
        bottomNav();
    }

    // Верхнее меню
    private void toolbar() {
        ImageView img_search = findViewById(R.id.img_search);
        ImageView img_logo = findViewById(R.id.img_logo);
        img_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottom_nav.setSelectedItemId(R.id.item_main);
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    // Настройки нижнего меню
    public void bottomNav() {
        // Получение фрагмента
        int itemId = getIntent().getIntExtra("ItemFragment", 0);
        switch (itemId) {
            case R.id.item_main:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new MainFragment()).commit();
                break;
            case R.id.item_basket:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new BasketFragment()).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_content, new MainFragment()).commit();
                break;

        }
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
                                    .replace(R.id.layout_content, new InfoFragment()).commit();
                            break;
                    }
                    return true;
                }
            };

    public static void selectItem(int item) {
        bottom_nav.setSelectedItemId(item);
    }
}