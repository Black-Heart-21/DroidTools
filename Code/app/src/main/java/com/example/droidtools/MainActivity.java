package com.example.droidtools;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.LoginAndSignUp.LoginOrSignUpPage;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            finally {
                Intent intent = new Intent(MainActivity.this, LoginOrSignUpPage.class);
                startActivity(intent);
                finish();
            }
        });
        thread.start();
    }
}