package com.example.droidtools.AppMenu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Objects;

public class HomeDashboard extends AppCompatActivity {

    LinearLayout imgtotxt,savedatabase,txttopdf,quicknotes,opendatabase,pdftotxt;
    private void init(){
        imgtotxt = (LinearLayout) findViewById(R.id.imgtotxt);
        savedatabase = (LinearLayout) findViewById(R.id.savedatabase);
        txttopdf = (LinearLayout) findViewById(R.id.txttopdf);
        quicknotes = (LinearLayout) findViewById(R.id.quicknotes);
        opendatabase = (LinearLayout) findViewById(R.id.opendatabase);
        pdftotxt = (LinearLayout) findViewById(R.id.pdftotxt);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);
        Objects.requireNonNull(getSupportActionBar()).hide();
        init();


        imgtotxt.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, ImageToText.class);
            startActivity(intent);
        });

        savedatabase.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, UploadFiles.class);
            startActivity(intent);
        });

        txttopdf.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, TextToPdf.class);
            startActivity(intent);
        });

        quicknotes.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, NotePadStartInterface.class);
            startActivity(intent);
        });

        opendatabase.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, FetchFileFromDB.class);
            startActivity(intent);
        });
        pdftotxt.setOnClickListener(view -> {
            Intent intent = new Intent(HomeDashboard.this, PdfToText.class);
            startActivity(intent);
        });





        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.navbarmain);
        navigationView.setSelectedItemId(R.id.home);
        //noinspection deprecation
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    Intent intent0 = new Intent(getApplicationContext(), HomeDashboard.class);
                    startActivity(intent0);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.profile:
                    Intent intent1 = new Intent(getApplicationContext(), UserDetailsShow.class);
                    startActivity(intent1);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.mainmenu:
                    Intent intent2 = new Intent(getApplicationContext(), AppMenuPage.class);
                    startActivity(intent2);
                    overridePendingTransition(0,0);
                    return true;
                case R.id.settings:
                    Intent intent3 = new Intent(getApplicationContext(), UserDashBoardSettings.class);
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
                                Intent intent21 = new Intent(getApplicationContext(), AppMenuPage.class);
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