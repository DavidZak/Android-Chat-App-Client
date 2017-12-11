package com.example.mradmin.androidnavapp.Entities;

/**
 * Created by mrAdmin on 12.09.2017.
 */

public class SignIn {

    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SignIn(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public SignIn() {
    }
}
