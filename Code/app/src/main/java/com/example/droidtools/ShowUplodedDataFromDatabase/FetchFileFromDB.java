package com.example.droidtools.ShowUplodedDataFromDatabase;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.FileUploadOnServer.FileUploader;
import com.example.droidtools.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class FetchFileFromDB extends AppCompatActivity implements AdapterForDBS.pdfClickListner{
    RecyclerView rView;
    private DatabaseReference reference;
    private AdapterForDBS adapter;
    private ProgressBar bar;
    private ArrayList<FileUploader> pdfList;
    String key = DataHolder.uidHolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_file_from_db);
        Objects.requireNonNull(getSupportActionBar()).hide();


        rView = findViewById(R.id.rView);
        adapter = new AdapterForDBS(this);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(adapter);
        pdfList=new ArrayList<>();
        bar = findViewById(R.id.progress_bar);
        reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://droidtools-73721-default-rtdb.firebaseio.com/")
                .child("Users").child(key).child("Upload Files");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pdfList.clear();
                for (DataSnapshot pdfSnapshot : snapshot.getChildren()) {
                    FileUploader file = pdfSnapshot.getValue(FileUploader.class);
                    pdfList.add(file);
                }

                adapter.setData(pdfList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void pdfClick(String url) {
        downloadPDFFromFirebaseStorage(url,"hello.pdf");
    }
    private void downloadPDFFromFirebaseStorage(String pdfUrl, String fileName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(pdfUrl);
        File localFile = new File(getCacheDir(), fileName);

        storageRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // File downloaded successfully, open the PDF viewer
                    openPDFViewer(localFile);
                })
                .addOnFailureListener(exception -> {
                    // Handle any errors that occur during the download
                    Toast.makeText(this, "Failed to download PDF", Toast.LENGTH_SHORT).show();
                });
    }
    private void openPDFViewer(File pdfFile) {
        Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // PDF viewer app not found, handle the exception
            Toast.makeText(this, "No PDF viewer app found", Toast.LENGTH_SHORT).show();
        }
    }


}