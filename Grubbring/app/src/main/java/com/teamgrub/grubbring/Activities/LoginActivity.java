package com.teamgrub.grubbring.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamgrub.grubbring.R;

import org.json.JSONObject;


import Constants.SharedPreferencesKeys;
import Models.Ring;
import Utilities.APIUtilities;
import Utilities.ResponseHandlerUtilities;
import VolleyCallbackInterfaces.APISuccessCallback;
import VolleyCallbackInterfaces.LoginSuccessCallback;

public class LoginActivity extends Activity {

    APIUtilities apiUtilities;

    EditText usernameEditText;
    EditText passwordEditText;
    TextView registerTextView;
    TextView loginFailedMsgTextView;
    Button loginButton;

    ImageView logoImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        apiUtilities = new APIUtilities(this);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
        loginFailedMsgTextView = (TextView) findViewById(R.id.loginFailedMsgTextView);
        logoImageView = (ImageView) findViewById(R.id.imageView);
        loginButton = (Button)findViewById(R.id.loginButton);

        SharedPreferences prfs = getSharedPreferences(SharedPreferencesKeys.SESSION_COOKIE_SHARED_PREFS_KEY, Context.MODE_PRIVATE);
        if(prfs != null){
            String sessionCookie = prfs.getString(SharedPreferencesKeys.SESSION_COOKIE_KEY, "");
            if(!sessionCookie.equals("")){
                //navigate to profile screen
                Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                startActivity(intent);
            }
        }

        loginButton.setClickable(false);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals("")){
                    loginButton.setClickable(false);
                    loginButton.setEnabled(false);
                    loginButton.setAlpha(.5f);
                }else{
                    loginButton.setClickable(true);
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1.0f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void login(View v) {
        final String username = usernameEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        String invalidUserEntryMessage = checkValidUserEntry(username, password);

        if(!invalidUserEntryMessage.equals("")){
            loginFailedMsgTextView.setVisibility(View.VISIBLE);
            loginFailedMsgTextView.setText(invalidUserEntryMessage);
        } else{
            apiUtilities.loginAPI(username, password, new LoginSuccessCallback() {
                @Override
                public void onAPICallSuccess(boolean didLogin) {
                    //navigate to profile screen
                    if(didLogin){
                        loginFailedMsgTextView.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, BaseActivity.class);
                        startActivity(intent);
                    }else{//login failed, call login attempts remaining api
                        apiUtilities.getLoginAttemptsAPI(username, new APISuccessCallback() {
                            @Override
                            public void onAPICallSuccess(JSONObject response) {
                                String loginFailedMessage = "";
                                if(response != null){
                                    loginFailedMessage = ResponseHandlerUtilities.getLoginAttempts(response);
                                }else{
                                    loginFailedMessage = "Server Error. Please try again in few minutes.";
                                }
                                loginFailedMsgTextView.setVisibility(View.VISIBLE);
                                loginFailedMsgTextView.setText(loginFailedMessage);
                            }
                        });
                    }
                }
            });
        }
    }

    private String checkValidUserEntry(String username, String password){
        String invalidUserEntry = "";

        if(username.equals("") && password.equals("")){
            invalidUserEntry = "Please enter a username and password.";
        }else if(username.equals("") && !password.equals("")){
            invalidUserEntry = "Please enter a username.";
        }else if(!username.equals("") && password.equals("")){
            invalidUserEntry = "Please enter a password.";
        }

        return invalidUserEntry;
    }

    public void register(View v) {
        Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
        startActivity(intent);
    }

}
