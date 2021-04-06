package com.example.ankas;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ankas.Fragments.InfoFragment;
import com.example.ankas.Objects.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegistrationActivity extends AppCompatActivity {

    LinearLayout layout_name;
    LinearLayout layout_surname;
    LinearLayout layout_mail;
    LinearLayout layout_tell;
    LinearLayout layout_password;
    LinearLayout layout_passwordReplay;

    TextView txt_name;
    TextView txt_surname;
    TextView txt_mail;
    TextView txt_tell;
    TextView txt_password;
    TextView txt_passwordReplay;

    Button btn_push;
    Button btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Ткстовые поля
        txt_name = findViewById(R.id.txt_name);
        txt_surname = findViewById(R.id.txt_surname);
        txt_mail = findViewById(R.id.txt_mail);
        txt_tell = findViewById(R.id.txt_tell);
        txt_password = findViewById(R.id.txt_password);
        txt_passwordReplay = findViewById(R.id.txt_passwordReplay);
        // Рамки
        layout_name = findViewById(R.id.layout_name);
        layout_surname = findViewById(R.id.layout_surname);
        layout_mail = findViewById(R.id.layout_mail);
        layout_tell = findViewById(R.id.layout_tell);
        layout_password = findViewById(R.id.layout_password);
        layout_passwordReplay = findViewById(R.id.layout_passwordReplay);
        // Кнопки
        btn_push = findViewById(R.id.btn_push);
        btn_cancel = findViewById(R.id.btn_cancel);
        // Отправка данных
        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = txt_name.getText().toString();
                String surname = txt_surname.getText().toString();
                String mail = txt_mail.getText().toString();
                String tell = txt_tell.getText().toString();
                String password = txt_password.getText().toString();
                String passwordReplay = txt_passwordReplay.getText().toString();
                int check = 0;
                // Имя
                if (name.length() > 2) {
                    check++;
                    layout_name.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_name.setBackgroundResource(R.drawable.border_red);
                }
                // Фамилия
                if (surname.length() > 2) {
                    check++;
                    layout_surname.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_surname.setBackgroundResource(R.drawable.border_red);
                }
                // Почта
                if (mail.length() > 2) {
                    check++;
                    layout_mail.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_mail.setBackgroundResource(R.drawable.border_red);
                }
                // Телефон
                if (tell.length() > 2) {
                    check++;
                    layout_tell.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_tell.setBackgroundResource(R.drawable.border_red);
                }
                // Пароль
                if (password.length() > 2) {
                    check++;
                    layout_password.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_password.setBackgroundResource(R.drawable.border_red);
                }
                // Повтор пароля
                if (passwordReplay.length() > 2) {
                    check++;
                    layout_passwordReplay.setBackgroundResource(R.drawable.border_gray);
                } else {
                    layout_passwordReplay.setBackgroundResource(R.drawable.border_red);
                }
                // Проверка всех условий
                if (check >= 6 ){
                    if (!password.equals(passwordReplay)) {
                        layout_password.setBackgroundResource(R.drawable.border_red);
                        layout_passwordReplay.setBackgroundResource(R.drawable.border_red);
                        Toast.makeText(RegistrationActivity.this, "Пароли не совпадают", Toast.LENGTH_LONG).show();
                    } else {
                        new pushUserInfo().execute("http://anndroidankas.h1n.ru/mobile-api/User/registration",
                                name,
                                surname,
                                mail,
                                tell,
                                password);
                    }
                }
            }
        });
        // Закрытие Формы
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private class pushUserInfo extends AsyncTask<String, Void, String> {
        String name;
        String surname;
        String mail;
        String telephone;
        String password;
        @Override
        protected String doInBackground(String... strings) {
            name = strings[1];
            surname = strings[2];
            mail = strings[3];
            telephone = strings[4];
            password = strings[5];

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.connect();
                // Компоновка параметров
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("name", name)
                        .appendQueryParameter("surname", surname)
                        .appendQueryParameter("mail", mail)
                        .appendQueryParameter("telephone", telephone)
                        .appendQueryParameter("password",password);
                // Отправка результата
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                writer.write(builder.build().getEncodedQuery());
                writer.flush();
                writer.close();
                // Получение результата
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null)
                    result.append(line);
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
            if(checkResult(result)){
                User.user = new User(0,
                        name,
                        surname,
                        mail,
                        telephone,
                        2,
                        password);
                User.saveUser(RegistrationActivity.this);
                finish();
                InfoFragment.infoUser();
            }
        }
    }

    // Проверка ответа
    private static boolean checkResult(String result) {
        if (!result.equals("null") || !result.equals("[]") || !result.equals("") || !result.equals("{}"))
            return true;
        else return false;
    }
}