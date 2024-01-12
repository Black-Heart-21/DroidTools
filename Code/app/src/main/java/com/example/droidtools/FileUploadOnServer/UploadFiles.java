package com.example.droidtools.FileUploadOnServer;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.AppMenu.AppMenuPage;
import com.example.droidtools.DatasOfUsersLogedIn.DataHolder;
import com.example.droidtools.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.Objects;

@SuppressWarnings("deprecation")
public class UploadFiles extends AppCompatActivity {

    Button upload;
    EditText filename;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    ProgressDialog dialog;
    String key = DataHolder.uidHolder;


    private void init(){
        upload = findViewById(R.id.upload);
        filename = findViewById(R.id.filename);
        storageReference = FirebaseStorage.getInstance().getReference();
        String key = DataHolder.uidHolder;
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(key).child("Upload Files");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_files);
        init();
//        upload.setEnabled(false);
        upload.setOnClickListener(view -> selectFile());
    }

    public void selectFile(){
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"selectpdffile"),21);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 21 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            Uri uri = data.getData();
            String uriStr = uri.toString();
            File file = new File(uriStr);
            String displayName = null;
            if (uriStr.startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri,null,null,null,null);
                    if (cursor != null && cursor.moveToFirst()){
                        displayName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                    }
                }finally {
                    Objects.requireNonNull(cursor).close();
                }
            }else if (uriStr.startsWith("files://")){
                displayName = file.getName();
            }
            upload.setEnabled(true);
            filename.setText(displayName);
            upload.setOnClickListener(view -> uploadToFirebase(uri));


        }

    }
    private void uploadToFirebase(Uri data){
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading....");
        dialog.show();

        StorageReference reference = storageReference.child(key).child(filename.getText().toString()+System.currentTimeMillis()+".pdf");

        //noinspection SuspiciousIndentAfterControlStatement
        reference.putFile(data).addOnSuccessListener(taskSnapshot -> {

            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isComplete());

                Uri url = uriTask.getResult();

                String nameoffile = filename.getText().toString();

                FileUploader uploader = new FileUploader(nameoffile,url.toString());
                databaseReference.child(Objects.requireNonNull(databaseReference.push().getKey())).setValue(uploader);

                Toast.makeText(UploadFiles.this, "File Uploaded", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

        }).addOnProgressListener(snapshot -> {
            double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
            try {
                dialog.setMessage("Uploaded : "+ (int) progress +"%");
                if (progress == 100.0) {
                    showCompletionMessage();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error in onProgress: " + e.getMessage());
            }
        });
    }

    private void showCompletionMessage() {
        Intent intent = new Intent(UploadFiles.this, AppMenuPage.class);
        intent.putExtra("key",key);
        startActivity(intent);
        Toast.makeText(this, "File uploaded....", Toast.LENGTH_SHORT).show();

    }

}
