package com.example.android.coinz;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String email;
    private String password;

    private double goldCoinsAmount;
    private int coinsLeft;

    private boolean dogBought;
    private boolean catBought;
    private boolean koalaBought;
    private int currentAvatar;

    private boolean boosterBought;

    private int steps;
    private double currCoinValue;

    private boolean newNotifications;
    private List<String> notifications;

    private String wallet;
    private String map;

    Player(String email, String password, double goldCoinsAmount, int coinsLeft) {
        this.email = email;
        this.password = password;
        this.goldCoinsAmount = goldCoinsAmount;
        this.coinsLeft = coinsLeft;
        this.dogBought = false;
        this.catBought = false;
        this.koalaBought = false;
        this.boosterBought = false;
        this.currentAvatar = 1;
        this.steps = 0;
        this.currCoinValue = 0;
        this.notifications = new ArrayList<>();
        this.newNotifications = false;
        this.wallet = "";
        this.map = "";
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

    public boolean isBoosterBought() {
        return boosterBought;
    }

    public int getSteps() {
        return steps;
    }

    public double getCurrCoinValue() {
        return currCoinValue;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public boolean isNewNotifications() {
        return newNotifications;
    }

    public String getWallet() {
        return wallet;
    }

    public String getMap() {
        return map;
    }
}
