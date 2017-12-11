package com.example.mradmin.androidnavapp.Entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.mradmin.androidnavapp.Entities.DialogueEntities.Profile;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;

/**
 * Created by mrAdmin on 06.10.2017.
 */

@Entity
public class ChatPublicInfo implements Comparable<ChatPublicInfo> {

    @PrimaryKey
    public long idshnik;

    private String _id;
    private String name;
    private String senderId;
    private int members;
    @Ignore
    private Profile profile;
    private boolean isGroup;
    @Ignore
    private MessageEntity lastMessage;
    private int unread;

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }

    public MessageEntity getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageEntity lastMessage) {
        this.lastMessage = lastMessage;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public ChatPublicInfo(String id, String name, String senderId, int members, Profile profile, boolean isGroup, MessageEntity lastMessage, int unread) {
        this._id = id;
        this.name = name;
        this.senderId = senderId;
        this.members = members;
        this.profile = profile;
        this.isGroup = isGroup;
        this.lastMessage = lastMessage;
        this.unread = unread;
    }

    public ChatPublicInfo() {
    }

    @Override
    public int compareTo(@NonNull ChatPublicInfo chatPublicInfo) {
        if (this.getLastMessage() == null) {
            return 0;
        } else
            return chatPublicInfo.getLastMessage().getDate().compareTo(this.getLastMessage().getDate());
    }
}
