package com.example.sleep_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class UI_4_Statisticstab extends AppCompatActivity {
    Button main, music, statistics, account;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics1);

        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        account = (Button) findViewById(R.id.accounttab); //계정관리기능 버튼


        statistics.setBackgroundColor(Color.GRAY);
        //메인 탭으로 이동
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(intent);
                finish();
            }
        });
        //음악 탭으로 이동
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                finish();
            }
        });
        //계정관리 탭으로 이동
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_5_AccountTab.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.appUseTime:
                intent = new Intent(UI_4_Statisticstab.this,AppTimeStatistics.class);
                startActivity(intent);
                finish();
                break;

            case R.id.sleepTimecomparison:
                intent = new Intent(UI_4_Statisticstab.this,SleepComparisonStatistics.class);
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }

}
