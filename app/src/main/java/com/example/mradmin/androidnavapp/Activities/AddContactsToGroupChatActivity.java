package com.example.mradmin.androidnavapp.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Adapters.ContactAdapter;
import com.example.mradmin.androidnavapp.ContactEnum;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrAdmin on 07.10.2017.
 */

public class AddContactsToGroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Profile> contactList = new ArrayList<>();

    private ImageButton buttonSearchContact;
    private TextView textViewTitleText;
    private TextInputEditText textInputSearchContacts;
    private ImageButton closeSearchButton;
    private ImageButton buttonAddGroupChat;

    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group_chat);

        //For toolbar------
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_new_group_chat_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //RETROFIT---------------
        //for extra------------------
        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        final String chatId = RetrofitApplication.getContactsSharedPreferences().getString("chat_id", "");
        String token = "";
        String id = "";

        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

                loadContacts(token, id);
            }
        }
        //--------------------------

        isSearch = false;
        textViewTitleText = (TextView) findViewById(R.id.textViewTitleTextAddNewGroupChat);
        textInputSearchContacts = (TextInputEditText) findViewById(R.id.textInputSearchAddNewGroupChat);
        buttonAddGroupChat = (ImageButton) findViewById(R.id.buttonAddGroupChat);

        final String finalToken = token;
        final String finalId = id;
        buttonAddGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < contactList.size(); i++) {
                    if (contactList.get(i).isChecked()) {

                        map.put("" + i, contactList.get(i).getUserName());

                    }
                }
                addNewContactsToGroupChat(finalToken, finalId, chatId, map);

            }
        });

        closeSearchButton = (ImageButton) findViewById(R.id.closeSearchAddNewGroupChat);

        buttonSearchContact = (ImageButton) findViewById(R.id.searchAddNewGroupChat);
        buttonSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableSearch();

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.addGroupChatContactsListView);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadContacts(String token, String id) {

        RetrofitApplication.getUserAPI().getContacts(token, id).enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

                if (response.isSuccessful()) {

                    List<Profile> users = response.body();

                    //sort by firstName --------
                    users.sort(new Comparator<Profile>() {
                        @Override
                        public int compare(Profile userEntity, Profile t1) {
                            return userEntity.getFirstName().compareTo(t1.getFirstName());
                        }
                    });
                    // -------------------------

                    contactList = users;
                    ContactAdapter contactAdapter = new ContactAdapter(contactList);

                    contactAdapter.contactEnum = ContactEnum.FOR_ADD_GROUP_CHAT;

                    recyclerView.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

                Toast.makeText(com.example.mradmin.androidnavapp.Activities.AddContactsToGroupChatActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void addNewContactsToGroupChat(String token, String id, String chatId, Map<String, String> map) {

        RetrofitApplication.getChatAPI().addMembersToGroupChat(token, id, chatId, map).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(com.example.mradmin.androidnavapp.Activities.AddContactsToGroupChatActivity.this, "Members added", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(com.example.mradmin.androidnavapp.Activities.AddContactsToGroupChatActivity.this, "Failed to add members", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void disableSearch() {
        textInputSearchContacts.setVisibility(View.GONE);
        closeSearchButton.setVisibility(View.GONE);

        textViewTitleText.setVisibility(View.VISIBLE);
        buttonSearchContact.setVisibility(View.VISIBLE);
        buttonAddGroupChat.setVisibility(View.VISIBLE);

        isSearch = false;
    }

    public void enableSearch() {
        textInputSearchContacts.setVisibility(View.VISIBLE);
        closeSearchButton.setVisibility(View.VISIBLE);

        textViewTitleText.setVisibility(View.GONE);
        buttonSearchContact.setVisibility(View.GONE);
        buttonAddGroupChat.setVisibility(View.GONE);

        isSearch = true;
    }

    //for back arrow ----------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (!isSearch) {
                    NavUtils.navigateUpFromSameTask(this);
                } else {
                    disableSearch();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //for back click ------------------------
    @Override
    public void onBackPressed() {
        if (!isSearch) {
            super.onBackPressed();
        } else {
            disableSearch();
        }
    }
}
