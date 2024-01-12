package com.example.droidtools.LicenseExp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.LoginAndSignUp.LogOutApp;
import com.example.droidtools.LoginAndSignUp.LoginPage;
import com.example.droidtools.R;

import java.util.Objects;

public class Expire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expire);
        Objects.requireNonNull(getSupportActionBar()).hide();

    }
    @Override
    public void onBackPressed() {
        LogOutApp logout = new LogOutApp();
        logout.logMeOut();
        Intent intent31 = new Intent(getApplicationContext(), LoginPage.class);
        startActivity(intent31);
        finish();

        overridePendingTransition(0,0);
    }
}