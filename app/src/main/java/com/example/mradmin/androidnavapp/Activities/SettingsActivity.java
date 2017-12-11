package com.example.mradmin.androidnavapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.Fragments.LogOutDialogFragment;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY_PICK = 1;

    private ImageView settingsImageView;

    //FOR EXTRA
    String imageURL;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

            }
        });

        getWindow().setStatusBarColor(Color.TRANSPARENT);

        settingsImageView = (ImageView) findViewById(R.id.settingsImageView);

        imageURL = "";
        title = "";

        //for data prefs-----------------
        final SharedPreferences dataSharedPreferences = RetrofitApplication.getDataSharedPreferences();

        if (dataSharedPreferences.contains("first_name")) {
            String firstName = dataSharedPreferences.getString("first_name", "");
            String lastName = "";
            if (dataSharedPreferences.contains("last_name")) {
                lastName = dataSharedPreferences.getString("last_name", "");
            }
            setTitle((firstName + " " + lastName).trim());
            title = (firstName + " " + lastName).trim();
        }
        if (dataSharedPreferences.contains("user_name")) {
            String userName = dataSharedPreferences.getString("user_name", "");
        }
        if (dataSharedPreferences.contains("imageHigh")) {

            String imageUrl = dataSharedPreferences.getString("imageHigh", "");

            if (!imageUrl.equals("")) {

                imageURL = imageUrl;

                Picasso.with(SettingsActivity.this)
                        .load(imageUrl)
                        .placeholder(R.mipmap.img_placeholder_avatar)
                        .into(settingsImageView);

            }

        }
        //---------------


        //final String finalImageURL = imageURL;
        //final String finalTitle = title;

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataSharedPreferences.contains("imageHigh")) {

                    String imageUrl = dataSharedPreferences.getString("imageHigh", "");

                    if (!imageUrl.equals("")) {
                        Intent intent = new Intent(SettingsActivity.this, PhotoViewActivity.class);
                        intent.putExtra("user_name", title);
                        intent.putExtra("image_url", imageURL);
                        intent.putExtra("parent", "Settings");
                        startActivity(intent);
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_name) {

            return true;

        } else if (id == R.id.action_log_out) {

            DialogFragment dialog = new LogOutDialogFragment();
            dialog.show(getSupportFragmentManager(), "LogOutDialogFragment");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();

                //for extra------------------
                final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

                String token = null;
                String id = null;

                if (sharedPreferencesUser.contains("token")) {

                    token = sharedPreferencesUser.getString("token", "");

                    if (sharedPreferencesUser.contains("user_id")) {

                        id = sharedPreferencesUser.getString("user_id", "");

                    }
                }
                //--------------------------

                uploadAvatarImage(token, id, resultUri);

            }
        }
    }


    private void uploadAvatarImage(String token, String id, Uri url){

        File filePath = new File(url.getPath());
        File file = null;

        try {
            file = new Compressor(this)
                    .setMaxWidth(400)
                    .setMaxHeight(400)
                    .setQuality(75)
                    .compressToFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody imageFilePart = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("image", file.getAbsolutePath(), imageFilePart);

        RetrofitApplication.getUserAPI().setUserAvatar(token, id, filePart).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.isSuccessful()) {

                    Profile profile = response.body();

                    settingsImageView = (ImageView) findViewById(R.id.settingsImageView);



                    //set data prefs
                    Map<String, String> keyPairs = new HashMap<>();
                    keyPairs.put("imageHigh", profile.getAvatar().getUrl());
                    keyPairs.put("imageLow", profile.getAvatar().getUrl());
                    RetrofitApplication.setSharedPreferences(RetrofitApplication.getDataSharedPreferences(), keyPairs);

                    Picasso.with(SettingsActivity.this)
                            .load(profile.getAvatar().getUrl())
                            .placeholder(R.mipmap.img_placeholder_avatar)
                            .into(settingsImageView);

                    imageURL = profile.getAvatar().getUrl();

                    Toast.makeText(SettingsActivity.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

                Toast.makeText(SettingsActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
