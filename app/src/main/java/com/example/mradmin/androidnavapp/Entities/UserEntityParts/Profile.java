package com.example.mradmin.androidnavapp.Entities.UserEntityParts;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by mrAdmin on 11.09.2017.
 */
@Entity
public class Profile implements Comparable<Profile> {

    @PrimaryKey(autoGenerate = true)
    public long id;

    private String userName;
    private String firstName;
    private String lastName;
    private String status;
    private String chat;
    private boolean checked;

    @Ignore
    private Avatar avatar;
    //private Avatar lowAvatar;

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    //public Avatar getLowAvatar() {
    //    return lowAvatar;
    //}

    //public void setLowAvatar(Avatar lowAvatar) {
    //    this.lowAvatar = lowAvatar;
    //}

    public Profile(String userName, String firstName, String lastName, String status, Avatar avatar) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.avatar = avatar;
        //this.lowAvatar = lowAvatar;
    }

    public Profile() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(@NonNull Profile profile) {
        return this.firstName.compareTo(profile.getFirstName());
    }
}
