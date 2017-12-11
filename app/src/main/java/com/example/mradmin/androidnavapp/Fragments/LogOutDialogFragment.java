package com.example.mradmin.androidnavapp.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.mradmin.androidnavapp.Activities.AuthActivity;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

public class LogOutDialogFragment extends DialogFragment{

    SharedPreferences sharedPreferencesUser;
    SharedPreferences sharedPreferencesData;
    SharedPreferences sharedPreferencesContact;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();
        sharedPreferencesData = RetrofitApplication.getDataSharedPreferences();
        sharedPreferencesContact = RetrofitApplication.getContactsSharedPreferences();

        builder.setMessage("Are you sure you want log out?")
                .setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!


                        //for extra------------------
                        SharedPreferences.Editor editor = sharedPreferencesUser.edit();
                        editor.remove("token");
                        editor.remove("user_id");
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferencesData.edit();
                        editor1.remove("first_name");
                        editor1.remove("user_name");
                        editor1.remove("last_name");
                        editor1.remove("status");
                        editor1.remove("imageHigh");
                        editor1.remove("imageLow");
                        editor1.commit();

                        SharedPreferences.Editor editor2 = sharedPreferencesContact.edit();
                        editor2.remove("first_name");
                        editor2.remove("last_name");
                        editor2.remove("status");
                        editor2.remove("imageHigh");
                        editor2.remove("imageLow");
                        editor2.commit();

                        //--------------------------


                        startActivity(new Intent(getActivity(), AuthActivity.class));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
