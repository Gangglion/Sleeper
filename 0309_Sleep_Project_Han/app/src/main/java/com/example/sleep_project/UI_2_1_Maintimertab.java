package com.example.sleep_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UI_2_1_Maintimertab extends AppCompatActivity {
    Button main, music, statistics, accountbtn, setBtn, lockbtn, ringbtn, setbtn2;
    private FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_2_1_maintimertab);
        mAuth = FirebaseAuth.getInstance(); //연결된 계정 불러오기

        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        accountbtn = (Button) findViewById(R.id.accountTab); //계정관리기능 버튼
        setBtn = (Button) findViewById(R.id.setBtn); //설정완료 버튼
        setbtn2=(Button)findViewById(R.id.setbtn2); // 설정 버튼

        main.setBackgroundColor(Color.GREEN);

        //임시 버튼 : 누르면 잠금화면으로 연결되게끔 함
        lockbtn = (Button) findViewById(R.id.lock);
        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_2_2_Lock_activity.class);
                startActivity(intent);
            }
        });
        //임시 버튼 : 누르면 알람화면으로 연결되게끔 함
        ringbtn = (Button) findViewById(R.id.Ring);
        ringbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_2_2_Maintimertabqna.class);
                startActivity(intent);
            }
        });
        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "설정완료", Toast.LENGTH_LONG).show();
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
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
            }
        });
        accountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_5_AccountTab.class);
                startActivity(intent);
                finish();
            }
        });
        setbtn2.setOnClickListener(new View.OnClickListener() { // UI변경 후 좌측 상단 설정버튼 - 태현
            @Override  // 6번 UI 액티비티 불러오기
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_6_Settings_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}