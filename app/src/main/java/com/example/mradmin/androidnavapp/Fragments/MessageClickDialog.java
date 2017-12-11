package com.example.mradmin.androidnavapp.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.R;

/**
 * Created by mrAdmin on 19.10.2017.
 */

public class MessageClickDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] choices = {"Forward", "Reply", "Remove", "Add to Bookmarks"};

        builder.setTitle("Message"); // заголовок для диалога

        builder.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // TODO Auto-generated method stub
                Toast.makeText(getActivity().getApplicationContext(),
                        "Выбранный вариант: " + choices[item],
                        Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

}
