package com.example.mradmin.androidnavapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Adapters.ContactAdapter;
import com.example.mradmin.androidnavapp.ContactEnum;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatProfileActivity extends AppCompatActivity {

    private ImageView groupChatProfileImage;

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private ContactAdapter contactAdapter;

    public String title = "";
    public String imageURL = "";

    private static final int GALLERY_PICK = 1;

    String chatId= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_profile);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        groupChatProfileImage = (ImageView) findViewById(R.id.imageViewGroupChatProfileImage);

        //for data prefs-----------------
        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        String token = "";
        String id = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

            }
        }

        SharedPreferences contactsSharedPreferences = RetrofitApplication.getContactsSharedPreferences();

        chatId = contactsSharedPreferences.getString("chat_id", "");

        loadChatInfo(token, id, chatId);

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

                Picasso.with(GroupChatProfileActivity.this)
                        .load(imageUrl)
                        .placeholder(R.mipmap.img_placeholder_avatar)
                        .into(groupChatProfileImage);

            }

        }
        //---------------


        final String finalImageURL = imageURL;
        final String finalTitle = title;

        groupChatProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GroupChatProfileActivity.this, PhotoViewActivity.class);
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

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
            }
        });

    }

    private void loadChatInfo(String token, String id, String chatId){

        RetrofitApplication.getChatAPI().getChatById(token, id, chatId).enqueue(new Callback<DialogueEntity>() {
            @Override
            public void onResponse(Call<DialogueEntity> call, Response<DialogueEntity> response) {

                if (response.isSuccessful()) {

                    DialogueEntity dialogueEntity = response.body();

                    List<Profile> members = dialogueEntity.getMembers();

                    contactAdapter = new ContactAdapter(members);
                    contactAdapter.contactEnum = ContactEnum.FOR_GROUP_CHAT_PROFILE;

                    recyclerView = (RecyclerView) findViewById(R.id.recyclerViewGroupProfile);
                    linearLayoutManager = new LinearLayoutManager(GroupChatProfileActivity.this);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    linearLayoutManager.setStackFromEnd(true);

                    recyclerView.setAdapter(contactAdapter);
                    //----------

                }

            }

            @Override
            public void onFailure(Call<DialogueEntity> call, Throwable t) {

            }
        });

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

                uploadGroupAvatarImage(token, id, chatId, resultUri);

            }
        }
    }

    private void uploadGroupAvatarImage(String token, String id, String chatId, Uri url){

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

        RetrofitApplication.getChatAPI().setGroupChatAvatar(token, id, chatId, filePart) .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()) {

                    String profile = response.body();

                    groupChatProfileImage = (ImageView) findViewById(R.id.imageViewGroupChatProfileImage);

                    //set data prefs
                    Map<String, String> keyPairs = new HashMap<>();
                    keyPairs.put("imageHigh", profile);
                    keyPairs.put("imageLow", profile);
                    RetrofitApplication.setSharedPreferences(RetrofitApplication.getContactsSharedPreferences(), keyPairs);

                    Picasso.with(GroupChatProfileActivity.this)
                            .load(profile)
                            .placeholder(R.mipmap.img_placeholder_avatar)
                            .into(groupChatProfileImage);

                    imageURL = profile;

                    Toast.makeText(GroupChatProfileActivity.this, "successfully uploaded", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Toast.makeText(GroupChatProfileActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_group_chat_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_change_chat_name) {

            startActivity(new Intent(GroupChatProfileActivity.this, ChangeGroupChatActivity.class));

            return true;

        } else if (id == R.id.action_add_member) {

            startActivity(new Intent(GroupChatProfileActivity.this, AddContactsToGroupChatActivity.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
