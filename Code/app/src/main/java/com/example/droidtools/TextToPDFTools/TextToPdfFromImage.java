package com.example.droidtools.TextToPDFTools;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.droidtools.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TextToPdfFromImage extends AppCompatActivity {
    EditText titlePDF,descPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_pdf_from_image);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        SpannableString s = new SpannableString("Text To PDF");
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);

        titlePDF = findViewById(R.id.note_title);
        descPDF = findViewById(R.id.note_desc);

        Intent intent = getIntent();
        String content =  intent.getStringExtra("content");
        descPDF.setText(content);
    }

    private void createpdf(){
            String title = titlePDF.getText().toString();
            String desc = descPDF.getText().toString();

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(path,title+"nil21TextToPDF.pdf");

            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            Document document = new Document(PageSize.A4);
            try {
                PdfWriter.getInstance(document,new FileOutputStream(file.getAbsoluteFile()));
            } catch (DocumentException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }


        document.open();
            Font fonttit = new Font(Font.FontFamily.HELVETICA,36,Font.BOLD, BaseColor.BLUE);
            Font fontpara = new Font(Font.FontFamily.HELVETICA,20);
            Paragraph paragraphtit = new Paragraph();
            Paragraph paragraphdsc = new Paragraph();
            paragraphtit.setAlignment(Element.ALIGN_CENTER);
            paragraphtit.add(new Paragraph(title,fonttit));
            paragraphdsc.add(new Paragraph(desc,fontpara));

            try {
                document.add(paragraphtit);
                document.add(paragraphdsc);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
            document.close();

            Toast.makeText(this, "Pdf Created Successfully", Toast.LENGTH_SHORT).show();
        }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_for_quick_notes,menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.save){
           createpdf();
        }
        return true;
    }

}