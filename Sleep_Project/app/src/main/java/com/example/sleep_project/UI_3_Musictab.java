package com.example.sleep_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
    Button main,music,statistics,settingtab;
    Button musicRestart,musicPause;
    TextView musicName;
    int temp = 0;
    int prePosition = 0;
    TabLayout tabLayout;
    LinearLayout rain_layout,sea_layout,wind_layout;
    Context context;
    ListView rainList,seaList,windList;
    private WindowManager.LayoutParams params;
    boolean rain_isPlaying,sea_isPlaying,wind_isPlaying;


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
        settingtab = (Button)findViewById(R.id.settingtab); //설정기능 버튼
        musicRestart = (Button)findViewById(R.id.musicStart);
        musicPause = (Button) findViewById(R.id.musicStop);
        musicName = (TextView)findViewById(R.id.musicName);

        //노래에 대한 리스트 목록
        rainList = (ListView)findViewById(R.id.rainList);
        seaList = (ListView)findViewById(R.id.seaList);
        windList = (ListView)findViewById(R.id.windList);



        //새 노래와 관련된 부분
        int[] rainSong = {R.raw.asphalt_rain,R.raw.eaves_under_rain,R.raw.large_rain_lightning,
                R.raw.mountain_path_rain,R.raw.small_field_rain,
                R.raw.boardblock_rain,R.raw.valley_rain
                ,R.raw.ruralarea_rain};
        String[] rainTitle = {"아스팔트 위에 내리는 비","처마밑 비","굵은비와 번개","산 오솔길","자잘밭","보드블록 위에 내리는 비","계곡에 내리는 비","시골에서 내리는 비"};
        ArrayAdapter<String> rainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,rainTitle);
        rainList.setAdapter(rainAdapter);
        rainAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_musictab_color,rainTitle);
        rainList.setAdapter(rainAdapter);
        MediaPlayer[] rainMd = new MediaPlayer[rainSong.length];
        //바다 노래
        int[] seaSong = {R.raw.sand_beach_waves,R.raw.mongdol_beach_waves1,R.raw.mongdol_beach_waves2,R.raw.rock_beach_waves,R.raw.gravel_beach_waves,
                        R.raw.getrock_beach_waves,R.raw.big_wave};
        String[] seaTitle = {"모래 해변가 파도","몽돌 해변가 파도(1)","몽돌 해변가 파도(2)","바위 해변가 파도","자갈 해변가 파도","겟바위의 파도", "큰 파도"};
        ArrayAdapter<String> seaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,seaTitle);
        seaList.setAdapter(seaAdapter);
        seaAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_musictab_color,seaTitle);
        seaList.setAdapter(seaAdapter);

        MediaPlayer[] seaMd = new MediaPlayer[seaSong.length];
        //새 노래 클릭했을때
        rainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //새노래가 실행중일때 다른 새노래 클릭시 실행중인 새노래 정지하고 새로 클릭한 새노래 실행
                   if(rain_isPlaying) {
                       rain_isPlaying = false;
                       for(int i =0; i < rainMd.length; i++) {
                           currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                           rainMd[i].stop();
                           rainMd[i].release();
                       }
                   }
                //바다노래 실행하고있으면 바다노래 정지
                if(sea_isPlaying) {
                    sea_isPlaying = false;
                    for(int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].stop();
                        seaMd[i].release();
                    }
                }
                for(int i = 0; i < rainSong.length; i++) {
                    rainMd[i] = MediaPlayer.create(UI_3_Musictab.this, rainSong[i]);
                }

                rainMd[position].start();
                for(int i = position; i < rainSong.length-1; i++) {
                    rainMd[i].setNextMediaPlayer(rainMd[i+1]);

                }

                rain_isPlaying = true;

            }
        });
        //바다 노래 클릭했을때

        seaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                if(rain_isPlaying) {
                    rain_isPlaying = false;
                    for(int i =0; i < rainMd.length; i++) {
                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].stop();
                        rainMd[i].release();
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
                if(rain_isPlaying) {
                    for (int i = 0; i < rainMd.length; i++) {


                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].pause();
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
                if (rain_isPlaying) {
                    for (int i = 0; i < rainMd.length; i++) {
                        if (rainMd[i] != null && !rainMd[i].isPlaying()) {

                            rainMd[i].start();
                            rainMd[i].seekTo(currentMediaPlayPosition);
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
        rain_layout = findViewById(R.id.rain_layout);
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
        settingtab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
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
                rain_layout.setVisibility(View.VISIBLE);
                rainList.setVisibility(View.VISIBLE);
                seaList.setVisibility(View.INVISIBLE);
                windList.setVisibility(View.INVISIBLE);
                break;
            case 1:
                sea_layout.setVisibility(View.VISIBLE);
                wind_layout.setVisibility(View.INVISIBLE);
                rain_layout.setVisibility(View.INVISIBLE);
                rainList.setVisibility(View.INVISIBLE);
                seaList.setVisibility(View.VISIBLE);
                windList.setVisibility(View.INVISIBLE);

                break;
            case 2:
                sea_layout.setVisibility(View.INVISIBLE);
                wind_layout.setVisibility(View.VISIBLE);
                rain_layout.setVisibility(View.INVISIBLE);
                rainList.setVisibility(View.INVISIBLE);
                seaList.setVisibility(View.INVISIBLE);
                windList.setVisibility(View.VISIBLE);
                break;
        }
    }
}

