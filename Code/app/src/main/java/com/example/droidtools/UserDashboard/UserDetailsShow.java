package com.example.droidtools.UserDashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.AppMenu.HomeDashboard;
import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.LoginAndSignUp.LogOutApp;
import com.example.droidtools.LoginAndSignUp.LoginPage;
import com.example.droidtools.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsShow extends AppCompatActivity {

    private String uiduser;
    TextView user_name,user_mail,phnouser,userid,join_date;
    String key = DataHolder.uidHolder;
    CircleImageView profile_image;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference =  FirebaseDatabase.getInstance().getReferenceFromUrl("https://droidtools-73721-default-rtdb.firebaseio.com/");

    private void init(){
        user_name = findViewById(R.id.user_name);
        user_mail = findViewById(R.id.user_mail);
        phnouser = findViewById(R.id.phnouser);
        userid = findViewById(R.id.userid);
        join_date = findViewById(R.id.join_date);
        profile_image = findViewById(R.id.profile_image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_show);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



              database.getReference().child("Users").child(key).child("Profile Pic")
                      .child("Users").child(key).child("imageurl")
                      .addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(@NonNull DataSnapshot snapshot) {
                              String img = snapshot.getValue(String.class);
                              Picasso.get().load(img).into(profile_image);
                          }
                          @Override
                          public void onCancelled(@NonNull DatabaseError error) {

                          }
                      });

                String userName = snapshot.child(key).child("fullname").getValue(String.class);
                user_name.setText(userName);
                String usermail = snapshot.child(key).child("email").getValue(String.class);
                user_mail.setText(usermail);
                String userph = snapshot.child(key).child("phoneno").getValue(String.class);
                phnouser.setText(userph);
                String jdate = snapshot.child(key).child("join").getValue(String.class);
                join_date.setText(jdate);
                userid.setText(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        BottomNavigationView navigationView;
        navigationView = findViewById(R.id.navbarmain);
        navigationView.setSelectedItemId(R.id.profile);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Intent intent0 = new Intent(UserDetailsShow.this, HomeDashboard.class);
                    startActivity(intent0);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile:
                    Intent intent1 = new Intent(UserDetailsShow.this,UserDetailsShow.class);
                    startActivity(intent1);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mainmenu:
                    Intent intent2 = new Intent(UserDetailsShow.this, AppMenuPage.class);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.settings:
                    Intent intent3 = new Intent(UserDetailsShow.this, UserDashBoardSettings.class);
                    startActivity(intent3);
                    overridePendingTransition(0,0);
                    break;
                case R.id.logout:
                    LogOutApp logout = new LogOutApp();
                    new AlertDialog.Builder(this).setMessage("Are you sure to logout?")
                            .setCancelable(false)
                            .setPositiveButton("Logout", (dialogInterface, i) -> {
                                logout.logMeOut();
                                Intent intent31 = new Intent(getApplicationContext(), LoginPage.class);
                                startActivity(intent31);
                                finish();
                                overridePendingTransition(0,0);
                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> {
                                Intent intent21 = new Intent(getApplicationContext(), UserDetailsShow.class);
                                startActivity(intent21);
                                overridePendingTransition(0,0);
                            })
                            .show();

                    overridePendingTransition(0,0);
                    return true;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return false;
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppMenuPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}