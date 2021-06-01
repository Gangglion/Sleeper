package com.example.sleep_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BrightControl extends AppCompatActivity {
    Button settingokbtn;
    int brightValue;
    prefvalue prefOb = new prefvalue();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bright_control_layout);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SeekBar brightbar = findViewById(R.id.seekbar_control);
        brightbar.setProgress(prefOb.getbrightvalue());

        settingokbtn = findViewById(R.id.brightset_okbtn);

        brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brightValue = progress;
                //Log.d("progress",String.valueOf(brightValue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        settingokbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "밝기값이 저장됩니다.", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = prefOb.pref.edit();
                editor.putInt("bright",brightValue);
                editor.commit();
                finish();
            }
        });
    }
}