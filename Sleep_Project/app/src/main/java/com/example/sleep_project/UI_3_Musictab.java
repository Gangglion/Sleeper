package com.example.sleep_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class UI_3_Musictab extends AppCompatActivity {
    Button main,music,statistics,account;
    Button musicRestart,musicPause;
    TextView musicName;
    int temp = 0;
    int prePosition = 0;
    TabLayout tabLayout;
    LinearLayout bird_layout,sea_layout,wind_layout;
    Context context;
    ListView birdList,seaList,windList;

    boolean bird_isPlaying,sea_isPlaying,wind_isPlaying;


    //버튼이 여러개 있으나 노래가 3개라 9개만 해놈.
    int currentMediaPlayPosition; // 현재 실행되고 있는 음악 위치
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_3_musictab);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        /////////////////////////////////////////////
        setTitle("음악");
        main = (Button)findViewById(R.id.main); //메인기능버튼
        music = (Button)findViewById(R.id.music); //음악기능 버튼
        statistics = (Button)findViewById(R.id.statistics); //통계기능 버튼
        account = (Button)findViewById(R.id.accounttab); //계정관리기능 버튼
        musicRestart = (Button)findViewById(R.id.musicStart);
        musicPause = (Button) findViewById(R.id.musicStop);
        musicName = (TextView)findViewById(R.id.musicName);

        //노래에 대한 리스트 목록
        birdList = (ListView)findViewById(R.id.birdList);
        seaList = (ListView)findViewById(R.id.seaList);
        windList = (ListView)findViewById(R.id.windList);



        //새 노래와 관련된 부분
        int[] birdSong = {R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song1, R.raw.song2, R.raw.song3,
                R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song1, R.raw.song2, R.raw.song3,
                R.raw.song1, R.raw.song2, R.raw.song3, R.raw.song1, R.raw.song2, R.raw.song3};
        String[] birdTitle = {"새노래1","새노래2","새노래3","새노래4","새노래5","새노래6",
                "새노래7","새노래8","새노래9","새노래10","새노래11"
                ,"새노래12","새노래13","새노래14","새노래15","새노래16","새노래17","새노래18"};
        ArrayAdapter<String> birdAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,birdTitle);
        birdList.setAdapter(birdAdapter);
        birdAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_musictab_color,birdTitle);
        birdList.setAdapter(birdAdapter);
        MediaPlayer[] birdMd = new MediaPlayer[birdSong.length];
        //바다 노래
        int[] seaSong = {R.raw.song3, R.raw.song2, R.raw.song1, R.raw.song3, R.raw.song2, R.raw.song1,
                R.raw.song3, R.raw.song2, R.raw.song1, R.raw.song3, R.raw.song2, R.raw.song1,
                R.raw.song3, R.raw.song2, R.raw.song1, R.raw.song3, R.raw.song2, R.raw.song1};
        String[] seaTitle = {"바다노래1","바다노래2","바다노래3","바다노래4","바다노래5","바다노래6",
                "바다노래7","바다노래8","바다노래9","바다노래10","바다노래11"
                ,"바다노래12","바다노래13","바다노래14","바다노래15","바다노래16","바다노래17","바다노래18"};
        ArrayAdapter<String> seaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,seaTitle);
        seaList.setAdapter(seaAdapter);
        seaAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_musictab_color,seaTitle);
        seaList.setAdapter(seaAdapter);

        MediaPlayer[] seaMd = new MediaPlayer[seaSong.length];
        //새 노래 클릭했을때
        birdList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(sea_isPlaying) {
                    sea_isPlaying = false;
                    for(int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].stop();
                        seaMd[i].release();
                    }
                }
                for(int i = 0; i < birdSong.length; i++) {
                    birdMd[i] = MediaPlayer.create(UI_3_Musictab.this, birdSong[i]);
                }


                for(int i = position; i < birdSong.length-1; i++) {
                    birdMd[i].setNextMediaPlayer(birdMd[i+1]);
                    musicName.setText(birdTitle[i]);
                }

                bird_isPlaying = true;

            }
        });
        //바다 노래 클릭했을때

        seaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {






                if(bird_isPlaying) {
                    bird_isPlaying = false;
                    for(int i =0; i < birdMd.length; i++) {
                        currentMediaPlayPosition = birdMd[i].getCurrentPosition();
                        birdMd[i].stop();
                        birdMd[i].release();
                    }
                }

                for(int i = 0; i < seaSong.length; i++) {
                    seaMd[i] = MediaPlayer.create(UI_3_Musictab.this, seaSong[i]);

                }
                seaMd[position].start();
                for(int i = position; i < seaSong.length-1; i++) {
                    seaMd[i].setNextMediaPlayer(seaMd[i+1]);

                }

                musicName.setText(seaTitle[position]);


                sea_isPlaying = true;
            }
        });


        //바람 노래와 관련된 부분

        //노래 일시정지
        musicPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bird_isPlaying) {
                    for (int i = 0; i < birdMd.length; i++) {


                        currentMediaPlayPosition = birdMd[i].getCurrentPosition();
                        birdMd[i].pause();
                    }

                }else if(sea_isPlaying) {
                    for(int i =0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].pause();
                    }
                }


            }
        });
        //일시정지한 노래 정지한 부분부터 재시작
        musicRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bird_isPlaying) {
                    for (int i = 0; i < birdMd.length; i++) {
                        if (birdMd[i] != null && !birdMd[i].isPlaying()) {

                            birdMd[i].start();
                            birdMd[i].seekTo(currentMediaPlayPosition);
                        }
                    }
                } else if (sea_isPlaying) {
                    for (int i = 0; i < seaMd.length; i++) {
                        if (seaMd[i] != null && !seaMd[i].isPlaying()) {

                            seaMd[i].start();
                            seaMd[i].seekTo(currentMediaPlayPosition);
                        }
                    }
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

        context = this;
        //버튼모양
        musicPause.setBackgroundResource(R.drawable.button_shape);
        musicRestart.setBackgroundResource(R.drawable.button_shape);

        music.setBackgroundColor(Color.GRAY);
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
                birdList.setVisibility(View.VISIBLE);
                seaList.setVisibility(View.INVISIBLE);
                windList.setVisibility(View.INVISIBLE);
                break;
            case 1:
                sea_layout.setVisibility(View.VISIBLE);
                wind_layout.setVisibility(View.INVISIBLE);
                bird_layout.setVisibility(View.INVISIBLE);
                birdList.setVisibility(View.INVISIBLE);
                seaList.setVisibility(View.VISIBLE);
                windList.setVisibility(View.INVISIBLE);

                break;
            case 2:
                sea_layout.setVisibility(View.INVISIBLE);
                wind_layout.setVisibility(View.VISIBLE);
                bird_layout.setVisibility(View.INVISIBLE);
                birdList.setVisibility(View.INVISIBLE);
                seaList.setVisibility(View.INVISIBLE);
                windList.setVisibility(View.VISIBLE);
                break;
        }
    }
}

