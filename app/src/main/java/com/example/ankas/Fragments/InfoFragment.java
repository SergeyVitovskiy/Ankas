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
import com.example.ankas.Objects.User;
import com.example.ankas.R;
import com.example.ankas.RegistrationActivity;
import com.example.ankas.StubInfoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class InfoFragment extends Fragment {
    Context mContext;
    View mInfoFragmentView;
    // Элементы диалога авторизации
    Dialog dialog;
    // Элементы пользователя
    static LinearLayout layout_authorization;
    static LinearLayout layout_authorizedUser;
    static TextView txt_nameUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInfoFragmentView = inflater.inflate(R.layout.info_fragment, null);
        mContext = mInfoFragmentView.getContext();
        // Кнопки меню
        btn_menu();
        layout_authorization = mInfoFragmentView.findViewById(R.id.layout_authorization);
        layout_authorizedUser = mInfoFragmentView.findViewById(R.id.layout_authorizedUser);
        txt_nameUser = mInfoFragmentView.findViewById(R.id.txt_nameUser);
        // Окно пользователя
        user();
        return mInfoFragmentView;
    }

    private void user() {
        infoUser();
        Button btn_authorization = mInfoFragmentView.findViewById(R.id.btn_authorization);
        Button btn_registration = mInfoFragmentView.findViewById(R.id.btn_registration);
        btn_authorization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                View viewDialog = View.inflate(mContext, R.layout.dialog_authorization, null);
                final LinearLayout layout_mail = viewDialog.findViewById(R.id.layout_mail);
                final LinearLayout layout_password = viewDialog.findViewById(R.id.layout_password);
                final EditText txt_mail = viewDialog.findViewById(R.id.txt_mail);
                final EditText txt_password = viewDialog.findViewById(R.id.txt_password);
                Button btn_entrance = viewDialog.findViewById(R.id.btn_entrance);
                Button btn_cancel = viewDialog.findViewById(R.id.btn_cancel);
                builder.setView(viewDialog);

                dialog = builder.create();
                // Вход
                btn_entrance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String mail = txt_mail.getText().toString();
                        String password = txt_password.getText().toString();
                        int check = 0;
                        // Майл
                        if (mail.length() > 3) {
                            check++;
                            layout_mail.setBackgroundResource(R.drawable.border_gray);
                        } else {
                            layout_mail.setBackgroundResource(R.drawable.border_red);
                        }
                        // Пароль
                        if (password.length() > 3) {
                            check++;
                            layout_password.setBackgroundResource(R.drawable.border_gray);
                        } else {
                            layout_password.setBackgroundResource(R.drawable.border_red);
                        }
                        // Проверка
                        if (check >= 2) {
                            new getAuthorization()
                                    .execute("http://anndroidankas.h1n.ru/mobile-api/User/authorization", mail, password);
                        }
                    }
                });
                // Отмена
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        btn_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    // Авторизация
    private class getAuthorization extends AsyncTask<String, Void, String> {
        String mail;
        String password;

        @Override
        protected String doInBackground(String... strings) {
            mail = strings[1];
            mail = strings[2];
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.connect();
                // Компоновка отправляемых данных
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("mail", strings[1])
                        .appendQueryParameter("password", strings[2]);
                // Отправка данных
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(builder.build().getEncodedQuery());
                writer.flush();
                writer.close();
                // Получение ответа
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "null";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (checkResult(result)) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObjectUser = jsonArray.getJSONObject(0);
                    User.user = new User(
                            jsonObjectUser.getInt("id_"),
                            jsonObjectUser.getString("name"),
                            jsonObjectUser.getString("surname"),
                            jsonObjectUser.getString("telephone"),
                            jsonObjectUser.getString("mail"),
                            jsonObjectUser.getInt("id_role"),
                            jsonObjectUser.getString("password")
                    );
                    User.saveUser(mContext);
                    dialog.cancel();
                    infoUser();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(mContext, "Неверный логин или пароль", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Информация о пользователе
    public static void infoUser() {
        if (User.user.getId_role() != 0
                && !User.user.getName().equals("")
                && !User.user.getSurname().equals("")
                && !User.user.getMail().equals("")
                && !User.user.getPassword().equals("")
                && !User.user.getTelephone().equals("")) {
            layout_authorizedUser.setVisibility(View.VISIBLE);
            layout_authorization.setVisibility(View.GONE);
            txt_nameUser.setText(User.user.getSurname() + " " + User.user.getName());
        }
    }

    // Кнопки информации меню
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

    // Проверка ответа
    private static boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}
