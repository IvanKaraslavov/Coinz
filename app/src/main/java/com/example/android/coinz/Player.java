package com.example.android.coinz;

public class Player {
    private String email;
    private String password;

    private double goldCoinsAmount;
    private int coinsLeft;

    private boolean dogBought;
    private boolean catBought;
    private boolean koalaBought;
    private int currentAvatar;

    Player(String email, String password, double goldCoinsAmount, int coinsLeft) {
        this.email = email;
        this.password = password;
        this.goldCoinsAmount = goldCoinsAmount;
        this.coinsLeft = coinsLeft;
        this.dogBought = false;
        this.catBought = false;
        this.koalaBought = false;
        this.currentAvatar = 1;
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

    public boolean isCatBought() {
        return catBought;
    }

    public boolean isKoalaBought() {
        return koalaBought;
    }

    public boolean isDogBought() {
        return dogBought;
    }

    public int getCurrentAvatar() {
        return currentAvatar;
    }
}
