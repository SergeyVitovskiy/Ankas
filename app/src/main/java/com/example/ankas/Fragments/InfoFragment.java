package com.example.ankas.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ankas.InfoActivity.ContactsActivity;
import com.example.ankas.MainActivity;
import com.example.ankas.R;
import com.example.ankas.StubInfoActivity;

public class InfoFragment extends Fragment {
    Context mContext;
    View mInfoFragmentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInfoFragmentView = inflater.inflate(R.layout.info_fragment, null);
        mContext = mInfoFragmentView.getContext();
        // Кнопки меню
        btn_menu();
        return mInfoFragmentView;
    }

    private void user(){
        Button btn_authorization = mInfoFragmentView.findViewById(R.id.btn_authorization);
        btn_authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View viewDialog = View.inflate(mContext, R.layout.dialog_authorization, null);
                EditText txt_mail = viewDialog.findViewById(R.id.txt_mail);
                EditText txt_password = viewDialog.findViewById(R.id.txt_password);
                Button btn_entrance = viewDialog.findViewById(R.id.btn_entrance);
                Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                builder.setView(viewDialog);
                // Вход
                btn_entrance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                // Отмена
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                Dialog dialog = builder.create();
                builder.show();
            }
        });
    }

    private void btn_menu() {
        Button btn_category = mInfoFragmentView.findViewById(R.id.btn_category);
        Button btn_contacts = mInfoFragmentView.findViewById(R.id.btn_contacts);
        Button btn_leaveMessage = mInfoFragmentView.findViewById(R.id.btn_leaveMessage);
        Button btn_orderStatus = mInfoFragmentView.findViewById(R.id.btn_orderStatus);
        Button btn_pick = mInfoFragmentView.findViewById(R.id.btn_pick);
        Button btn_delivery = mInfoFragmentView.findViewById(R.id.btn_delivery);
        Button btn_payment = mInfoFragmentView.findViewById(R.id.btn_payment);
        // Категории товаров
        btn_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.selectItem(R.id.item_category);
            }
        });
        // Контакты
        btn_contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ContactsActivity.class);
                mContext.startActivity(intent);
            }
        });
        // Оставить сообщение
        {
            btn_leaveMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View viewDialog = View.inflate(mContext, R.layout.dialog_leave_message, null);
                    final LinearLayout layout_date = viewDialog.findViewById(R.id.layout_date);
                    final LinearLayout layout_message = viewDialog.findViewById(R.id.layout_message);
                    final TextView eText_num = viewDialog.findViewById(R.id.eText_num);
                    final TextView eText_date = viewDialog.findViewById(R.id.eText_date);
                    final TextView eText_message = viewDialog.findViewById(R.id.eText_message);
                    Button btn_push = viewDialog.findViewById(R.id.btn_push);
                    Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                    builder.setView(viewDialog);
                    final Dialog dialog = builder.create();
                    btn_push.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int cheak = 0;
                            String num = eText_num.getText().toString();
                            String date = eText_date.getText().toString();
                            String message = eText_message.getText().toString();
                            // Фамилия
                            if (date != null && date.length() >= 3) {
                                cheak++;
                                layout_date.setBackgroundResource(R.drawable.border_gray);
                            } else {
                                layout_date.setBackgroundResource(R.drawable.border_red);
                            }
                            // Имя
                            if (message != null && message.length() >= 3) {
                                cheak++;
                                layout_message.setBackgroundResource(R.drawable.border_gray);
                            } else {
                                layout_message.setBackgroundResource(R.drawable.border_red);
                            }
                            if (cheak >= 2) {
                                // Отправка запроса
                                Toast.makeText(mContext, "Запрос принят.", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            } else {
                                Toast.makeText(mContext, "Не все поля заполнены корректно.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
        }
        // Знать состояние заказа
        {
            btn_orderStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    View viewDialog = View.inflate(mContext, R.layout.dialog_order_status, null);
                    final LinearLayout layout_num = viewDialog.findViewById(R.id.layout_num);
                    final EditText eText_num = viewDialog.findViewById(R.id.eText_num);
                    Button btn_push = viewDialog.findViewById(R.id.btn_push);
                    Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                    builder.setView(viewDialog);
                    final Dialog dialog = builder.create();
                    btn_push.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int cheak = 0;
                            String num = eText_num.getText().toString();
                            // Фамилия
                            if (num != null && num.length() >= 3) {
                                cheak++;
                                layout_num.setBackgroundResource(R.drawable.border_gray);
                            } else {
                                layout_num.setBackgroundResource(R.drawable.border_red);
                            }
                            if (cheak >= 1) {
                                // Отправка запроса
                                Toast.makeText(mContext, "Запрос принят.", Toast.LENGTH_LONG).show();
                                dialog.cancel();
                            } else {
                                Toast.makeText(mContext, "Не все поля заполнены корректно.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
        }
        // Самовывоз
        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StubInfoActivity.class);
                mContext.startActivity(intent);
            }
        });
        // Доставка
        btn_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StubInfoActivity.class);
                mContext.startActivity(intent);
            }
        });
        // Оплата
        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, StubInfoActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
}
