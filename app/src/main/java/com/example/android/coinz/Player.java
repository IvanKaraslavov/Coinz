package com.example.android.coinz;

public class Player {
    private String email;
    private String password;

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
