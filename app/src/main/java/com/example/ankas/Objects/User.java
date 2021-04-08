package com.example.ankas.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class User {
    public static User user;
    // Данные пользователя
    int id_;
    String name;
    String surname;
    String telephone;
    String mail;
    int id_role;
    String name_role;
    String password;

    public User(int id_, String name, String surname, String telephone, String mail, int id_role, String name_role, String password) {
        this.id_ = id_;
        this.name = name;
        this.surname = surname;
        this.telephone = telephone;
        this.mail = mail;
        this.id_role = id_role;
        this.name_role = name_role;
        this.password = password;
    }

    public static void saveUser(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("id_", User.user.getId_()).apply();
        editor.putString("name", User.user.getName()).apply();
        editor.putString("surname", User.user.getSurname()).apply();
        editor.putString("telephone", User.user.getTelephone()).apply();
        editor.putString("mail", User.user.getMail()).apply();
        editor.putInt("id_role", User.user.getId_role()).apply();
        editor.putString("name_role", User.user.getName_role()).apply();
        editor.putString("password", User.user.getPassword()).apply();
    }

    public static void getUser(Context mContext) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        User.user = new User(
                sharedPreferences.getInt("id_", 0),
                sharedPreferences.getString("name", ""),
                sharedPreferences.getString("surname", ""),
                sharedPreferences.getString("telephone", ""),
                sharedPreferences.getString("mail", ""),
                sharedPreferences.getInt("id_role", 0),
                sharedPreferences.getString("name_role", ""),
                sharedPreferences.getString("password", "")
        );
    }
    // Авторизированный ли пользователь
    public Boolean getAuthorizedUser() {
        if (User.user.getId_role() == 2)
            return true;
        else
            return false;
    }

    public int getId_() {
        return id_;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getMail() {
        return mail;
    }

    public int getId_role() {
        return id_role;
    }

    public String getName_role() {
        return name_role;
    }

    public String getPassword() {
        return password;
    }
}
