package com.example.mradmin.androidnavapp;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.example.mradmin.androidnavapp.EntityClients.ChatClient;
import com.example.mradmin.androidnavapp.EntityClients.UserClient;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrAdmin on 13.09.2017.
 */

public class RetrofitApplication extends Application {

    private OkHttpClient.Builder okHttp;
    private Retrofit.Builder builder;
    private Retrofit retrofit;

    private static UserClient userClient;
    private static ChatClient chatClient;


    private static SharedPreferences dataSharedPreferences;
    private static SharedPreferences userSharedPreferences;
    private static SharedPreferences themeSharedPreferences;
    private static SharedPreferences contactsSharedPreferences;
    private static SharedPreferences dialogueSharedPreferences;

    private static SharedPreferences chatBackgroundPreferences;

    private static SharedPreferences welcomeSharedPreferences;

    //private UserDB userdb;

    @Override
    public void onCreate() {
        super.onCreate();

        okHttp =  new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        okHttp.sslSocketFactory(getSSLSocketFactory());
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttp.addInterceptor(logging);

        builder = new Retrofit.Builder()
                .baseUrl("https://192.168.10.1:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp.build());

        retrofit = builder.build();

        userClient = retrofit.create(UserClient.class);
        chatClient = retrofit.create(ChatClient.class);


        dataSharedPreferences = getSharedPreferences("datapref", Context.MODE_PRIVATE);
        userSharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE);
        themeSharedPreferences = getSharedPreferences("themepref", Context.MODE_PRIVATE);
        contactsSharedPreferences = getSharedPreferences("contactspref", Context.MODE_PRIVATE);
        dialogueSharedPreferences = getSharedPreferences("dialoguepref", Context.MODE_PRIVATE);

        chatBackgroundPreferences = getSharedPreferences("chatBackgroundpref", Context.MODE_PRIVATE);

        welcomeSharedPreferences = getSharedPreferences("welcomepref", Context.MODE_PRIVATE);

        //userdb = Room.databaseBuilder(getApplicationContext(),
        //        UserDB.class, "userDB").build();
    }


    public static UserClient getUserAPI() {
        return userClient;
    }

    public static ChatClient getChatAPI() {
        return chatClient;
    }


    //for prefs============================
    public static SharedPreferences getDataSharedPreferences() {
        return dataSharedPreferences;
    }

    public static SharedPreferences getUserSharedPreferences() {
        return userSharedPreferences;
    }

    public static SharedPreferences getThemeSharedPreferences() {
        return themeSharedPreferences;
    }

    public static SharedPreferences getContactsSharedPreferences() {
        return contactsSharedPreferences;
    }

    public static SharedPreferences getDialogueSharedPreferences() {
        return dialogueSharedPreferences;
    }

    public static SharedPreferences getChatBackgroundPreferences() {
        return chatBackgroundPreferences;
    }

    public static SharedPreferences getWelcomeSharedPreferences() {
        return welcomeSharedPreferences;
    }

    public static void setSharedPreferences(SharedPreferences preferences, Map<String, String> keyPairs) {
        SharedPreferences.Editor editor = preferences.edit();
        for (Map.Entry<String, String> entry : keyPairs.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.commit();
    }
    //======================


    //====================== for ROOM


    /*public static UserDB getUserdb() {
        return userdb;
    }*/

    //for SSL =======================
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }

    }


}
