package com.example.mradmin.androidnavapp.EntityClients;

import com.example.mradmin.androidnavapp.Entities.ChatPublicInfo;
import com.example.mradmin.androidnavapp.Entities.DialogueEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by mrAdmin on 07.10.2017.
 */

public interface ChatClient {

    @GET("/chats/")
    Call<List<ChatPublicInfo>> getChats(@Header("Authorization") String token, @Header("Id") String id);

    @GET("/chat/{chat_id}")
    Call<DialogueEntity> getChatById(@Header("Authorization") String token, @Header("Id") String id, @Path("chat_id") String chatId);

    @FormUrlEncoded
    @POST("/chat/")
    Call<ResponseBody> createNewChat(@Header("Authorization") String token, @Header("Id") String id, @FieldMap Map<String, String> map);

    @Multipart
    @PUT("/chat/{chat_id}/avatar")
    Call<String> setGroupChatAvatar(@Header("Authorization") String token, @Header("Id") String userId, @Path("chat_id") String chatId, @Part MultipartBody.Part highAvatar);

    // PUT: /chat/:chatId/name + body:{ name: newName }  ->  update avatar of the chat
    @FormUrlEncoded
    @PUT("/chat/{chat_id}/name")
    Call<ResponseBody> setGroupChatName(@Header("Authorization") String token, @Header("Id") String userId, @Path("chat_id") String chatId, @Field("name") String name);

    // /chat/:chatId/members + body:{ userName[index]: userName }  ->  remove members
    @FormUrlEncoded
    @PUT("/chat/{chat_id}/members/delete")
    Call<ResponseBody> deleteMemberFromGroupChat(@Header("Authorization") String token, @Header("Id") String userId, @Path("chat_id") String chatId, @FieldMap Map<String, String> map);

    // PUT: /chat/:chatId/members + body:{  userName[index]: userName }  ->  add new members in the chat
    @FormUrlEncoded
    @PUT("/chat/{chat_id}/members")
    Call<ResponseBody> addMembersToGroupChat(@Header("Authorization") String token, @Header("Id") String userId, @Path("chat_id") String chatId, @FieldMap Map<String, String> map);

    // DELETE: /chat/:chatId/history  ->  remove all msgs (clear history)
    @PUT("/chat/{chat_id}/history")
    Call<ResponseBody> clearChatHistory (@Header("Authorization") String token, @Header("Id") String userId, @Path("chat_id") String chatId);
}
