package com.example.droidtools.Notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.droidtools.R;

public class UpdateContent extends AppCompatActivity {

    private EditText update_desc;

    public void init(){
        update_desc = findViewById(R.id.update_desc);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_content);

        String data =  getIntent().getStringExtra("content");
        update_desc.setText(data);


    }
}