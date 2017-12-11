package com.example.mradmin.androidnavapp.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Activities.MainActivity;
import com.example.mradmin.androidnavapp.Adapters.ContactAdapter;
import com.example.mradmin.androidnavapp.Adapters.DialogueAdapter;
import com.example.mradmin.androidnavapp.DBHelper.DBHelper;
import com.example.mradmin.androidnavapp.Entities.ChatPublicInfo;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageParts.Body;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Avatar;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrAdmin on 06.09.2017.
 */

public class DialoguesFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<ChatPublicInfo> chatList = new ArrayList<>();
    //private List<DialogueEntity> chatsList = new ArrayList<>();
    private DialogueAdapter chatAdapter;

    private String title;

    private DialogueEntity dialogue;

    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_dialogues, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.dialoguesListView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        chatAdapter = new DialogueAdapter(chatList);
        recyclerView.setAdapter(chatAdapter);

        //dbHelper = new DBHelper(getContext());

        //List<DialogueEntity> allChats = dbHelper.getAllChats();

        //if (allChats.size() > 0){
        //    chatList = dbHelper.getAllChats();
        //    chatAdapter = new DialogueAdapter(chatList);

        //    recyclerView.setAdapter(chatAdapter);
        //    chatAdapter.notifyDataSetChanged();
        //} else {


            //for extra------------------
            final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

            if (sharedPreferencesUser.contains("token")) {

                String token = sharedPreferencesUser.getString("token", "");

                if (sharedPreferencesUser.contains("user_id")) {

                    String id = sharedPreferencesUser.getString("user_id", "");

                    loadChats(token, id);


                }
            }
            //--------------------------
        //}

        return rootView;
    }

    private void loadChats(final String token, String id){

        RetrofitApplication.getChatAPI().getChats(token, id).enqueue(new Callback<List<ChatPublicInfo>>() {
            @Override
            public void onResponse(Call<List<ChatPublicInfo>> call, Response<List<ChatPublicInfo>> response) {

                if (response.isSuccessful()) {

                    List<ChatPublicInfo> chats = response.body();

                    chatList = chats;

                    if  (chatList.size() > 0) {



                        Collections.sort(chatList);

                        chatAdapter = new DialogueAdapter(chatList);

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
