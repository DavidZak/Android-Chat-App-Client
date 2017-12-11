package com.example.mradmin.androidnavapp.Adapters;

import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Entities.Messages.ChatPart;
import com.example.mradmin.androidnavapp.Entities.Messages.ChatTime;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;
import com.example.mradmin.androidnavapp.Fragments.MessageClickDialog;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrAdmin on 06.09.2017.
 */


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static boolean TRANSLATE = false;

    private List<ChatPart> messageList;

    String token = "";
    String id = "";

    public static boolean MORE_THEN_TWO_MEMBERS = false;

    public MessageAdapter(List<ChatPart> messageList) {

        this.messageList = messageList;

        final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

        if (sharedPreferencesUser.contains("token")) {

            token = sharedPreferencesUser.getString("token", "");

            if (sharedPreferencesUser.contains("user_id")) {

                id = sharedPreferencesUser.getString("user_id", "");

            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        RecyclerView.ViewHolder viewHolder = null;
        View v = null;

        if (viewType == ChatPart.VIEW_TYPE_MESSAGE_SENT) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_row_layout_other, parent, false);
            viewHolder = new MessageViewHolder(v);
        } else if (viewType == ChatPart.VIEW_TYPE_MESSAGE_RECEIVED) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.message_row_layout, parent, false);
            viewHolder = new MessageViewHolder(v);
        } else if (viewType == ChatPart.VIEW_TYPE_TIME) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_time_row_layout, parent, false);
            viewHolder = new TimeViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType(id);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == ChatPart.VIEW_TYPE_TIME){

            final ChatTime message = (ChatTime) messageList.get(position);

            TimeViewHolder timeViewHolder = (TimeViewHolder) holder;

            timeViewHolder.chatTime.setText(message.toString());


        } else {

            final MessageEntity message = (MessageEntity) messageList.get(position);

            final MessageViewHolder messageViewHolder = (MessageViewHolder) holder;

            messageViewHolder.messageText.setText(message.getBody().getContent());

            String date = message.getDate();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
            try {
                Date dateFormat = format.parse(date);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");

                long milliDate = dateFormat.getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(milliDate);

                messageViewHolder.messageTime.setText(simpleDateFormat.format(cal.getTime()));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (messageViewHolder.profileImage != null) {
                if (MORE_THEN_TWO_MEMBERS) {
                    messageViewHolder.profileImage.setVisibility(View.VISIBLE);
                } else {
                    messageViewHolder.profileImage.setVisibility(View.GONE);
                }
            }

            messageViewHolder.linearLayoutBoss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(messageViewHolder.view.getContext(), messageViewHolder.linearLayoutBoss);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_msg_pop_up);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_delete:
                                    //handle menu1 click
                                    break;
                                case R.id.action_to_bookmarks: {
                                    final SharedPreferences sharedPreferencesUser = RetrofitApplication.getUserSharedPreferences();

                                    String token = "";
                                    String id = "";
                                    if (sharedPreferencesUser.contains("token")) {

                                        token = sharedPreferencesUser.getString("token", "");

                                        if (sharedPreferencesUser.contains("user_id")) {

                                            id = sharedPreferencesUser.getString("user_id", "");

                                            Map<String, String> ids = new HashMap<String, String>();
                                            ids.put("msg_id", message.getId());

                                            RetrofitApplication.getUserAPI().putToBookmarks(token, id, ids).enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                                    if (response.isSuccessful()) {

                                                    }

                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                }
                                            });

                                        }
                                    }

                                }

                                break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            });

            messageViewHolder.linearLayoutBoss.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(messageViewHolder.view.getContext(), "long click", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public LinearLayout linearLayoutBoss;
        public TextView messageText;
        public CircleImageView profileImage;
        public TextView messageTime;

        public MessageViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            linearLayoutBoss = (LinearLayout) itemView.findViewById(R.id.linearLayoutBossClickable);
            messageText = (TextView) itemView.findViewById(R.id.messageTextView);
            profileImage = (CircleImageView) itemView.findViewById(R.id.imageViewMessageSender);
            messageTime = (TextView) itemView.findViewById(R.id.messageTime);

        }
    }

    public class TimeViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public TextView chatTime;

        public TimeViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            chatTime = (TextView) itemView.findViewById(R.id.textViewChatTime);

        }
    }
}

