package com.example.mradmin.androidnavapp.Activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.FoneService;
import com.example.mradmin.androidnavapp.Fragments.AppInfoDialogFragment;
import com.example.mradmin.androidnavapp.Fragments.DialoguesFragment;
import com.example.mradmin.androidnavapp.Fragments.LogOutDialogFragment;
import com.example.mradmin.androidnavapp.Fragments.MessageClickDialog;
import com.example.mradmin.androidnavapp.NetworkChangeReceiver;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView titleTextViewMain;

    //Toolbar elems
    private ImageButton searchDialoguesButton;
    private ImageButton addDialogueButton;

    //Header elems
    private CircleImageView userImage;
    private TextView userName;
    private TextView userEmail;

    private BroadcastReceiver receiver;
    private IntentFilter filter;

    private ProgressBar progressBarWaitLoad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.startService(new Intent(this, FoneService.class));

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, ContactsActivity.class));


            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //for header
        View hView = navigationView.getHeaderView(0);
        userImage = (CircleImageView) hView.findViewById(R.id.imageViewUserImage);
        userName = (TextView) hView.findViewById(R.id.textViewUserName);
        userEmail = (TextView) hView.findViewById(R.id.textViewUserEmail);

        //for data prefs-----------------
        SharedPreferences dataSharedPreferences = RetrofitApplication.getDataSharedPreferences();

        if (dataSharedPreferences.contains("first_name")) {
            String firstName = dataSharedPreferences.getString("first_name", "");
            String lastName = "";
            if (dataSharedPreferences.contains("last_name")) {
                lastName = dataSharedPreferences.getString("last_name", "");
            }
            userName.setText(firstName + " " + lastName);

            if (dataSharedPreferences.contains("user_name")) {
                String userName = dataSharedPreferences.getString("user_name", "");
                userEmail.setText(userName);
            }
            if (dataSharedPreferences.contains("imageLow")) {

                String imageUrl = dataSharedPreferences.getString("imageLow", "");

                if (!imageUrl.equals("")) {

                    Picasso.with(MainActivity.this)
                            .load(imageUrl)
                            .placeholder(R.mipmap.img_placeholder_avatar)
                            .into(userImage);

                }

            }
        } else {
            //for extra------------------
            final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

            if (sharedPreferencesUser.contains("token")) {

                String token = sharedPreferencesUser.getString("token", "");

                if (sharedPreferencesUser.contains("user_id")) {

                    String id = sharedPreferencesUser.getString("user_id", "");

                    getUser(token, id);
                }
            }
            //--------------------------
        }
        //---------------

        //Toolbar elems------------
        searchDialoguesButton = (ImageButton) findViewById(R.id.searchDialogues);
        searchDialoguesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, SearchChatsActivity.class));

            }
        });

        addDialogueButton = (ImageButton) findViewById(R.id.addDialogues);
        addDialogueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, AddGroupChatActivity.class));

            }
        });
        //------------------------

        final Fragment fragment = new DialoguesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, fragment).commit();

        //for network check----------
        titleTextViewMain = (TextView) findViewById(R.id.titleTextViewMain);

        progressBarWaitLoad = (ProgressBar) findViewById(R.id.mainProgressBar);

        filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");

        receiver = new NetworkChangeReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (NetworkUtils.isNetworkAvailable(context)) {

                    titleTextViewMain.setText(R.string.app_name);
                    progressBarWaitLoad.setVisibility(View.GONE);

                } else {

                    titleTextViewMain.setText(R.string.network_waiting);
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

    private void getUser(String token, String id){

        RetrofitApplication.getUserAPI().getMyProfile(token, id).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful()) {

                    Profile profile = response.body();

                    if (profile != null){

                        Toast.makeText(MainActivity.this, profile.getUserName(), Toast.LENGTH_SHORT).show();
                        userImage = (CircleImageView) findViewById(R.id.imageViewUserImage);
                        userName = (TextView) findViewById(R.id.textViewUserName);
                        userEmail = (TextView) findViewById(R.id.textViewUserEmail);

                        userName.setText(profile.getFirstName().toString());
                        userEmail.setText(profile.getUserName().toString());

                        //set data prefs
                        Map<String, String> keyPairs = new HashMap<>();
                        keyPairs.put("first_name", profile.getFirstName());
                        keyPairs.put("user_name", profile.getUserName());
                        keyPairs.put("last_name", profile.getLastName());
                        keyPairs.put("status", profile.getStatus());
                        keyPairs.put("imageHigh", profile.getAvatar().getUrl());
                        keyPairs.put("imageLow", profile.getAvatar().getUrl());
                        RetrofitApplication.setSharedPreferences(RetrofitApplication.getDataSharedPreferences(), keyPairs);

                        if (profile.getAvatar().getUrl() != null) {

                            if (!profile.getAvatar().getUrl().equals("")) {

                                Picasso.with(MainActivity.this)
                                        .load(profile.getAvatar().getUrl())
                                        .placeholder(R.mipmap.img_placeholder_avatar)
                                        .into(userImage);
                            }
                        }
                    }

                }

                else {

                    Toast.makeText(MainActivity.this, "profile == null", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_invite_contacts) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Try our new app Albatross available on Google Play");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        }  else if (id == R.id.nav_contacts) {

                startActivity(new Intent(MainActivity.this, ContactsActivity.class));   //for test

        } else if (id == R.id.nav_settings) {

            startActivity(new Intent(MainActivity.this, SettingsActivity.class));   //for test

        } else if (id == R.id.nav_unitem) {
            DialogFragment dialog = new AppInfoDialogFragment();
            dialog.show(getSupportFragmentManager(), "App info");
        }

        item.setCheckable(false); //good for now

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
