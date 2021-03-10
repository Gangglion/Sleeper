package com.example.sleep_project;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UI_2_2_AlarmImg_Activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_2_2_alarmimg_layout);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //TODO : AlarmReceiver에서 서비스 시작하는 부분을 인텐트를 주어 위 엑티비티가 나오게끔 한 뒤
        // 이 엑티비티에서 state인텐트 다시 설정 후 서비스 시작하게끔
    }
}
