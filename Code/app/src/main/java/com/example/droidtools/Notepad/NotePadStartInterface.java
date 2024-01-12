package com.example.droidtools.Notepad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class NotePadStartInterface extends AppCompatActivity {
    private FloatingActionButton addNotes;
    private NoteView adapter;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private ActivityResultLauncher<Intent> launcher;
    private static final int REQUEST_CODE = 1;
    private ChildEventListener listener;
    private ArrayList<Note> noteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pad_start_interface);
        Objects.requireNonNull(getSupportActionBar()).hide();

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Note note=(Note)data.getSerializableExtra("Result");
                            reference.push().setValue(note);
                        }
                    }
                }
        );




        init();

        addNotes.setOnClickListener(view -> {
            Intent intent = new Intent(NotePadStartInterface.this, QuickNoteInputPage.class);
            launcher.launch(intent);

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
      //  loadRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // loadRecyclerView();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void init() {
        addNotes = findViewById(R.id.add_btn);
        recyclerView = findViewById(R.id.recycleview);
        adapter = new NoteView(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        noteList=new ArrayList<>();
        String key = DataHolder.uidHolder;
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://droidtools-73721-default-rtdb.firebaseio.com/").child("Users").child(key).child("notes");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                noteList.clear();
                for (DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    Note note = noteSnapshot.getValue(Note.class);
                    noteList.add(note);
                }

                adapter.setData(noteList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}