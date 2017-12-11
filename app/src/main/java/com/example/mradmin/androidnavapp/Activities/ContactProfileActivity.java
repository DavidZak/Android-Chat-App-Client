package com.example.mradmin.androidnavapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactProfileActivity extends AppCompatActivity {

    private ImageView contactProfileImage;

    public String title = "";
    public String imageURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        contactProfileImage = (ImageView) findViewById(R.id.imageViewContactProfileImage);

        imageURL = "";
        title = "";


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

        if (contactsSharedPreferences.contains("full_name")) {
            String fullName = contactsSharedPreferences.getString("full_name", "");

            setTitle(fullName);
            title = fullName;
        }
        if (contactsSharedPreferences.contains("status")) {
            String status = contactsSharedPreferences.getString("status", "");
        }
        if (contactsSharedPreferences.contains("imageHigh")) {

            String imageUrl = contactsSharedPreferences.getString("imageHigh", "");

            if (!imageUrl.equals("")) {

                imageURL = imageUrl;

                Picasso.with(ContactProfileActivity.this)
                        .load(imageUrl)
                        .placeholder(R.mipmap.img_placeholder_avatar)
                        .into(contactProfileImage);

            }

        }
        //---------------


        final String finalImageURL = imageURL;
        final String finalTitle = title;

        contactProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ContactProfileActivity.this, PhotoViewActivity.class);
                intent.putExtra("user_name", finalTitle);
                intent.putExtra("image_url", finalImageURL);
                intent.putExtra("parent", "ContactProfile");
                startActivity(intent);

            }
        });



        getWindow().setStatusBarColor(Color.TRANSPARENT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ContactProfileActivity.this, ChatActivity.class));
            }
        });

    }

    private void loadContactProfile(String token, String id, String userName) {

        RetrofitApplication.getUserAPI().getUserByUserName(token, id, userName).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful()) {

                    Profile userProfile = response.body();

                    String fullName = userProfile.getFirstName() + " " + userProfile.getLastName();

                    setTitle(fullName);
                    title = fullName;

                    String imageUrl = userProfile.getAvatar().getUrl();

                    if (!imageUrl.equals("")) {

                        imageURL = imageUrl;

                        Picasso.with(ContactProfileActivity.this)
                                .load(imageUrl)
                                .placeholder(R.mipmap.img_placeholder_avatar)
                                .into(contactProfileImage);

                    }

                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
