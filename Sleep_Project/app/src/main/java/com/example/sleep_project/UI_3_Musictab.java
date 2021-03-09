package com.example.sleep_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class UI_3_Musictab extends AppCompatActivity {
    Button main,music,statistics,account;
    Button musicRestart,musicPause;
    TextView musicName;

    MediaPlayer md;
    TabLayout tabLayout;
    LinearLayout bird_layout,sea_layout,wind_layout;
    Context context;
    ScrollView bird_scrollView,sea_scrollView,wind_scrollView;
    //버튼이 여러개 있으나 노래가 3개라 9개만 해놈.
    Button birdSong1,birdSong2,birdSong3,birdSong4,birdSong5,birdSong6,birdSong7,birdSong8,birdSong9;
    int currentMediaPlayPosition; // 현재 실행되고 있는 음악 위치
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_3_musictab);
        main = (Button)findViewById(R.id.main); //메인기능버튼
        music = (Button)findViewById(R.id.music); //음악기능 버튼
        statistics = (Button)findViewById(R.id.statistics); //통계기능 버튼
        account = (Button)findViewById(R.id.set); //계정관리기능 버튼
        musicRestart = (Button)findViewById(R.id.musicStart);
        musicPause = (Button) findViewById(R.id.musicStop);

        //노래 버튼에 관련된 버튼
        birdSong1 = (Button)findViewById(R.id.birdSong1);
        birdSong2 = (Button)findViewById(R.id.birdSong2);
        birdSong3 = (Button)findViewById(R.id.birdSong3);
        birdSong4 = (Button)findViewById(R.id.birdSong4);
        birdSong5 = (Button)findViewById(R.id.birdSong5);
        birdSong6 = (Button)findViewById(R.id.birdSong6);
        birdSong7 = (Button)findViewById(R.id.birdSong7);
        birdSong8 = (Button)findViewById(R.id.birdSong8);
        birdSong9 = (Button)findViewById(R.id.birdSong9);
        //노래들을 담아두는 배열(노래가 3개라 3개를 3번 중복해서 넣음)
        int[] birdSongArr = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song1, R.raw.song2, R.raw.song3};
        birdSong1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                md = MediaPlayer.create(UI_3_Musictab.this,birdSongArr[0]);
                md.start();


            }
        });

        //노래 일시정지
        musicPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(md != null) {
                        currentMediaPlayPosition = md.getCurrentPosition();
                        md.pause();
                    }

            }
        });
        //일시정지한 노래 정지한 부분부터 재시작
        musicRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(md!=null && !md.isPlaying()) {
                    md.start();
                    md.seekTo(currentMediaPlayPosition);
                }
            }
        });
        //새 , 바람 , 바다 탭
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        //새,바다,바람에 대한 노래들 출력되는곳
        bird_layout = findViewById(R.id.bird_layout);
        sea_layout = findViewById(R.id.sea_layout);
        wind_layout = findViewById(R.id.wind_layout);
        wind_scrollView = findViewById(R.id.wind_scrollView);
        bird_scrollView = findViewById(R.id.bird_scrollView);
        sea_scrollView = findViewById(R.id.sea_scrollView);
        context = this;
        //버튼모양
        musicPause.setBackgroundResource(R.drawable.button_shape);
        musicRestart.setBackgroundResource(R.drawable.button_shape);
        music.setBackgroundColor(Color.BLUE);

       

                //메인 탭으로 이동
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(intent);
                finish();
            }
        });

        //통계 탭으로 이동
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
            }
        });

        //설정 탭으로 이동
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_5_AccountTab.class);
                startActivity(intent);
                finish();
            }
        });


    }
        //밑에 changView 메소드를 이용한거 int 형을 집어넣어서 어떤 탭을 선택했는지 changView 매개변수에 집어넣음
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };


        //위에있는 이벤트에서 매개변수로 넘어온 위치값으 받아 그 해당하는 위치값 화면을 출력
    private void changeView(int index) {
        switch (index) {
            case 0 :
                sea_layout.setVisibility(View.INVISIBLE);
                wind_layout.setVisibility(View.INVISIBLE);
                bird_layout.setVisibility(View.VISIBLE);
                bird_scrollView.setVisibility(View.VISIBLE);
                wind_scrollView.setVisibility(View.INVISIBLE);
                sea_scrollView.setVisibility(View.INVISIBLE);

                break;
            case 1:
                sea_layout.setVisibility(View.VISIBLE);
                wind_layout.setVisibility(View.INVISIBLE);
                bird_layout.setVisibility(View.INVISIBLE);
                bird_scrollView.setVisibility(View.INVISIBLE);
                wind_scrollView.setVisibility(View.INVISIBLE);
                sea_scrollView.setVisibility(View.VISIBLE);
                break;
            case 2:
                sea_layout.setVisibility(View.INVISIBLE);
                wind_layout.setVisibility(View.VISIBLE);
                bird_layout.setVisibility(View.INVISIBLE);
                bird_scrollView.setVisibility(View.INVISIBLE);
                wind_scrollView.setVisibility(View.VISIBLE);
                sea_scrollView.setVisibility(View.INVISIBLE);
                break;
        }
    }
}

