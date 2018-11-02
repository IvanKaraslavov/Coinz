package com.example.android.coinz;

public class Player {
    private String email;
    private String password;

    private double goldCoinsAmount;

    Player(String email, String password, double goldCoinsAmount) {
        this.email = email;
        this.password = password;
        this.goldCoinsAmount = goldCoinsAmount;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public double getGoldCoinsAmount() {
        return goldCoinsAmount;
    }
}
