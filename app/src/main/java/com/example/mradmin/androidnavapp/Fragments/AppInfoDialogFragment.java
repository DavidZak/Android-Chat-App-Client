package com.example.mradmin.androidnavapp.Fragments;

import android.app.Application;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.example.mradmin.androidnavapp.R;

/**
 * Created by mrAdmin on 23.09.2017.
 */

public class AppInfoDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Messenger for android v. " + Build.VERSION.RELEASE)
                .setIcon(R.mipmap.ic_launcher_round)
                .setTitle("Messenger")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
