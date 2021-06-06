package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class seeservicetab extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeservicetab_layout);
        TextView txtview = (TextView)findViewById(R.id.servicetxt);
        txtview.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}