package com.example.droidtools.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SignUpPage extends AppCompatActivity {
    private EditText password;
    private EditText cPassword;
    private EditText mail,phonenumber,username;
    private  Button signUpBtn;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private FirebaseAuth auth;
    String pass1,usernm,pass2,mailstr,phno;



    private void  init(){
        password = findViewById(R.id.password);
        cPassword = findViewById(R.id.cPassword);
        mail = findViewById(R.id.mail);
        phonenumber = findViewById(R.id.phonenumber);
        username = findViewById(R.id.username);
        signUpBtn = findViewById(R.id.signUpBtn);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://droidtools-73721-default-rtdb.firebaseio.com/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        signUpBtn.setOnClickListener(view -> {

            pass1 = password.getText().toString();
            phno = phonenumber.getText().toString();
            mailstr = mail.getText().toString();
            pass2 = cPassword.getText().toString();
            usernm = username.getText().toString();



            if (pass1.isEmpty() && pass2.isEmpty()) {
                Toast.makeText(SignUpPage.this, "Empty password", Toast.LENGTH_SHORT).show();
            }
            else{
                if (pass1.equals(pass2)) {
                    if (pass1.length() <  6){
                        Toast.makeText(this, "Password must contain 6 or more word.", Toast.LENGTH_SHORT).show();
                    }
                    else if (!mailstr.isEmpty()){
                       authLogin();
                    }
                    
                    else {
                        Toast.makeText(this, "Please enter your Email first!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(SignUpPage.this, "Passwords are not same", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void authLogin(){
        auth.createUserWithEmailAndPassword(mailstr,pass1)
                .addOnCompleteListener(SignUpPage.this, task -> {
                    if(task.isSuccessful()){
                        FirebaseUser user=auth.getCurrentUser();
                        String uid= null;
                        if (user != null) {
                            uid = user.getUid();
                        }
                        String date = java.time.LocalDate.now().toString();

                        if (uid != null) {
                            reference.child("Users").child(uid).child("fullname").setValue(usernm);
                        }
                        if (uid != null) {
                            reference.child("Users").child(uid).child("email").setValue(mailstr);
                        }
                        if (uid != null) {
                            reference.child("Users").child(uid).child("phoneno").setValue(phno);
                        }
                        if (uid != null) {
                            reference.child("Users").child(uid).child("join").setValue(date);
                        }


                        Toast.makeText(SignUpPage.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),LoginPage.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(SignUpPage.this, "Something went wrong!!!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}