package com.example.mradmin.androidnavapp.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.mradmin.androidnavapp.Adapters.DialogueAdapter;
import com.example.mradmin.androidnavapp.Adapters.SearchDialogueAdapter;
import com.example.mradmin.androidnavapp.Entities.ChatPublicInfo;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ChatPublicInfo> chatList = new ArrayList<>();
    private SearchDialogueAdapter chatAdapter;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_chats);

        //For toolbar------
        Toolbar toolbar = (Toolbar) findViewById(R.id.search_chat_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------

        recyclerView = (RecyclerView) findViewById(R.id.searchDialoguesListView);
        linearLayoutManager = new LinearLayoutManager(this);

        SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        String token = "";
        String id = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

                loadChats(token, id);

            }
        }

    }

    private void loadChats(final String token, String id){

        RetrofitApplication.getChatAPI().getChats(token, id).enqueue(new Callback<List<ChatPublicInfo>>() {
            @Override
            public void onResponse(Call<List<ChatPublicInfo>> call, Response<List<ChatPublicInfo>> response) {

                if (response.isSuccessful()) {

                    List<ChatPublicInfo> chats = response.body();

                    chatList = chats;


                    if  (chatList.size() > 0) {

                        chatAdapter = new SearchDialogueAdapter(chatList);

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setAdapter(chatAdapter);
                        chatAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChatPublicInfo>> call, Throwable t) {

                // Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
