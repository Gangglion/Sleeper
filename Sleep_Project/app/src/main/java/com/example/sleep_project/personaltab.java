package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class personaltab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personaltab);
        TextView txtview = (TextView)findViewById(R.id.personaltxt);
        txtview.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}