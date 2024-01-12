package com.example.droidtools.ForgotPasswordTools;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.LoginAndSignUp.LoginPage;
import com.example.droidtools.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    Button forgotBtn;
    EditText gmail;
    FirebaseAuth auth;
    String getMailTxt;


    private void init(){
        forgotBtn = findViewById(R.id.forgotbtn);
        gmail = findViewById(R.id.gmail);
        auth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();

        forgotBtn.setOnClickListener(view -> {
            getMailTxt = gmail.getText().toString();

            if (!TextUtils.isEmpty(getMailTxt)){
                resetPass();
            }
            else {
                Toast.makeText(ForgotPassword.this, "Mail Not Found", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private  void resetPass(){
        auth.sendPasswordResetEmail(getMailTxt).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(ForgotPassword.this, "Open your mail...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(ForgotPassword.this, "Failed.....", Toast.LENGTH_SHORT).show();
            }
        });
    }


}