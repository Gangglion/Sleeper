package com.example.sleep_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UI_2_Maintimertab extends AppCompatActivity {
    Button main,music,statistics,account,setBtn,lockbtn,ringbtn, setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_2_maintimertab);
        main = (Button)findViewById(R.id.main); //메인기능버튼
        music = (Button)findViewById(R.id.music); //음악기능 버튼
        statistics = (Button)findViewById(R.id.statistics); //통계기능 버튼
        account = (Button)findViewById(R.id.set); //계정관리기능 버튼
        setBtn = (Button)findViewById(R.id.setBtn); // 설정완료 버튼
        setting = (Button)findViewById(R.id.setBtn2); // UI개선 메인화면 상단 설정버튼
        main.setBackgroundColor(Color.GREEN);

        //임시 버튼 : 누르면 잠금화면으로 연결되게끔 함
        lockbtn = (Button)findViewById(R.id.lock);
        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), lock.class);
                startActivity(intent);
            }
        });
        //임시 버튼 : 누르면 알람화면으로 연결되게끔 함
        ringbtn = (Button)findViewById(R.id.Ring);
        ringbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertabqna.class);
                startActivity(intent);
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"설정완료",Toast.LENGTH_LONG).show();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() { // UI변경 후 좌측 상단 설정버튼 - 태현
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UI_5_Settings_activity.class);
                startActivity(intent);
                finish();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                finish();
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_5_AccountTab.class);
                startActivity(intent);
                finish();
            }
        });
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
            }
        });
    }
}