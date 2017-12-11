package com.example.mradmin.androidnavapp.Entities.UserEntityParts;


/**
 * Created by mrAdmin on 15.09.2017.
 */

public class Avatar {

    private String avatarId;
    //private String avatarIdLow;
    private String url;
    //private String urlLow;

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    //public String getAvatarIdLow() {
    //    return avatarIdLow;
    //}

    //public void setAvatarIdLow(String avatarIdLow) {
    //    this.avatarIdLow = avatarIdLow;
    //}


    //public String getUrlLow() {
    //    return urlLow;
    //}

    //public void setUrlLow(String urlLow) {
    //    this.urlLow = urlLow;
    //}

    public String getUrl() {
        return url;
    }

    public void setUrl(String urlHigh) {
        this.url = urlHigh;
    }

    public Avatar() {
    }

    public Avatar(String avatarIdHigh, String urlHigh) {
        this.avatarId = avatarIdHigh;
        //this.avatarIdLow = avatarIdLow;
        this.url = urlHigh;
        //this.urlLow = urlLow;
    }
}
