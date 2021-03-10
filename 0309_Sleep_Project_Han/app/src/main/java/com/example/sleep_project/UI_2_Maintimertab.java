package com.example.sleep_project;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class UI_2_Maintimertab extends AppCompatActivity {
    Button main, music, statistics, accountbtn, setBtn, lockbtn, ringbtn, setbtn2;
    private FirebaseAuth mAuth ;
    AlarmManager alarm_manager;
    TimePicker sleep_timePicker,alarm_timePicker;
    Context context;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        setContentView(R.layout.ui_2_maintimertab);
        mAuth = FirebaseAuth.getInstance(); //연결된 계정 불러오기

        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        accountbtn = (Button) findViewById(R.id.accountTab); //계정관리기능 버튼
        setBtn = (Button) findViewById(R.id.setBtn); //설정완료 버튼
        setbtn2=(Button)findViewById(R.id.setbtn2); // 설정 버튼
        main.setBackgroundColor(Color.GREEN);

        this.context = this;

        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE); // 알람매니저 설정

        sleep_timePicker = findViewById(R.id.sleepTime); // 잠들시간 타임피커 설정
        alarm_timePicker = findViewById(R.id.breakTime); // 일어날시간 타임피커 설정
        final Calendar calendar = Calendar.getInstance(); // Calendar 객체 생성

        final Intent alarm_intent = new Intent(this.context, UI_2_2_AlarmReceiver.class); // 알람리시버 intent 생성

        //설정완료 버튼을 눌렀을때 동작
        setBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                // calendar에 시간 세팅
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timePicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timePicker.getMinute());

                // 시간을 가져오는 명령
                int hour = alarm_timePicker.getHour();
                int minute = alarm_timePicker.getMinute();
                Toast.makeText(UI_2_Maintimertab.this, hour + "시 " + minute + "분" + "에 알람이 울립니다 ",Toast.LENGTH_SHORT).show();

                alarm_intent.putExtra("state","alarm on");  // receiver에 string 값 넘겨주기

                pendingIntent = PendingIntent.getBroadcast(UI_2_Maintimertab.this, 0, alarm_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent); // 알람셋팅
            }
        });

        //////////////////////////////////////////////////////////화면이동 관련///////////////////////////////////////////////////////
        //임시 버튼 : 누르면 잠금화면으로 연결되게끔 함
        lockbtn = (Button) findViewById(R.id.lock);
        lockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_2_1_Lock_activity.class);
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
            }
        });
    }
}