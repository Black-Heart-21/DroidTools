package com.example.droidtools.AppMenu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.FileUploadOnServer.UploadFiles;
import com.example.droidtools.ImageToTextTools.ImageToText;
import com.example.droidtools.LoginAndSignUp.LogOutApp;
import com.example.droidtools.LoginAndSignUp.LoginPage;
import com.example.droidtools.Notepad.NotePadStartInterface;
import com.example.droidtools.PdfToTextTools.PdfToText;
import com.example.droidtools.R;
import com.example.droidtools.ShowUplodedDataFromDatabase.FetchFileFromDB;
import com.example.droidtools.TextToPDFTools.TextToPdf;
import com.example.droidtools.UserDashboard.UserDashBoardSettings;
import com.example.droidtools.UserDashboard.UserDetailsShow;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class AppMenuPage extends AppCompatActivity {
    private LinearLayout quickNoteBtn,txtToPdf,imgtotxt,uploadToServerFile,pdfToTxt,openDbBtn;
    public boolean isOneMonthDifference;
    private void init(){
        quickNoteBtn = (LinearLayout) findViewById(R.id.quiNoteBtn);
        imgtotxt = (LinearLayout) findViewById(R.id.imtotxt);
        uploadToServerFile = (LinearLayout) findViewById(R.id.uploadToServerFile);
        txtToPdf = (LinearLayout) findViewById(R.id.txtToPdf);
        pdfToTxt = (LinearLayout) findViewById(R.id.pdfToTxt);
        openDbBtn = (LinearLayout) findViewById(R.id.openDbBtn);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_menu_page);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();

        String key = DataHolder.uidHolder;
        if (key == null || key.equals("null")){
            Intent intent = new Intent(AppMenuPage.this, LoginPage.class);
            startActivity(intent);
        }



        quickNoteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, NotePadStartInterface.class);
            startActivity(intent);
        });


        imgtotxt.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, ImageToText.class);
            startActivity(intent);
        });


        uploadToServerFile.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, UploadFiles.class);
            startActivity(intent);
        });

        txtToPdf.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, TextToPdf.class);
            startActivity(intent);
        });

        pdfToTxt.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, PdfToText.class);
            startActivity(intent);
        });

        openDbBtn.setOnClickListener(view -> {
            Intent intent = new Intent(AppMenuPage.this, FetchFileFromDB.class);
            startActivity(intent);
        });




        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navbarmain);
        navigationView.setSelectedItemId(R.id.mainmenu);
        //noinspection deprecation
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Intent intent0 = new Intent(AppMenuPage.this, HomeDashboard.class);
                    startActivity(intent0);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile:
                    Intent intent1 = new Intent(AppMenuPage.this, UserDetailsShow.class);
                    startActivity(intent1);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mainmenu:
                    Intent intent2 = new Intent(AppMenuPage.this, AppMenuPage.class);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.settings:
                    Intent intent3 = new Intent(AppMenuPage.this, UserDashBoardSettings.class);
                    startActivity(intent3);
                    overridePendingTransition(0,0);
                    break;
                case R.id.logout:
                    LogOutApp logout = new LogOutApp();
                    new AlertDialog.Builder(this).setMessage("Are you sure to logout?")
                                    .setCancelable(false)
                                            .setPositiveButton("Logout", (dialogInterface, i) -> {
                                                logout.logMeOut();
                                                Intent intent31 = new Intent(AppMenuPage.this, LoginPage.class);
                                                startActivity(intent31);
                                                finish();
                                                overridePendingTransition(0,0);
                                            })
                            .setNegativeButton("Cancel", (dialogInterface, i) -> {
                                Intent intent21 = new Intent(AppMenuPage.this, AppMenuPage.class);
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
                    Intent intent21 = new Intent(getApplicationContext(), AppMenuPage.class);
                    startActivity(intent21);
                    overridePendingTransition(0,0);
                })
                .show();

        overridePendingTransition(0,0);
    }
}