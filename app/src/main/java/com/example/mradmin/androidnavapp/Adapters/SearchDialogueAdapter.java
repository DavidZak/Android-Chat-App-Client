package com.example.mradmin.androidnavapp.Adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Activities.ChatActivity;
import com.example.mradmin.androidnavapp.Entities.ChatPublicInfo;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.LastSeen;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mrAdmin on 30.09.2017.
 */

public class SearchDialogueAdapter extends RecyclerView.Adapter<com.example.mradmin.androidnavapp.Adapters.SearchDialogueAdapter.SearchDialogueViewHolder> {

    private List<ChatPublicInfo> dialogueList;

    public SearchDialogueAdapter(List<ChatPublicInfo> dialogueList) {

        this.dialogueList = dialogueList;

    }

    @Override
    public SearchDialogueAdapter.SearchDialogueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_dialogue_row_layout, parent, false);

        return new com.example.mradmin.androidnavapp.Adapters.SearchDialogueAdapter.SearchDialogueViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final com.example.mradmin.androidnavapp.Adapters.SearchDialogueAdapter.SearchDialogueViewHolder holder, int position) {

        final ChatPublicInfo dialogue = dialogueList.get(position);

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

                    //String status

                    //set date status
                    //LastSeen timeSinceAgo = new LastSeen();
                    //long lastTime = LastSeen.getFormattedDate(status);
                    //String lastSeen = timeSinceAgo.getTimeAgo(lastTime);

                    //final String statusText = "Last seen " + lastSeen;

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

    }


    @Override
    public int getItemCount() {
        return dialogueList.size();
    }

    public class SearchDialogueViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public TextView dialogueContactName;
        public CircleImageView dialogueImage;

        public SearchDialogueViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;

            dialogueContactName = (TextView) itemView.findViewById(R.id.textViewDialogueFirstName);
            dialogueImage = (CircleImageView) itemView.findViewById(R.id.imageViewDialogueImage);

        }
    }
}
