package com.example.mradmin.androidnavapp.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.Fragments.LogOutDialogFragment;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddContactActivity extends AppCompatActivity {

    private TextInputEditText userNameTetxInput;

    private CircleImageView userImageView;
    private TextView userNameTextView;

    private LinearLayout linearLayoutUserProfileFounded;

    private ImageButton imageButtonSearchContact;

    private ImageButton buttonAddContact;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //For toolbar------
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_contact_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //-----------

        SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        String token = "";
        String id = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

            }
        }

        titleTextView = (TextView) findViewById(R.id.titleTextView);
        titleTextView.setText("Add contact");
        buttonAddContact = (ImageButton) findViewById(R.id.buttonAddContact);

        linearLayoutUserProfileFounded = (LinearLayout) findViewById(R.id.linearLayoutUserProfileFounded);
        userImageView = (CircleImageView) findViewById(R.id.imageViewContactImage);
        userNameTextView = (TextView) findViewById(R.id.textViewUserName);
        userNameTetxInput = (TextInputEditText) findViewById(R.id.textInputEditTextUsername);
        imageButtonSearchContact = (ImageButton) findViewById(R.id.imageButtonSearchContact);

        final String finalToken = token;
        final String finalId = id;
        imageButtonSearchContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getUserByUsername(finalToken, finalId, userNameTetxInput.getText().toString());

            }
        });

    }

    private void getUserByUsername(String token, String id, String userName) {

        RetrofitApplication.getUserAPI().getUserByUserName(token, id, userName).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful()) {

                    Profile profile = response.body();

                    linearLayoutUserProfileFounded = (LinearLayout) findViewById(R.id.linearLayoutUserProfileFounded);
                    userImageView = (CircleImageView) findViewById(R.id.imageViewContactImage);
                    userNameTextView = (TextView) findViewById(R.id.textViewUserName);

                    userNameTextView.setText(profile.getFirstName() + " " + profile.getLastName());

                    if (!profile.getAvatar().getUrl().equals("")) {

                        Picasso.with(AddContactActivity.this)
                                .load(profile.getAvatar().getUrl())
                                .placeholder(R.mipmap.img_placeholder_avatar)
                                .into(userImageView);

                    }

                    linearLayoutUserProfileFounded.setVisibility(View.VISIBLE);

                }
                else {

                    linearLayoutUserProfileFounded.setVisibility(View.GONE);

                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                linearLayoutUserProfileFounded.setVisibility(View.GONE);
            }
        });

    }

}
