package com.example.Sleeper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class seeservicetab extends AppCompatActivity {
    //서비스이용약관
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seeservicetab_layout);
        TextView txtview = (TextView)findViewById(R.id.servicetxt);
        txtview.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}