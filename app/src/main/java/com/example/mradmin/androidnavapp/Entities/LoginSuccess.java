package com.example.mradmin.androidnavapp.Entities;

/**
 * Created by mrAdmin on 12.09.2017.
 */

public class LoginSuccess {

    private boolean success;
    private String id;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginSuccess(boolean success, String id, String token) {
        this.success = success;
        this.id = id;
        this.token = token;
    }

    public LoginSuccess() {
    }
}
