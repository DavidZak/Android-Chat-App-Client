package com.example.mradmin.androidnavapp.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mrAdmin on 09.08.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "userData";

    // User table name
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_CHATS = "chats";

    //Common Table Columns names
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE = "image";

    //Chats Table Columns names
    private static final String COLUMN_CHAT_NAME = "chat_name";
    private static final String COLUMN_CHAT_RECEIVER = "receiver";

    //Contacts Table Columns names
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_FIRST_NAME = "first_name";
    private static final String COLUMN_USER_LAST_NAME = "last_name";

    // create Contacts table sql query
    private String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT, "
            + COLUMN_USER_FIRST_NAME +"TEXT," + COLUMN_USER_LAST_NAME + "TEXT," + COLUMN_IMAGE + "TEXT" + ")";

    // create Chats table sql query
    private String CREATE_CHATS_TABLE = "CREATE TABLE " + TABLE_CHATS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_CHAT_NAME + " TEXT, "
            + COLUMN_IMAGE + "TEXT, " + COLUMN_CHAT_RECEIVER + "TEXT" + ")";

    // drop Contacts table sql query
    private String DROP_CONTACTS_TABLE = "DROP TABLE IF EXISTS " + TABLE_CONTACTS;

    // drop Chats table sql query
    private String DROP_CHATS_TABLE = "DROP TABLE IF EXISTS " + TABLE_CHATS;

    /**
     * Constructor
     *
     * @param context
     */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CHATS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_CONTACTS_TABLE);
        db.execSQL(DROP_CHATS_TABLE);

        // Create tables again
        onCreate(db);

    }


    //FOR CONTACTS TABLE _________________________________________

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(UserEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getProfile().getUserName());
        values.put(COLUMN_USER_FIRST_NAME, user.getProfile().getFirstName());
        values.put(COLUMN_USER_LAST_NAME, user.getProfile().getLastName());
        //values.put(COLUMN_IMAGE, user.getProfile().getHighAvatar().getUrl());

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */

    public List<UserEntity> getAllUsers() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_USER_NAME,
                COLUMN_USER_FIRST_NAME,
                COLUMN_USER_LAST_NAME,
                COLUMN_IMAGE
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<UserEntity> userList = new ArrayList<UserEntity>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_CONTACTS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserEntity user = new UserEntity();
                //user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                user.getProfile().setUserName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.getProfile().setFirstName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_FIRST_NAME)));
                user.getProfile().setLastName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LAST_NAME)));
                //user.getProfile().getHighAvatar().setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(UserEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getProfile().getUserName());
        values.put(COLUMN_USER_FIRST_NAME, user.getProfile().getFirstName());
        values.put(COLUMN_USER_LAST_NAME, user.getProfile().getLastName());
        //values.put(COLUMN_IMAGE, user.getProfile().getHighAvatar().getUrl());

        // updating row
        db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(UserEntity user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param userName
     * @return true/false
     */
    public boolean checkUser(String userName) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {userName};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_CONTACTS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_CONTACTS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }


    //FOR CHATS TABLE _________________________________________

    /**
     * This method is to create user record
     *
     * @param dialogueEntity
     */
    public void addChat(DialogueEntity dialogueEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_CHAT_NAME, dialogueEntity.getName());
       // values.put(COLUMN_CHAT_RECEIVER, dialogueEntity.getReceivers().get(0));

        // Inserting Row
        db.insert(TABLE_CHATS, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */

    public List<DialogueEntity> getAllChats() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ID,
                COLUMN_CHAT_NAME,
                COLUMN_CHAT_RECEIVER
        };
        // sorting orders
        String sortOrder =
                COLUMN_CHAT_NAME + " ASC";
        List<DialogueEntity> dialogueEntityList = new ArrayList<DialogueEntity>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_CHATS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DialogueEntity dialogueEntity = new DialogueEntity();
                //dialogueEntity.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                // Adding user record to list
                dialogueEntityList.add(dialogueEntity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return dialogueEntityList;
    }

    /**
     * This method to update user record
     *
     * @param dialogueEntity
     */
    public void updateChat(DialogueEntity dialogueEntity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(COLUMN_CHAT_NAME, dialogueEntity.getName());
        //values.put(COLUMN_CHAT_RECEIVER, dialogueEntity.getReceivers().get(0));

        // updating row
        db.update(TABLE_CHATS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(dialogueEntity.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param dialogueEntity
     */
    public void deleteChat(DialogueEntity dialogueEntity) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_CHATS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(dialogueEntity.getId())});
        db.close();
    }
}

