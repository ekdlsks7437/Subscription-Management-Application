package com.byu.cs428.subscriptionmanager.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.byu.cs428.subscriptionmanager.Cache.Cache;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.model.domain.UserSettings;
import com.byu.cs428.subscriptionmanager.presenter.AuthenticationPresenter;
import com.byu.cs428.subscriptionmanager.presenter.LoginPresenter;
import com.byu.cs428.subscriptionmanager.presenter.SettingsPresenter;
import com.byu.cs428.subscriptionmanager.view.fragment.SubscriptionFragment;


public class LoginActivity<T> extends AppCompatActivity implements AuthenticationPresenter.AuthenticationView, SettingsPresenter.SettingsView {


    private final String LOG_TAG = "MainActivity";
    private Toast infoToast;
    private EditText emailEdt;
    private EditText passwordEdt;
    private Button loginBtn;
    private Button registerBtn;
    private TextView errorTV;

    LoginPresenter presenter;
    SettingsPresenter settingsPresenter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // initialize variables
        emailEdt = findViewById(R.id.edt_email);
        passwordEdt = findViewById(R.id.edt_password);
        loginBtn = findViewById(R.id.btn_login);
        registerBtn = findViewById(R.id.btn_register);
        errorTV = findViewById(R.id.error_pass_email);

        presenter = new LoginPresenter(this,emailEdt.getText().toString(),passwordEdt.getText().toString());
        settingsPresenter = new SettingsPresenter(this);

        // initialize click listener for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    errorTV.setVisibility(View.VISIBLE);
                } else {
                    loginUser(email, password);
                }

            }
        });

        // initialize click lister for register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivities((Class<T>) SignupActivity.class);
            }
        });
    }

    private void loginUser(String email, String password) {
        if (presenter.validateLogin(email, password)) {
            presenter.loginUser(email,password);
        } else {
            errorTV.setVisibility(View.VISIBLE);
        }
    }

    private void switchActivities(Class<T> newClass) {
        Intent newActivity = new Intent(this, newClass);
        startActivity(newActivity);
    }

    @Override
    public void authenticated() {
        settingsPresenter.getUserSettings();
        clearInfoMessage();
        Toast.makeText(LoginActivity.this, "Signed in!",Toast.LENGTH_LONG).show();
//        getSupportFragmentManager().beginTransaction().add(R.id.subscription_fragment, new Fragment()).commit();
    }

    @Override
    public void displayInvalidLogin() {
        //TODO: maybe a way to clear this when they correct it? or if they successfully login then it will not matter?
        errorTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateErrorTv(String errorString) {
        errorTV.setText(errorString);
    }

    @Override
    public void displayFailureMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG, message);
        errorTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayInformationMessage(String message) {
        infoToast = Toast.makeText(this,message,Toast.LENGTH_LONG);
        infoToast.show();
    }

    @Override
    public void displayExceptionMessage(String message, Exception exception) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        Log.e(LOG_TAG,message,exception);
        errorTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearInfoMessage() {
        infoToast.cancel();
    }

    @Override
    public void settingsFinished() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}