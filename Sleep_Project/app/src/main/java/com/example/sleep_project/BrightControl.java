package com.example.sleep_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BrightControl extends AppCompatActivity {
    Button settingokbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bright_control_layout);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        SeekBar brightbar = findViewById(R.id.seekbar_control);
        settingokbtn = findViewById(R.id.brightset_okbtn);

        brightbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBrightness(progress);
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
                Toast.makeText(getApplicationContext(), "밝기값이 sharedPreference에 저장됩니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    private void setBrightness(int value){
        //시크바에 저장한 밝기 값이 sharedPreference에 저장되는 메소드로 수정할 예정
        if(value <0){
            value = 0;
        }else if(value>100){
            value=100;
        }
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = (float)value / 100;
        getWindow().setAttributes(params);
    }
}