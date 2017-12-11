package com.example.mradmin.androidnavapp.Entities.Messages;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by mrAdmin on 18.10.2017.
 */

public abstract class ChatPart implements Comparable<ChatPart> {

    public static final int VIEW_TYPE_MESSAGE_SENT = 0;
    public static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;
    public static final int VIEW_TYPE_TIME = 2;

    public abstract String getDate();

    public abstract int getType(String userId);

    @Override
    public int compareTo(@NonNull ChatPart o) {

        int result = 0;

        ChatPart anotherEntity = (ChatPart) o;
        String anotherDate = anotherEntity.getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");

        try {

            Date dateFormat = format.parse(getDate());

            try {
                Date dateFormat1 = format.parse(anotherDate);

                if (dateFormat.compareTo(dateFormat1) > 0) {
                    result = 1;
                }
                else if (dateFormat.compareTo(dateFormat1) < 0) {
                    result = -1;
                } else {
                    result = 0;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int compareDateOnly(@NonNull ChatPart o) {
        int result = 0;

        ChatPart anotherEntity = (ChatPart) o;
        String anotherDate = anotherEntity.getDate();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date dateFormat = format.parse(getDate());

            try {
                Date dateFormat1 = format.parse(anotherDate);

                if (dateFormat.compareTo(dateFormat1) > 0) {
                    result = 1;
                }
                else if (dateFormat.compareTo(dateFormat1) < 0) {
                    result = -1;
                } else {
                    result = 0;
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return result;
    }
}
