package com.example.droidtools.PdfToTextTools;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.Notepad.NoteContentHolder;
import com.example.droidtools.Notepad.QuickNoteInputPage;
import com.example.droidtools.R;
import com.example.droidtools.TextToPDFTools.TextToPdfFromImage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PdfToText extends AppCompatActivity {


    private final int CHOOSE_PDF_FROM_DEVICE = 1001;
    private static final String TAG = "PdfToText";
    InputStream inputStream;

    private String cont;

    private ImageView buttonsave;
    private TextView showtxt;
    private String filecontent = "";
//    private PdfReader reader = null;
    private void init() {
        buttonsave = findViewById(R.id.pdfbtn);
        showtxt = findViewById(R.id.showtxt);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_to_text);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString("PDF To Text");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        init();
        buttonsave.setOnClickListener(view -> chooseFileFromDevice());


    }

    private void chooseFileFromDevice(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent,CHOOSE_PDF_FROM_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_PDF_FROM_DEVICE && resultCode == RESULT_OK){
            if (data != null){
                extractTxtToPdf(data.getData());

            }
        }
    }


    private  void extractTxtToPdf(Uri uri){


        try {
            inputStream = PdfToText.this.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        new Thread(() -> {
            PdfReader reader = null;
            StringBuilder builder = new StringBuilder();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                reader = new PdfReader(inputStream);
                int pages = reader.getNumberOfPages();
                for (int i = 1; i <= pages; i++){
                   filecontent = PdfTextExtractor.getTextFromPage(reader,i);
                }
                builder.append(filecontent);
            }
            if (reader != null) {
                reader.close();
            }

            runOnUiThread(() -> {
                cont = builder.toString();
                showtxt.setText(builder.toString());
            });

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        }).start();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_img_to_txt,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.savepdf){
            Intent intent = new Intent(PdfToText.this, TextToPdfFromImage.class);
            intent.putExtra("content",cont );
            startActivity(intent);
        } else if (item_id == R.id.savetxt) {
            Intent intent = new Intent(getApplicationContext(), QuickNoteInputPage.class);
            intent.putExtra("content",cont );
            NoteContentHolder.desc_holder = "ok";
            startActivity(intent);
        }

        return true;
    }


}