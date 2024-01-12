package com.example.droidtools.LoginAndSignUp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.ForgotPasswordTools.ForgotPassword;
import com.example.droidtools.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    private EditText email;
    private  TextView signupText;
    private EditText password;
    private Button loginButton;
    private TextView forgot;
    private FirebaseAuth auth;
    private DatabaseReference reference;
    private String uid;
    private void init(){
        email = findViewById(R.id.mailid);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.button);
        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://droidtools-73721-default-rtdb.firebaseio.com/");
        forgot = findViewById(R.id.forgotpass);
        signupText = findViewById(R.id.signupText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        forgot.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, ForgotPassword.class);
            startActivity(intent);
        });

        signupText.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, SignUpPage.class);
            startActivity(intent);
        });



        loginButton.setOnClickListener(view -> {
            String mailstr = email.getText().toString();
            String pass  = password.getText().toString();
            if (mailstr.isEmpty()){
                Toast.makeText(LoginPage.this, "Enter email.", Toast.LENGTH_SHORT).show();
            } else if (pass.isEmpty()) {
                Toast.makeText(LoginPage.this, "Enter password.", Toast.LENGTH_SHORT).show();
            }else{

                auth.signInWithEmailAndPassword(mailstr,pass)
                        .addOnCompleteListener(LoginPage.this, task -> {
                            if (task.isSuccessful()){
                                loginUser();
                            }
                            else {
                                Toast.makeText(LoginPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void loginUser() {
        FirebaseUser user=auth.getCurrentUser();
        if (user != null) {
            uid=user.getUid();
        }
        Toast.makeText(LoginPage.this, "Login Successful!", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getApplicationContext(), AppMenuPage.class);
        DataHolder holder = new DataHolder(uid);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(), LoginOrSignUpPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}