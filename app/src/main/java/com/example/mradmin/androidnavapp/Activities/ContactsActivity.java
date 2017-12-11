package com.example.mradmin.androidnavapp.Activities;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.migration.Migration;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Adapters.ContactAdapter;
import com.example.mradmin.androidnavapp.ContactEnum;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Avatar;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.NetworkChangeReceiver;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    List<Profile> contactList = new ArrayList<>();

    private ImageButton buttonAddContact;
    private ImageButton buttonSearchContact;
    private TextView textViewTitleText;
    private TextInputEditText textInputSearchContacts;
    private ImageButton closeSearchButton;

    private boolean isSearch = false;

    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private ProgressBar progressBarWaitLoad;

    private boolean contactsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //For toolbar------
        Toolbar toolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------

        isSearch = false;
        textViewTitleText = (TextView) findViewById(R.id.textViewTitleTextContacts);
        textInputSearchContacts = (TextInputEditText) findViewById(R.id.textInputSearchContacts);
        closeSearchButton = (ImageButton) findViewById(R.id.closeSearchContacts);
        buttonSearchContact = (ImageButton) findViewById(R.id.searchContacts);
        buttonSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                enableSearch();

            }
        });

        buttonAddContact = (ImageButton) findViewById(R.id.addContacts);
        buttonAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ContactsActivity.this, AddContactActivity.class));

            }
        });

        for (int i=0;i<100;i++) {
            contactList.add(new Profile("davo", "davon", "mihalich", "2017-05-13T13:30:13.333Z", new Avatar("", "")));
        }
        recyclerView = (RecyclerView) findViewById(R.id.contactsListView);

        ContactAdapter contactAdapter = new ContactAdapter(contactList);

        recyclerView.setAdapter(contactAdapter);

        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        //for extra------------------
        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        contactsLoaded = false;

        String token = "";
        String id = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

                if (!contactsLoaded)
                    loadContacts(token, id);
            }
        }
        //--------------------------

        //for network check----------
        progressBarWaitLoad = (ProgressBar) findViewById(R.id.contactsProgressBar);

        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        final String finalToken = token;
        final String finalId = id;
        receiver = new NetworkChangeReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (NetworkUtils.isNetworkAvailable(context)) {

                    textViewTitleText.setText("Contacts");
                    progressBarWaitLoad.setVisibility(View.GONE);

                    if (!contactsLoaded)
                        loadContacts(finalToken, finalId);


                    //--------------------------


                } else {

                    textViewTitleText.setText(R.string.network_waiting);
                    progressBarWaitLoad.setVisibility(View.VISIBLE);
                }
            }
        };
        //--------------------------
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    public void onPause() {
        super.onPause();

        try{
            unregisterReceiver(receiver);
        } catch (Exception e) {

        }
    }

    private void loadContacts(String token, String id){

        RetrofitApplication.getUserAPI().getContacts(token, id).enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {

                if (response.isSuccessful()) {

                    List<Profile> users = response.body();


                    //sort by firstName --------
                    //users.sort(new Comparator<Profile>() {
                    //    @Override
                    //    public int compare(Profile userEntity, Profile t1) {
                    //        return userEntity.getFirstName().compareTo(t1.getFirstName());
                    //    }
                    //});
                    // -------------------------
                    contactList = users;

                    Collections.sort(contactList);

                    ContactAdapter contactAdapter = new ContactAdapter(contactList);

                    contactAdapter.contactEnum = ContactEnum.FOR_CONTACTS;

                    recyclerView.setAdapter(contactAdapter);
                    contactAdapter.notifyDataSetChanged();

                    contactsLoaded = true;
                }
            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {

                Toast.makeText(ContactsActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void disableSearch(){
        textInputSearchContacts.setVisibility(View.GONE);
        closeSearchButton.setVisibility(View.GONE);

        textViewTitleText.setVisibility(View.VISIBLE);
        buttonSearchContact.setVisibility(View.VISIBLE);
        buttonAddContact.setVisibility(View.VISIBLE);

        isSearch = false;
    }

    public void enableSearch(){
        textInputSearchContacts.setVisibility(View.VISIBLE);
        closeSearchButton.setVisibility(View.VISIBLE);
        buttonAddContact.setVisibility(View.GONE);

        textViewTitleText.setVisibility(View.GONE);
        buttonSearchContact.setVisibility(View.GONE);

        isSearch = true;
    }

    //for back arrow ----------------------
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (!isSearch){
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
        if (!isSearch){
            super.onBackPressed();
        } else {
            disableSearch();
        }
    }
}
