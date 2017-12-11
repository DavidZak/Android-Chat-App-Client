package com.example.mradmin.androidnavapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mradmin.androidnavapp.Activities.MainActivity;
import com.example.mradmin.androidnavapp.Entities.UserEntity;
import com.example.mradmin.androidnavapp.Entities.UserEntityParts.Profile;
import com.example.mradmin.androidnavapp.R;
import com.example.mradmin.androidnavapp.Utils.InputValidation;

/**
 * Created by mrAdmin on 08.08.2017.
 */

public class SignUpFragment extends Fragment {

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private FloatingActionButton buttonSignUp;

    private InputValidation inputValidation;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        inputValidation = new InputValidation(getActivity());

        textInputLayoutName = (TextInputLayout) view.findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) view.findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) view.findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) view.findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) view.findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) view.findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) view.findViewById(R.id.textInputEditTextConfirmPassword);

        buttonSignUp = (FloatingActionButton) view.findViewById(R.id.loginButtonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("click");

                //sign up
                startActivity(new Intent(getActivity(), MainActivity.class));

                emptyInputEditText();
            }
        });

        return view;
    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}

