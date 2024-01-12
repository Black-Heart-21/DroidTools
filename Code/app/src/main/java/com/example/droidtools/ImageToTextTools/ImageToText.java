package com.example.droidtools.ImageToTextTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.Notepad.NoteContentHolder;
import com.example.droidtools.Notepad.QuickNoteInputPage;
import com.example.droidtools.R;
import com.example.droidtools.TextToPDFTools.TextToPdfFromImage;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class ImageToText extends AppCompatActivity {

    ImageView imageView;
    TextView recgText;
    Uri imageUri;
    String cont;

    TextRecognizer textRecognizer;


    private void init(){
        imageView= findViewById(R.id.imageView);
        recgText= findViewById(R.id.recgtext);
    }

    @SuppressLint("MissingInflatedId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_text_tools);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString("Image To Text");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        init();


        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        imageView.setOnClickListener(view -> ImagePicker.with(ImageToText.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080,1080)
                .start());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode== Activity.RESULT_OK){

            if (data!=null){

                imageUri = data.getData();

                Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show();

                recognizeText();
            }
        }
        else {

            Toast.makeText(this, "image not select", Toast.LENGTH_SHORT).show();
        }
    }

    private void recognizeText() {
        if (imageUri!=null){

            try {
                InputImage inputImage = InputImage.fromFilePath(ImageToText.this,imageUri);

                textRecognizer.process(inputImage)
                        .addOnSuccessListener(text -> {
                            String recognizeText = text.getText();
                            TextHolder holder = new TextHolder(recognizeText);
                            recognizeText = TextHolder.textholder;
                            recgText.setText(recognizeText);

                            cont = recognizeText;

                        }).addOnFailureListener(e -> Toast.makeText(ImageToText.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_img_to_txt,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.savepdf){
            Intent intent = new Intent(ImageToText.this, TextToPdfFromImage.class);
            intent.putExtra("content",cont );
            startActivity(intent);
        } else if (item_id == R.id.savetxt) {
            Intent intent = new Intent(ImageToText.this, QuickNoteInputPage.class);
            intent.putExtra("content",cont );
            NoteContentHolder.desc_holder = "ok";
            startActivity(intent);
        }

        return true;
    }

}