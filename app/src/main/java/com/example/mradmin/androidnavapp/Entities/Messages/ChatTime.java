package com.example.mradmin.androidnavapp.Entities.Messages;

import com.example.mradmin.androidnavapp.Entities.Messages.ChatPart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mrAdmin on 18.10.2017.
 */

public class ChatTime extends ChatPart {

    private String date;

    @Override
    public int getType(String userId) {
        return VIEW_TYPE_TIME;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ChatTime(String date) {
        this.date = date;
    }

    public ChatTime() {
    }

    @Override
    public String toString() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");

        String resString = "";

        try {

            Date dateFormat = format.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            resString = format1.format(dateFormat);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resString;
    }
}
