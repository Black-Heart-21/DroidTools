package com.example.droidtools.Notepad;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class QuickNoteInputPage extends AppCompatActivity {
    private EditText noteTitle, noteDesc;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ProgressBar progressBar;
    private final String key = DataHolder.uidHolder;
    private String currentTimeString;
    private String currentDateString;
    private String date , time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_note_input_page);
        FirebaseApp.initializeApp(this);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString("Quick Notes");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        init();

        String isIntentChk =  NoteContentHolder.desc_holder;
        if (isIntentChk == "ok"){
            String data =  getIntent().getStringExtra("content");
            noteDesc.setText(data);
            NoteContentHolder.desc_holder = "";
        }

    }

    private void init() {
        noteTitle = findViewById(R.id.note_title);
        noteDesc = findViewById(R.id.note_desc);
        progressBar= findViewById(R.id.loading);
        database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_quick_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.save) {
            progressBar.setVisibility(View.VISIBLE);

            getDate();
            getTime();

            String title = noteTitle.getText().toString();
            String description = noteDesc.getText().toString();


            if (title.isEmpty() && description.isEmpty()) {
                Toast.makeText(this, "Content Empty", Toast.LENGTH_SHORT).show();
            }
            else if (title.length() >= 12){
                Toast.makeText(this, "Title Not greater than 12 words.", Toast.LENGTH_SHORT).show();
            }
            else {
                reference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        reference.child("Users").child(key).child("notes").child(key+currentDateString+currentTimeString).child("title").setValue(title);
                        reference.child("Users").child(key).child("notes").child(key+currentDateString+currentTimeString).child("description").setValue(description);
                        reference.child("Users").child(key).child("notes").child(key+currentDateString+currentTimeString).child("date").setValue(date);
                        reference.child("Users").child(key).child("notes").child(key+currentDateString+currentTimeString).child("time").setValue(time);
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }


        }
        return true;
    }
    private void getTime(){
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        currentTimeString = currentTime.format(formatter);
        time = currentTimeString;
        currentTimeString=currentTimeString.replace(":","");
        currentTimeString=currentTimeString.replace(" ","");

        System.out.println("Current time: " + currentTimeString);
    }
    private void getDate(){
        LocalDate currentDate = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        currentDateString = currentDate.format(formatter);
        date = currentDateString;
        currentDateString=currentDateString.replace("-","");

        System.out.println("Current date: " + currentDateString);
    }
}