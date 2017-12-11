package com.example.mradmin.androidnavapp.Entities.Messages;

import android.support.annotation.NonNull;
import android.text.format.DateUtils;

import com.example.mradmin.androidnavapp.Entities.Messages.MessageParts.Body;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by mrAdmin on 06.09.2017.
 */

public class MessageEntity extends ChatPart {

    private String _id;
    private Body body;
    private String sender;
    private String date;

    @Override
    public int getType(String userId) {
        if (this.sender.equals(userId)) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else
            return VIEW_TYPE_MESSAGE_RECEIVED;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public MessageEntity() {
    }

    public MessageEntity(Body body, String sender, String date) {
        this.body = body;
        this.sender = sender;
        this.date = date;

    }
}

