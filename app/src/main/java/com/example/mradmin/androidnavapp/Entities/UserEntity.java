package com.example.mradmin.androidnavapp.Entities;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;

import java.util.List;

/**
 * Created by mrAdmin on 06.09.2017.
 */

public class UserEntity {

    private String _id;
    private Profile profile;
    //private List<DialogueEntity> chats;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public UserEntity() {
    }

    public UserEntity(Profile profile) {
        this.profile = profile;
    }
}
