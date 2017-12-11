package com.example.mradmin.androidnavapp.Activities;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeGroupChatActivity extends AppCompatActivity {

    private TextInputEditText textInputEditTextGroupChatName;
    private ImageButton buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_group_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.change_group_chat_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        //for data prefs-----------------
        SharedPreferences contactsSharedPreferences = RetrofitApplication.getContactsSharedPreferences();

        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        final String chatId = contactsSharedPreferences.getString("chat_id", "");

        String token = "";
        String id = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

            }
        }

        textInputEditTextGroupChatName = findViewById(R.id.textInputGroupChatNewName);
        textInputEditTextGroupChatName.setText(RetrofitApplication.getContactsSharedPreferences().getString("full_name",""));
        buttonSave = findViewById(R.id.buttonChangeGroupChatName);
        final String finalToken = token;
        final String finalId = id;
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newName = textInputEditTextGroupChatName.getText().toString();

                changeGroupChatName(finalToken, finalId, chatId, newName);

            }
        });

    }

    private void changeGroupChatName(String token, String id, String chatId, String name) {

        RetrofitApplication.getChatAPI().setGroupChatName(token, id, chatId, name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(ChangeGroupChatActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                    Map<String, String> keyPairs = new HashMap<>();
                    keyPairs.put("full_name", textInputEditTextGroupChatName.getText().toString());
                    RetrofitApplication.setSharedPreferences(RetrofitApplication.getContactsSharedPreferences(), keyPairs);

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}
