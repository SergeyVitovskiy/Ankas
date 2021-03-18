package com.example.ankas.Components;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.ankas.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MySliderImage extends RelativeLayout {

    ImageView image_slider;
    ImageView image_left;
    ImageView image_right;
    LinearLayout layout_point;
    int positionSlider;
    List<String> listImage;

    int tick;

    public MySliderImage(Context context) {
        super(context);
        init(context);
    }

    public MySliderImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MySliderImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public MySliderImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        View viewItem = View.inflate(context, R.layout.my_slideri_image, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        image_slider = viewItem.findViewById(R.id.image_slider);
        image_left = viewItem.findViewById(R.id.image_left);
        image_right = viewItem.findViewById(R.id.image_right);
        layout_point = viewItem.findViewById(R.id.layout_point);
    }

    // Настройка компонента
    public void setListImage(List<String> list) {
        listImage = new ArrayList<>();
        listImage = list;
        sliderImage();
    }

    // Установка таймера
    public void setTimer(int tick) {
        this.tick = tick;
        // Запуск таймера
        new timerSlider().execute();
    }

    // Изображения
    private void sliderImage() {
        ImageView image_left = findViewById(R.id.image_left);
        ImageView image_right = findViewById(R.id.image_right);
        updateImageBanner();
        // Перелистывание изображения
        // Перелистывание назад
        image_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (positionSlider <= 0) {
                    positionSlider = listImage.size() - 1;
                } else {
                    positionSlider--;
                }
                updateImageBanner();
            }
        });
        // Перелистывание вперед
        image_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (positionSlider >= listImage.size() - 1) {
                    positionSlider = 0;
                } else {
                    positionSlider++;
                }
                updateImageBanner();
            }
        });
    }

    // Обновление изображения
    private void updateImageBanner() {
        image_slider = findViewById(R.id.image_slider);
        layout_point = findViewById(R.id.layout_point);
        Animation slideMovement = AnimationUtils.loadAnimation(getContext(), R.anim.slider_movement);
        image_slider.setAnimation(slideMovement);
        image_slider.animate();
        Picasso.get().load("http://anndroidankas.h1n.ru/image/" + listImage.get(positionSlider))
                .placeholder(R.drawable.banner)
                .into(image_slider);
        Log.d("positionSlider", " - - " + positionSlider);
        // Поинты
        layout_point.removeAllViews();
        for (int position = 0; position < listImage.size(); position++) {
            ImageView imageView = new ImageView(getContext());
            if (position == positionSlider) {
                imageView.setImageResource(R.drawable.point_true);
            } else {
                imageView.setImageResource(R.drawable.point_false);
            }
            imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
            imageView.setPadding(5, 0, 5, 0);
            layout_point.addView(imageView);
        }
    }

    // Таймер слайдера
    private class timerSlider extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Thread.sleep(tick);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (positionSlider >= listImage.size() - 1) {
                positionSlider = 0;
            } else {
                positionSlider++;
            }
            updateImageBanner();
            new timerSlider().execute();
        }
    }
}
