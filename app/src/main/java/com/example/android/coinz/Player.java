package com.example.android.coinz;

public class Player {
    private String email;
    private String password;

    private double goldCoinsAmount;
    private int coinsLeft;

    Player(String email, String password, double goldCoinsAmount, int coinsLeft) {
        this.email = email;
        this.password = password;
        this.goldCoinsAmount = goldCoinsAmount;
        this.coinsLeft = coinsLeft;
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

    public double getCoinsLeft() {
        return coinsLeft;
    }
}
