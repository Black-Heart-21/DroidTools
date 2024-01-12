package com.example.droidtools.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.R;

import java.util.Objects;

public class LoginOrSignUpPage extends AppCompatActivity {
    private Button login;
    private Button signup;
    private void init(){
        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_signup_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        login.setOnClickListener(view -> {
            Intent intent = new Intent(LoginOrSignUpPage.this, LoginPage.class);
            startActivity(intent);
        });
        signup.setOnClickListener(view -> {
            Intent intent = new Intent(LoginOrSignUpPage.this, SignUpPage.class);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}