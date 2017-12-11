package com.example.mradmin.androidnavapp.EntityClients;

import com.example.mradmin.androidnavapp.Entities.LoginSuccess;
import com.example.mradmin.androidnavapp.Entities.Messages.MessageEntity;
import com.example.mradmin.androidnavapp.Entities.SignIn;
import com.example.mradmin.androidnavapp.Entities.UserEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
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
 * Created by mrAdmin on 06.09.2017.
 */

public interface UserClient {

    @GET("/users")
    Call<List<UserEntity>> users();

    @POST("/signin")
    Call<LoginSuccess> signIn(@Body SignIn signIn);

    //@GET("/user/{id}")
    //Call<Profile> getUserById(@Header("Authorization") String token, @Path("id") String id);

    @Multipart
    @PUT("/avatar/")
    Call<Profile> setUserAvatar(@Header("Authorization") String token, @Header("Id") String userId, @Part MultipartBody.Part highAvatar);

    @GET("/contacts/")
    Call<List<Profile>> getContacts(@Header("Authorization") String token, @Header("Id") String id);

    @GET("/bookmarks")
    Call<List<MessageEntity>> getBookmarks(@Header("Authorization") String token, @Header("Id") String id);

    @FormUrlEncoded
    @PUT("/bookmarks")
    Call<ResponseBody> putToBookmarks(@Header("Authorization") String token, @Header("Id") String id, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("/msg/chat/{chat_id}")
    Call<String> sendMessage(@Header("Authorization") String token, @Header("Id") String id, @Path("chat_id") String chatId, @Field("msg") String msgBody);

    @GET("/my")
    Call<Profile> getMyProfile(@Header("Authorization") String token, @Header("Id") String id);

    @GET("/user/{userName}")
    Call<Profile> getUserByUserName(@Header("Authorization") String token, @Header("Id") String id, @Path("userName") String userName);

    @PUT("/invite/{user_name}")
    Call<ResponseBody> sendInviteToContact(@Header("Authorization") String token, @Header("Id") String userId, @Path("user_name") String userName);

}
