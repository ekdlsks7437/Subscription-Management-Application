package com.byu.cs428.subscriptionmanager.view;

import androidx.appcompat.app.AppCompatActivity;
import com.byu.cs428.subscriptionmanager.R;
import com.byu.cs428.subscriptionmanager.presenter.AuthenticationPresenter;
import com.byu.cs428.subscriptionmanager.presenter.LoginPresenter;
import com.byu.cs428.subscriptionmanager.presenter.SettingsPresenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity implements AuthenticationPresenter.AuthenticationView, SettingsPresenter.SettingsView {

    public final String LOG_TAG = "SignupActivity";
    private Toast infoToast;

    private EditText emailEdt;
    private EditText passwordEdt;
    private EditText passwordVerifyEdt;
    private Button loginBtn;
    private Button registerBtn;
    private TextView errorTV;

    LoginPresenter presenter;

    SettingsPresenter settingsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailEdt = findViewById(R.id.edt_email);
        passwordEdt = findViewById(R.id.edt_password);
        passwordVerifyEdt = findViewById(R.id.edt_password_verification);
        loginBtn = findViewById(R.id.btn_back);
        registerBtn = findViewById(R.id.btn_register);
        errorTV = findViewById(R.id.error_pass_email);


        presenter = new LoginPresenter(this,emailEdt.getText().toString(),passwordEdt.getText().toString());
        settingsPresenter = new SettingsPresenter(this);

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email = emailEdt.getText().toString();
                String password = passwordEdt.getText().toString();
                String passwordVerify = passwordVerifyEdt.getText().toString();

                if(!presenter.validateRegistration(email,password,passwordVerify)){
                    errorTV.setText(presenter.validateRequestStringValue(email,password,passwordVerify));
                    errorTV.setVisibility(View.VISIBLE);
                } else {
                    errorTV.setVisibility(View.INVISIBLE);
                    registerUser(email,password);
                }
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    public void registerUser(String email, String password){
        presenter.registerUser(email,password);
    }

    @Override
    public void authenticated() {
        settingsPresenter.CreateSettings("8:00","$");
        clearInfoMessage();
        Toast.makeText(this, "Signed in!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayInvalidLogin() {
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
    }

    @Override
    public void clearInfoMessage() {
        infoToast.cancel();
    }

    @Override
    public void settingsFinished() {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}