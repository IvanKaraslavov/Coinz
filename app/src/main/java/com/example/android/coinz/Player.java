package com.example.android.coinz;

public class Player {
    private String email;
    private String password;

    private int goldCoinsAmount;

    Player(String email, String password, int goldCoinsAmount) {
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

    public int getGoldCoinsAmount() {
        return goldCoinsAmount;
    }
}
