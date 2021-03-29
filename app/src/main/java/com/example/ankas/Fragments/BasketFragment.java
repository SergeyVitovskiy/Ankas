package com.example.ankas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.Adapter.BasketAdapter;
import com.example.ankas.Components.ExpandableHeightGridView;
import com.example.ankas.Components.MySliderImage;
import com.example.ankas.MainActivity;
import com.example.ankas.Objects.Basket;
import com.example.ankas.Objects.User;
import com.example.ankas.R;

import java.util.Timer;
import java.util.TimerTask;

public class BasketFragment extends Fragment {
    static LinearLayout layout_windowBasketNull;
    View BasketFragmentView;
    Context context;
    ExpandableHeightGridView grid_basket;
    static BasketAdapter basketAdapter;
    String receivingProduct;
    // Сумма заказа
    static TextView txt_sumProductPrice;
    static TextView sumPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BasketFragmentView = inflater.inflate(R.layout.basket_fragment, null);
        context = BasketFragmentView.getContext();
        // Добавление товаров в корзину
        addProductBasket();
        // Проверка на пустую корзину
        windowNullBasket();
        // Выбор способа получения товара
        receivingProduct();
        // Оформление заказа
        createOrder();
        // Нижнее меню
        bottomMenu();
        // Сумма товаров
        txt_sumProductPrice = BasketFragmentView.findViewById(R.id.txt_sumProductPrice);
        sumPrice = BasketFragmentView.findViewById(R.id.sumPrice);
        sumPrice();
        // Возрат элемента
        return BasketFragmentView;
    }

    // Общая сумма стоимости товаров
    public static void sumPrice() {
        txt_sumProductPrice.setText("Общая стоимость товаров: " + setPrice(Basket.getSumProduct()));
        sumPrice.setText("Итог: " + setPrice(Basket.getSumProduct()));
    }

    // Выбор способа получения
    private void receivingProduct() {
        receivingProduct = "pickUp";
        final LinearLayout layout_pickUp = BasketFragmentView.findViewById(R.id.layout_pickUp);
        final LinearLayout layout_delivery = BasketFragmentView.findViewById(R.id.layout_delivery);

        final LinearLayout window_pickUp = BasketFragmentView.findViewById(R.id.window_pickUp);
        final LinearLayout window_delivery = BasketFragmentView.findViewById(R.id.window_delivery);
        // Самовывоз
        layout_pickUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_pickUp.setBackgroundResource(R.drawable.border_green);
                layout_delivery.setBackgroundResource(R.drawable.border_gray);
                receivingProduct = "pickUp";
                window_pickUp.setVisibility(View.VISIBLE);
                window_delivery.setVisibility(View.GONE);
            }
        });
        // Доставка
        layout_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_pickUp.setBackgroundResource(R.drawable.border_gray);
                layout_delivery.setBackgroundResource(R.drawable.border_green);
                receivingProduct = "delivery";
                window_pickUp.setVisibility(View.GONE);
                window_delivery.setVisibility(View.VISIBLE);
            }
        });
    }

    // Оформление заказа
    private void createOrder() {
        final LinearLayout layout_surname = BasketFragmentView.findViewById(R.id.layout_surname);
        final LinearLayout layout_name = BasketFragmentView.findViewById(R.id.layout_name);
        final LinearLayout layout_mail = BasketFragmentView.findViewById(R.id.layout_mail);
        final LinearLayout layout_tell = BasketFragmentView.findViewById(R.id.layout_tell);
        final LinearLayout layout_address = BasketFragmentView.findViewById(R.id.layout_address);
        final LinearLayout layout_details_address = BasketFragmentView.findViewById(R.id.layout_details_address);
        final TextView txt_surname = BasketFragmentView.findViewById(R.id.txt_surname);
        final TextView txt_name = BasketFragmentView.findViewById(R.id.txt_name);
        final TextView txt_mail = BasketFragmentView.findViewById(R.id.txt_mail);
        final TextView txt_tell = BasketFragmentView.findViewById(R.id.txt_tell);
        final TextView txt_note = BasketFragmentView.findViewById(R.id.txt_note);
        final TextView txt_address = BasketFragmentView.findViewById(R.id.txt_address);
        final TextView txt_details_address = BasketFragmentView.findViewById(R.id.txt_details_address);
        Button btn_order = BasketFragmentView.findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Пустая ли корзина
                if(Basket.getSizeBasket() != 0) {
                    // Считывание данных с полей ввода
                    String surname = txt_surname.getText().toString();
                    String name = txt_name.getText().toString();
                    String mail = txt_mail.getText().toString();
                    String tell = txt_tell.getText().toString();
                    String note = txt_note.getText().toString();
                    String address = txt_address.getText().toString();
                    String details_address = txt_details_address.getText().toString();
                    int check = 0;
                    // Фамилия
                    if (surname.length() >= 6) {
                        check++;
                        layout_surname.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_surname.setBackgroundResource(R.drawable.border_red);
                    }
                    // Имя
                    if (name.length() >= 6) {
                        check++;
                        layout_name.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_name.setBackgroundResource(R.drawable.border_red);
                    }
                    // Почта
                    if (mail.length() >= 10) {
                        check++;
                        layout_mail.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_mail.setBackgroundResource(R.drawable.border_red);
                    }
                    // Телефон
                    if (tell.length() >= 8) {
                        check++;
                        layout_tell.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_tell.setBackgroundResource(R.drawable.border_red);
                    }
                    // Адресс
                    if (address.length() >= 8) {
                        check++;
                        layout_address.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_address.setBackgroundResource(R.drawable.border_red);
                    }
                    // Дополнительный адресс
                    if (details_address.length() >= 8) {
                        check++;
                        layout_details_address.setBackgroundResource(R.drawable.border_gray);
                    } else {
                        layout_details_address.setBackgroundResource(R.drawable.border_red);
                    }
                    // Самовывоз
                    if (receivingProduct.equals("pickUp") && check == 4) {
                        new timerDialogLoading().execute(1000);
                        // Сохранение данных пользователей
                        User.user.setSurname(surname);
                        User.user.setName(name);
                        User.user.setMail(mail);
                        User.user.setTell(tell);
                        User.user.setAddress(address);
                        User.user.setDetails_address(details_address);
                        User.saveSystemList(context);
                    }
                    // Доставка
                    else if (receivingProduct.equals("delivery") && check == 6) {
                        new timerDialogLoading().execute(1000);
                        // Сохранение данных пользователей
                        User.user.setSurname(surname);
                        User.user.setName(name);
                        User.user.setMail(mail);
                        User.user.setTell(tell);
                        User.user.setAddress(address);
                        User.user.setDetails_address(details_address);
                        User.saveSystemList(context);

                    } else {
                        Toast.makeText(context, "Некорректное заполнение данных.", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(context, "Ваша корзина пуста", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    // Диалог оформления заказа

    // Таймер слайдера
    private class timerDialogLoading extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integer) {
            return integer[0];
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View viewItem = View.inflate(context, R.layout.dialog_loading_order, null);
            ImageView img_ico = viewItem.findViewById(R.id.img_ico);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotate_center);
            img_ico.setAnimation(animation);
            builder.setView(viewItem);
            final Dialog dialog = builder.create();
            dialog.show();
            try {
                Thread.sleep(result);
                dialog.cancel();
                Toast.makeText(context, "Заказ оформлен", Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Вывод информации о товарах в корзине
    private void addProductBasket() {
        grid_basket = BasketFragmentView.findViewById(R.id.grid_basket);
        grid_basket.setExpanded(true);
        basketAdapter = new BasketAdapter(Basket.getBasketList(), context);
        grid_basket.setAdapter(basketAdapter);
    }

    // Окно при пустой корзине
    private void windowNullBasket() {
        layout_windowBasketNull = BasketFragmentView.findViewById(R.id.layout_windowBasketNull);
        // Переход в корзину
        Button btn_busket = BasketFragmentView.findViewById(R.id.btn_busket);
        btn_busket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.selectItem(R.id.item_category);
            }
        });
        checkVisibleWindowNullBasket();
    }

    // Скрыть или показать диалог
    public static void checkVisibleWindowNullBasket() {
        if (Basket.getSizeBasket() == 0) {
            layout_windowBasketNull.setVisibility(View.VISIBLE);
        } else {
            layout_windowBasketNull.setVisibility(View.GONE);
        }
        basketAdapter.notifyDataSetChanged();
    }

    // конвертация цена
    private static String setPrice(int price) {
        StringBuffer newPrice = new StringBuffer(String.valueOf(price) + " ₽");
        int position = 5;
        while (newPrice.length() > position) {
            newPrice = newPrice.insert((newPrice.length() - position), " ");
            position += 4;
        }
        return newPrice.toString();
    }

    // Нижнее меню
    private void bottomMenu() {
        // Банки
        ImageView image_applePay = BasketFragmentView.findViewById(R.id.image_applePay);
        image_applePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.apple.com/ru/apple-pay/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_googlePay = BasketFragmentView.findViewById(R.id.image_googlePay);
        image_googlePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pay.google.com/intl/ru_ru/about/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_mastercard = BasketFragmentView.findViewById(R.id.image_mastercard);
        image_mastercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mastercard.ru/ru-ru.html"));
                startActivity(browserIntent);
            }
        });
        ImageView iamge_visa = BasketFragmentView.findViewById(R.id.iamge_visa);
        iamge_visa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.visa.com.ru/"));
                startActivity(browserIntent);
            }
        });
        // Соц сетия
        ImageView image_VK = BasketFragmentView.findViewById(R.id.image_VK);
        image_VK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/ankas_ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_YouTube = BasketFragmentView.findViewById(R.id.image_YouTube);
        image_YouTube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Inst = BasketFragmentView.findViewById(R.id.image_Inst);
        image_Inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/ankas.ru/?hl=ru"));
                startActivity(browserIntent);
            }
        });
        ImageView image_Facebook = BasketFragmentView.findViewById(R.id.image_Facebook);
        image_Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ankas.ru/"));
                startActivity(browserIntent);
            }
        });
    }
}
