package com.example.mradmin.androidnavapp.Entities;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.mradmin.androidnavapp.Entities.DialogueEntities.Profile;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;

import java.util.List;

/**
 * Created by mrAdmin on 06.09.2017.
 */

@Entity
public class DialogueEntity {

    @PrimaryKey
    public long idshnik;

    private String _id;
    private String senderId;
    @Ignore
    private List<MessageEntity> messages;
    @Ignore
    private List<com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile> members;
    @Ignore
    private Profile profile;
    private int unread;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    private boolean isGroup;

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public List<MessageEntity> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageEntity> messages) {
        this.messages = messages;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public List<com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile> getMembers() {
        return members;
    }

    public void setMembers(List<com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile> members) {
        this.members = members;
    }

    public DialogueEntity(String id, String senderId, List<MessageEntity> messages, List<com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile> members, Profile profile, boolean isGroup, int unread) {
        this._id = id;
        this.senderId = senderId;
        this.messages = messages;
        this.members = members;
        this.profile = profile;

        this.isGroup = isGroup;
        this.unread = unread;
    }

    public DialogueEntity() {
    }
}
