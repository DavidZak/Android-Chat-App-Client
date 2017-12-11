package com.example.mradmin.androidnavapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mrAdmin on 04.10.2017.
 */

public class AutoRun extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            // получили boot_completed - запустили FoneService
            context.startService(new Intent(context, FoneService.class));
            Log.i("chat", "+ AutoRun - отработал");
        }
    }
}
