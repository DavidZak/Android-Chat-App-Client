package com.example.mradmin.androidnavapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Adapters.MessageAdapter;
import com.example.mradmin.androidnavapp.CustomUIElements.HideShowScrollListener;
import com.example.mradmin.androidnavapp.Entities.Messages.ChatPart;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.Messages.ChatTime;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageParts.Body;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.LastSeen;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout linearLayoutBoss;

    private RecyclerView recyclerView;
    private List<ChatPart> messageList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    private TextView contactName;
    private TextView onlineStatus;
    private CircleImageView imageViewContact;

    private TextInputEditText textInputMessage;
    private ImageButton buttonSendMessage;

    private FloatingActionButton fab;

    private UpdateReceiver updateReceiver;

    private String token = "";
    private String userId = "";
    private String chatId = "";

    private long unread = 0;

    private Parcelable recyclerViewOffset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //for themes-----------------
        SharedPreferences sharedpreferences = RetrofitApplication.getThemeSharedPreferences();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //For toolbar elements----------
        contactName = (TextView) findViewById(R.id.imageViewUserName);
        onlineStatus = (TextView) findViewById(R.id.imageViewUserStatus);
        imageViewContact = (CircleImageView) findViewById(R.id.imageViewUserImage);

        //for data prefs-----------------
        SharedPreferences contactsSharedPreferences = RetrofitApplication.getContactsSharedPreferences();

        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        chatId = contactsSharedPreferences.getString("chat_id", "");

        token = "";
        userId = "";
        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                userId = sharedPreferencesUser.getString("user_id", "");

            }
        }

        String isGroupChat = "";

        if (contactsSharedPreferences.contains("full_name") && contactsSharedPreferences.contains("status") && contactsSharedPreferences.contains("imageLow") && contactsSharedPreferences.contains("isGroup")) {


            String fullName = contactsSharedPreferences.getString("full_name", "");
            contactName.setText(fullName);


            String imageUrl = contactsSharedPreferences.getString("imageLow", "");

            if (!imageUrl.equals("")) {

                Picasso.with(ChatActivity.this)
                        .load(imageUrl)
                        .centerCrop()
                        .resize(100, 100)
                        .placeholder(R.mipmap.img_placeholder_avatar)
                        .into(imageViewContact);
            }

            isGroupChat = contactsSharedPreferences.getString("isGroup", "");

            String status = contactsSharedPreferences.getString("status", "");
            onlineStatus.setText(status);

            //--------------------------
            loadChatInfo(token, userId, chatId);

        }
        //---------------

        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        linearLayoutBoss = (LinearLayout) findViewById(R.id.chatToolbarLayoutBoss);

        final String finalIsGroupChat = isGroupChat;

        linearLayoutBoss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (finalIsGroupChat.equals("true")) {

                    Intent intent = new Intent(ChatActivity.this, GroupChatProfileActivity.class);
                    startActivity(intent);

                }
                if (finalIsGroupChat.equals("false")){

                    Intent intent = new Intent(ChatActivity.this, ContactProfileActivity.class);
                    startActivity(intent);

                }

            }
        });

        textInputMessage = (TextInputEditText) findViewById(R.id.messageTextInput);
        buttonSendMessage = (ImageButton) findViewById(R.id.buttonSendMessage);
        buttonSendMessage.setEnabled(false);
        buttonSendMessage.setVisibility(View.GONE);

        textInputMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {

                    if (!buttonSendMessage.isEnabled()) {

                        buttonSendMessage.setVisibility(View.VISIBLE);
                        buttonSendMessage.setEnabled(true);

                    }

                } else {

                    if (buttonSendMessage.isEnabled()) {

                        buttonSendMessage.setVisibility(View.GONE);
                        buttonSendMessage.setEnabled(false);

                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //for init adapters and view for chat
        recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
        //----------

        final String finalToken = token;
        final String finalId = userId;
        buttonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = textInputMessage.getText().toString().trim();

                sendNewMsg(finalToken, finalId, chatId, message);

            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //recyclerView.scrollToPosition(messageList.size() - 1);
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });

        updateReceiver = new UpdateReceiver();

        recyclerViewOffset = HideShowScrollListener.recyclerViewOffset;

        recyclerView.addOnScrollListener(
                new HideShowScrollListener() {

                    @Override
                    public void onHide() {
                        fab.hide();
                    }

                    @Override
                    public void onShow() {
                        fab.show();
                    }
                });
    }

    private void sendNewMsg(String token, final String userId, String chatId, final String msgBody){

        RetrofitApplication.getUserAPI().sendMessage(token, userId, chatId, msgBody).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.isSuccessful()){

                    MessageEntity messageEntity = new MessageEntity(new Body(msgBody), userId, "");

                    //for testing
                    messageList.add(new ChatTime(messageEntity.getDate()));
                    messageList.add(messageEntity);
                    messageAdapter.notifyDataSetChanged();

                    textInputMessage.setText("");

                    recyclerView.scrollToPosition(messageList.size() - 1);
                    //-------

                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }

    private void loadChatInfo(String token, String id, String chatId){

        RetrofitApplication.getChatAPI().getChatById(token, id, chatId).enqueue(new Callback<DialogueEntity>() {
            @Override
            public void onResponse(Call<DialogueEntity> call, Response<DialogueEntity> response) {

                if (response.isSuccessful()) {

                    DialogueEntity dialogueEntity = response.body();


                    if (dialogueEntity.getMembers().size() == 2) {

                        Profile receiverProfile = dialogueEntity.getMembers().get(0);

                        String status = receiverProfile.getStatus();

                        //set date status
                        LastSeen timeSinceAgo = new LastSeen();
                        long lastTime = LastSeen.getFormattedDate(status);
                        String lastSeen = timeSinceAgo.getTimeAgo(lastTime);

                        final String statusText = "Last seen " + lastSeen;

                        onlineStatus.setText(statusText);

                        MessageAdapter.MORE_THEN_TWO_MEMBERS = false;

                    } else {
                        MessageAdapter.MORE_THEN_TWO_MEMBERS = true;
                    }

                    List<MessageEntity> messageEntityList = dialogueEntity.getMessages();

                    messageList.clear();

                    Collections.sort(messageEntityList);

                    for (int i=1;i<messageEntityList.size();i++){
                        if (messageEntityList.get(i).compareDateOnly(messageEntityList.get(i-1)) != 0) {

                            messageList.add(new ChatTime(messageEntityList.get(i).getDate()));
                        }
                            messageList.add(messageEntityList.get(i));
                    }

                    Collections.sort(messageList);

                    //for init adapters and view for chat
                    messageAdapter = new MessageAdapter(messageList);

                    messageAdapter.notifyDataSetChanged();

                    recyclerView = (RecyclerView) findViewById(R.id.chatRecyclerView);
                    linearLayoutManager = new LinearLayoutManager(ChatActivity.this);

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    linearLayoutManager.setStackFromEnd(true);

                    recyclerView.setAdapter(messageAdapter);

                    //for restore recyclerView position
                    recyclerViewOffset = HideShowScrollListener.recyclerViewOffset;
                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewOffset);
                    //----------

                }

            }

            @Override
            public void onFailure(Call<DialogueEntity> call, Throwable t) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;

        } else if (id == R.id.action_clear_history) {

            clearHistory(token, userId, chatId);

            return true;

        } else if (id == R.id.action_mute) {

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void clearHistory(String token, String id, String chatId) {

        RetrofitApplication.getChatAPI().clearChatHistory(token, id, chatId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(ChatActivity.this, "history cleared", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                Toast.makeText(ChatActivity.this, "error while clearing", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public class UpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("chat",
                    "+ ChatActivity - ðåñèâåð ïîëó÷èë ñîîáùåíèå - îáíîâèì ListView");

            loadChatInfo(token, userId, chatId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(updateReceiver, new IntentFilter(
                "com.example.action.UPDATE_RecyclerView"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(updateReceiver);
    }
}
