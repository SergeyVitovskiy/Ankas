package com.example.ankas.Objects;

public class Characteristic {
    String nameCharacteristic;
    String characteristic;

    public Characteristic(String nameCharacteristic, String characteristic) {
        this.nameCharacteristic = nameCharacteristic;
        this.characteristic = characteristic;
    }

    public String getNameCharacteristic() {
        return nameCharacteristic;
    }

    public String getCharacteristic() {
        return characteristic;
    }
}
