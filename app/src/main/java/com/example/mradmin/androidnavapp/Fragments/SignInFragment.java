package com.example.mradmin.androidnavapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mradmin.androidnavapp.Activities.AuthActivity;
import com.example.mradmin.androidnavapp.Activities.MainActivity;
import com.example.mradmin.androidnavapp.Adapters.ContactAdapter;
import com.example.mradmin.androidnavapp.DBHelper.DBHelper;
import com.example.mradmin.androidnavapp.Entities.LoginSuccess;
import com.example.mradmin.androidnavapp.Entities.SignIn;
import com.example.mradmin.androidnavapp.Entities.UserEntity;
import com.example.mradmin.androidnavapp.EntityClients.UserClient;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.RetrofitApplication;
import com.example.mradmin.androidnavapp.Utils.InputValidation;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mrAdmin on 08.08.2017.
 */

public class SignInFragment extends Fragment {

    public static final int RC_SIGN_IN = 1;

    private InputValidation inputValidation;

    private FloatingActionButton button;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;

    private DBHelper databaseHelper;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.emailTextView);
        textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.passwordTextView);

        editTextEmail = (TextInputEditText) view.findViewById(R.id.messageEditText);
        editTextPassword = (TextInputEditText) view.findViewById(R.id.messageEditTextPassword);

        inputValidation = new InputValidation(getContext());

        //buttons------------
        button = (FloatingActionButton) view.findViewById(R.id.loginButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //sign in
                signIn();

                //clear fields
                emptyInputEditText();
            }
        });
        //---------------

        databaseHelper = new DBHelper(getContext());



        return view;
    }

    private void emptyInputEditText() {
        editTextEmail.setText(null);
        editTextPassword.setText(null);
    }

    private void signIn(){

        RetrofitApplication.getUserAPI().signIn(new SignIn(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim())).enqueue(new Callback<LoginSuccess>() {
            @Override
            public void onResponse(Call<LoginSuccess> call, Response<LoginSuccess> response) {

                if (response.isSuccessful()) {

                    LoginSuccess user = response.body();

                    System.out.println(user);
                    if (user != null) {
                        //Toast.makeText(getActivity(), user.getToken(), Toast.LENGTH_SHORT).show();

                        //if (databaseHelper.checkUser(editTextEmail.getText().toString().trim())) {


                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                        //}
                        Map<String, String> keyPairs = new HashMap<String, String>();
                        keyPairs.put("token", user.getToken());
                        keyPairs.put("user_id", user.getId());
                        RetrofitApplication.setSharedPreferences(RetrofitApplication.getUserSharedPreferences(), keyPairs);
                    }
                }

            }

            @Override
            public void onFailure(Call<LoginSuccess> call, Throwable t) {

                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();

                System.out.println("---------------" + call.toString());
            }
        });

    }

}
