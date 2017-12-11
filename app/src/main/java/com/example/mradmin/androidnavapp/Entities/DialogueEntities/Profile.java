package com.example.mradmin.androidnavapp.Entities.DialogueEntities;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Avatar;

/**
 * Created by mrAdmin on 11.09.2017.
 */

public class Profile {

    private String name;
    private Avatar avatar;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Profile(String name, Avatar avatar) {
        this.name = name;
        this.avatar = avatar;
        //this.lowAvatar = lowAvatar;
    }

    public Profile() {
    }
}
