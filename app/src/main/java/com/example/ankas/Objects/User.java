package com.example.ankas.Objects;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class User {
    public static User user;

    String surname;
    String name;
    String mail;
    String tell;
    String address;
    String details_address;
    String token;

    public User(Context context) {
        getSystemList(context);
    }

    public User(String surname, String name, String mail, String tell, String address, String details_address) {
        this.surname = surname;
        this.name = name;
        this.mail = mail;
        this.tell = tell;
        this.address = address;
        this.details_address = details_address;
    }

    // Сохранение данных в память
    public static void saveSystemList(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("surname", user.getSurname()).apply();
        editor.putString("name", user.getName()).apply();
        editor.putString("mail", user.getMail()).apply();
        editor.putString("tell", user.getTell()).apply();
        editor.putString("address", user.getAddress()).apply();
        editor.putString("details_address", user.getDetails_address()).apply();
    }

    // Получение данных из памяти
    public static void getSystemList(Context context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        user = new User(
                sharedPreferences.getString("surname", null),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("mail", null),
                sharedPreferences.getString("tell", null),
                sharedPreferences.getString("address", null),
                sharedPreferences.getString("details_address", null));
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTell() {
        return tell;
    }

    public void setTell(String tell) {
        this.tell = tell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetails_address() {
        return details_address;
    }

    public void setDetails_address(String details_address) {
        this.details_address = details_address;
    }
}
