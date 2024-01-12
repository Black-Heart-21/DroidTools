package com.example.droidtools.UserDashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.AppMenu.HomeDashboard;
import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.ForgotPasswordTools.ForgotPassword;
import com.example.droidtools.LoginAndSignUp.LogOutApp;
import com.example.droidtools.LoginAndSignUp.LoginPage;
import com.example.droidtools.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashBoardSettings extends AppCompatActivity {

    private EditText phonenumber,username;
    private CircleImageView displaypic;
    private Button changedetails;
    private Uri imageUri;
    private TextView forgot;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference storeDp;

    private FirebaseStorage stroage=FirebaseStorage.getInstance();
    private StorageTask storageTask;

    private String key = DataHolder.uidHolder;
    String usernm,phno;
    private FloatingActionButton camera;


    private void  init(){

        phonenumber = findViewById(R.id.phonenumber);
        username = findViewById(R.id.username);
        displaypic = findViewById(R.id.displaypic);
        forgot = findViewById(R.id.forgotpass);
        camera = findViewById(R.id.camerBtn);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Profile Pic");
        storeDp = FirebaseStorage.getInstance().getReference().child(key);
        changedetails = findViewById(R.id.changedetails);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dash_board_settings);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init();


        camera.setOnClickListener(view -> ImagePicker.Companion.with(UserDashBoardSettings.this).crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start());





        changedetails.setOnClickListener(view -> {
            usernm = username.getText().toString();
            phno = phonenumber.getText().toString();
            if (!usernm.isEmpty() && phno.isEmpty()){
                changeData(usernm);
            }
            if (!phno.isEmpty() && usernm.isEmpty()){
                changeData(1,phno);
            }
            if (!usernm.isEmpty() && !phno.isEmpty()){
                changeData(usernm,phno);
            }
            if (usernm.isEmpty() && phno.isEmpty()){
                Toast.makeText(UserDashBoardSettings.this, "Empty field", Toast.LENGTH_SHORT).show();
            }

        });

        forgot.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
            startActivity(intent);
        });

        dpShow();


        BottomNavigationView navigationView;
        navigationView = findViewById(R.id.navbarmain);
        navigationView.setSelectedItemId(R.id.settings);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Intent intent0 = new Intent(UserDashBoardSettings.this, HomeDashboard.class);
                    startActivity(intent0);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile:
                    Intent intent1 = new Intent(UserDashBoardSettings.this,UserDetailsShow.class);
                    startActivity(intent1);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mainmenu:
                    Intent intent2 = new Intent(UserDashBoardSettings.this, AppMenuPage.class);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.settings:
                    Intent intent3 = new Intent(UserDashBoardSettings.this, UserDashBoardSettings.class);
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
                                Intent intent21 = new Intent(getApplicationContext(), UserDashBoardSettings.class);
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
        displaypic.setOnClickListener(view -> ImagePicker.with(UserDashBoardSettings.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start());


        getUserInfo();

    }




    private void getUserInfo(){
           reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount() > 0){
                    if (snapshot.hasChild("image")){
                        String image = snapshot.child(key).child("image").getValue(String.class);
                        Picasso.get()
                        .load(image)
                        .into(displaypic);

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }





    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {
                imageUri = data.getData();
                displaypic.setImageURI(imageUri);


                storeDp = stroage.getReference().
                                child(key).child("imgs").child("profileimg");

                storeDp.putFile(imageUri).addOnSuccessListener(taskSnapshot -> storeDp.getDownloadUrl().addOnSuccessListener(uri -> reference.child("Users").child(key).child("imageurl").setValue(uri.toString()).addOnSuccessListener(unused -> Toast.makeText(UserDashBoardSettings.this, "Profile picture updated!", Toast.LENGTH_SHORT).show())));
                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();
            }
        } else {

            Toast.makeText(this, "image not select", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeData (String nm) {

        key = DataHolder.uidHolder;

        HashMap user = new HashMap();
        user.put("fullname",nm);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(key).updateChildren(user).addOnCompleteListener(task -> {
            username.setText("");
            if (task.isSuccessful()){
                Toast.makeText(UserDashBoardSettings.this, "Name updated.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(UserDashBoardSettings.this, "Faild", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void changeData (int a, String ph) {

        key = DataHolder.uidHolder;

        HashMap user = new HashMap();
        user.put("phoneno",ph);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(key).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                phonenumber.setText("");
                if (task.isSuccessful()){
                    Toast.makeText(UserDashBoardSettings.this, "Phone Number updated.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(UserDashBoardSettings.this, "Faild", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    private void changeData (String nm, String ph) {

        key = DataHolder.uidHolder;



        HashMap user = new HashMap();
        user.put("fullname",nm);
        user.put("phoneno",ph);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(key).updateChildren(user).addOnCompleteListener(task -> {
            username.setText("");
            if (task.isSuccessful()){
                Toast.makeText(UserDashBoardSettings.this, "Name and Number updated.", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(UserDashBoardSettings.this, "Faild", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void dpShow(){
        reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                database.getReference().child("Users").child(key).child("Profile Pic")
                        .child("Users").child(key).child("imageurl")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String img = snapshot.getValue(String.class);
                                Picasso.get().load(img).into(displaypic);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AppMenuPage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}