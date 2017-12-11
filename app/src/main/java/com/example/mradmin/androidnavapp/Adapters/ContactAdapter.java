package com.example.mradmin.androidnavapp.Adapters;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mradmin.androidnavapp.Activities.ChatActivity;
import com.example.mradmin.androidnavapp.Activities.ContactProfileActivity;
import com.example.mradmin.androidnavapp.Activities.ContactsActivity;
import com.example.mradmin.androidnavapp.ContactEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Profile> contactsList;

    private String titleExtra;
    private String statusExtra;

    //public boolean chatOrAdd;
    public ContactEnum contactEnum;

    public ContactAdapter(List<Profile> contactsList) {

        this.contactsList = contactsList;

    }

    @Override
    public ContactAdapter.ContactViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_row_layout, parent, false);

        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {

        final String token = RetrofitApplication.getUserSharedPreferences().getString("token","");
        final String id = RetrofitApplication.getUserSharedPreferences().getString("user_id","");
        final String groupChatId = RetrofitApplication.getContactsSharedPreferences().getString("chat_id", "");

        final Profile contact = contactsList.get(position);

        final String firstName = contact.getFirstName();
        final String lastName = contact.getLastName();
        final String status = contact.getStatus();
        final String imageUrl = contact.getAvatar().getUrl();

        final String chatId = contact.getChat();

        final String fullName = firstName + " " + lastName;

        final String userName = contact.getUserName();

        //set date status
        LastSeen timeSinceAgo = new LastSeen();
        long lastTime = LastSeen.getFormattedDate(status);
        String lastSeen = timeSinceAgo.getTimeAgo(lastTime);

        final String statusText = "Last seen " + lastSeen;

        //set holder
        holder.contactName.setText(fullName.toString());
        holder.contactStatus.setText(statusText);

        if (!imageUrl.equals("")){

            Picasso.with(holder.view.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .resize(100, 100)
                    .placeholder(R.mipmap.img_placeholder_avatar)
                    .into(holder.contactImage);

        }

        //put extra on click
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contactEnum == ContactEnum.FOR_CONTACTS) {

                    Map<String, String> keyPairs = new HashMap<>();
                    keyPairs.put("full_name", fullName);
                    keyPairs.put("status", statusText);
                    keyPairs.put("imageHigh", imageUrl);
                    keyPairs.put("imageLow", imageUrl);
                    keyPairs.put("chat_id", chatId);
                    keyPairs.put("isGroup", "false");
                    keyPairs.put("user_name", userName);
                    RetrofitApplication.setSharedPreferences(RetrofitApplication.getContactsSharedPreferences(), keyPairs);

                    Intent chatIntent = new Intent(holder.view.getContext(), ChatActivity.class);
                    holder.view.getContext().startActivity(chatIntent);

                } else if (contactEnum == ContactEnum.FOR_ADD_GROUP_CHAT){

                    if (holder.checkContact.getVisibility() == View.GONE){

                        holder.checkContact.setVisibility(View.VISIBLE);
                        contactsList.get(position).setChecked(true);

                    } else if (holder.checkContact.getVisibility() == View.VISIBLE){

                        holder.checkContact.setVisibility(View.GONE);
                        contactsList.get(position).setChecked(false);
                    }

                } else if (contactEnum == ContactEnum.FOR_GROUP_CHAT_PROFILE) {

                    //creating a popup menu
                    PopupMenu popup = new PopupMenu(holder.view.getContext(), holder.relativeLayout);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.contact_delete_from_chat_pop_up);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_delete: {

                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("userName[1]", userName);

                                    RetrofitApplication.getChatAPI().deleteMemberFromGroupChat(token, id, groupChatId, map).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {

                                                System.out.println("deleted");

                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });

                                }

                                break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactsList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public View view;

        public RelativeLayout relativeLayout;

        public TextView contactName;
        public TextView contactStatus;
        public CircleImageView contactImage;

        public ImageView checkContact;

        public ContactViewHolder(View itemView) {
            super(itemView);

            view = itemView;

            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayoutContactClick);

            contactName = (TextView) itemView.findViewById(R.id.userName);
            contactStatus = (TextView) itemView.findViewById(R.id.userStatus);
            contactImage = (CircleImageView) itemView.findViewById(R.id.imageViewContactImage);

            checkContact = (ImageView) itemView.findViewById(R.id.contactCheck);
            checkContact.setVisibility(View.GONE);

        }

    }
}
