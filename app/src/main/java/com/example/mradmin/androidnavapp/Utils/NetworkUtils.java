package com.example.mradmin.androidnavapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by mrAdmin on 19.09.2017.
 */

public class NetworkUtils {

    public static boolean isConnected = false;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        if (!isConnected) {
                            //Log.v(LOG_TAG, "Now you are connected to Internet!");
                            Toast.makeText(context, "Internet available via Broadcast receiver", Toast.LENGTH_SHORT).show();
                            isConnected = true;

                            // do your processing here ---
                            // if you need to post any data to the server or get
                            // status
                            // update from the server
                        }
                        return true;
                    }
                }
            }
        }
        //Log.v(LOG_TAG, "You are not connected to Internet!");
        Toast.makeText(context, "Internet NOT availablle via Broadcast receiver", Toast.LENGTH_SHORT).show();
        isConnected = false;

        return false;
    }

}
