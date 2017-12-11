package com.example.mradmin.androidnavapp.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Activities.ChatActivity;
import com.example.mradmin.androidnavapp.Activities.MainActivity;
import com.example.mradmin.androidnavapp.Entities.ChatPublicInfo;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.LastSeen;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrAdmin on 06.09.2017.
 */

public class DialogueAdapter extends RecyclerView.Adapter<DialogueAdapter.DialogueViewHolder> {

    private List<ChatPublicInfo> dialogueList;

    public DialogueAdapter(List<ChatPublicInfo> dialogueList) {

        this.dialogueList = dialogueList;

    }

    @Override
    public DialogueAdapter.DialogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialogue_row_layout, parent, false);

        return new DialogueAdapter.DialogueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final DialogueAdapter.DialogueViewHolder holder, int position) {

        final ChatPublicInfo dialogue = dialogueList.get(position);

        String id = RetrofitApplication.getUserSharedPreferences().getString("user_id", "");


        if (dialogue.isGroup()) {

            holder.imageViewChatType.setImageResource(R.drawable.ic_group);

        } else if (!dialogue.isGroup()) {

            holder.imageViewChatType.setVisibility(View.GONE);

        }

        String chatName = dialogue.getProfile().getName();
        holder.dialogueContactName.setText(chatName);

        if (dialogue.getProfile().getAvatar().getUrl() != null && !dialogue.getProfile().getAvatar().getUrl().equals("")) {

            Picasso.with(holder.view.getContext())
                    .load(dialogue.getProfile().getAvatar().getUrl())
                    .centerCrop()
                    .resize(100, 100)
                    .placeholder(R.mipmap.img_placeholder_avatar)
                    .into(holder.dialogueImage);

        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> keyPairs = new HashMap<>();
                keyPairs.put("full_name", holder.dialogueContactName.getText().toString());

                if (dialogue.isGroup()) {

                    keyPairs.put("status", dialogue.getMembers() + " members");

                } else {

                    keyPairs.put("status", dialogue.getMembers() + " members");

                }

                keyPairs.put("imageHigh", dialogue.getProfile().getAvatar().getUrl());
                keyPairs.put("imageLow", dialogue.getProfile().getAvatar().getUrl());
                keyPairs.put("chat_id", dialogue.getId());
                keyPairs.put("isGroup", String.valueOf(dialogue.isGroup()));
                RetrofitApplication.setSharedPreferences(RetrofitApplication.getContactsSharedPreferences(), keyPairs);

                Intent chatIntent = new Intent(holder.view.getContext(), ChatActivity.class);
                holder.view.getContext().startActivity(chatIntent);

            }
        });


        String sender = "";
        String message = "";
        String time = "";

        if (dialogue.getLastMessage() != null) {

            sender = dialogue.getLastMessage().getSender() + ": ";

            message = dialogue.getLastMessage().getBody().getContent();

            //set date status
            String date = dialogue.getLastMessage().getDate();

            LastSeen timeSinceAgo = new LastSeen();
            long lastTime = LastSeen.getFormattedDate(date);
            time = timeSinceAgo.getTimeAgo(lastTime);
        }

        holder.dialogueLastMessageSender.setText(sender);
        holder.dialogueLastMessage.setText(message);
        holder.dialogueLastMessageTime.setText(time);

        int unread = dialogue.getUnread();
        if (unread > 0) {

            holder.textViewUnread.setText(String.valueOf(unread));
            holder.unreadCountContainerLayout.setVisibility(View.VISIBLE);

        } else {

            holder.unreadCountContainerLayout.setVisibility(View.GONE);

        }

    }


    @Override
    public int getItemCount() {
        return dialogueList.size();
    }

    public class DialogueViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public TextView dialogueContactName;
        public TextView dialogueLastMessage;
        public TextView dialogueLastMessageTime;
        public CircleImageView dialogueImage;
        public TextView dialogueLastMessageSender;
        public TextView textViewUnread;
        public LinearLayout unreadCountContainerLayout;
        public CircleImageView imageViewChatType;

        public DialogueViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            dialogueContactName = (TextView) itemView.findViewById(R.id.textViewDialogueName);
            dialogueLastMessage = (TextView) itemView.findViewById(R.id.textViewMessage);
            dialogueLastMessageTime = (TextView) itemView.findViewById(R.id.textViewMessageTime);
            dialogueImage = (CircleImageView) itemView.findViewById(R.id.imageViewDialogueImage);
            dialogueLastMessageSender = (TextView) itemView.findViewById(R.id.textViewMessageSender);
            textViewUnread = (TextView) itemView.findViewById(R.id.textViewUnreadMessageCount);
            unreadCountContainerLayout = (LinearLayout) itemView.findViewById(R.id.unreadCountContainerLayout);
            imageViewChatType = (CircleImageView) itemView.findViewById(R.id.imageViewChatType);

        }
    }
}
