package com.example.mradmin.androidnavapp.Entities;

/**
 * Created by mrAdmin on 27.09.2017.
 */

public class RequestSuccess {

    private boolean success;

    public RequestSuccess(boolean success) {
        this.success = success;
    }

    public RequestSuccess() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
