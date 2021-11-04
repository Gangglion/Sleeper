package com.example.sleep_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;

public class UI_3_Musictab extends AppCompatActivity {
    Button main, music, statistics, settingtab;
    ImageButton musicRestart, musicPause;
    TextView musicName;
    int temp = 0;
    int prePosition = 0;
    TabLayout tabLayout;
    LinearLayout rain_layout, sea_layout, wind_layout;
    Context context;
    ListView rainList, seaList, windList;
    private WindowManager.LayoutParams params;
    boolean rain_isPlaying, sea_isPlaying, wind_isPlaying;
    int seaDuration, windDuration, rainDuration;
    MediaPlayer[] rainMd, windMd, seaMd;
    MusicHandler seaHandler,windHandler,rainHandler;
    int nextPosition;

    String[] seaTitle = {"모래 해변가 파도", "몽돌 해변가 파도(1)", "몽돌 해변가 파도(2)", "바위 해변가 파도", "자갈 해변가 파도", "겟바위의 파도", "큰 파도"};
    String[] rainTitle = {"아스팔트 위에 내리는 비", "처마밑 비", "굵은비와 번개", "산 오솔길", "자잘밭", "보드블록 위에 내리는 비", "계곡에 내리는 비", "시골에서 내리는 비"};
    String[] windTitle = {"힐링을 위한 바람","폭풍우치는 바람","창문가에 들리는 바람","진한 바람","조금씩 줄어드는 바람","잔잔한 봄바람","시원한 바람","숨속의 바람","백색소음 바람","바람"};
    MusicThread seaThread, rainThread,windThread;
    //버튼이 여러개 있으나 노래가 3개라 9개만 해놈.
    int currentMediaPlayPosition; // 현재 실행되고 있는 음악 위치

    NetworkStatus networkStatus = new NetworkStatus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_3_musictab);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        setTitle("음악");
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        settingtab = (Button) findViewById(R.id.settingtab); //설정기능 버튼
        musicRestart = (ImageButton) findViewById(R.id.musicStart);
        musicPause = (ImageButton) findViewById(R.id.musicStop);
        musicName = (TextView) findViewById(R.id.musicName);

        //노래에 대한 리스트 목록
        rainList = (ListView) findViewById(R.id.rainList);
        seaList = (ListView) findViewById(R.id.seaList);
        windList = (ListView) findViewById(R.id.windList);

        //새 노래와 관련된 부분
        int[] rainSong = {R.raw.asphalt_rain, R.raw.eaves_under_rain, R.raw.large_rain_lightning,
                R.raw.mountain_path_rain, R.raw.small_field_rain,
                R.raw.boardblock_rain, R.raw.valley_rain
                , R.raw.ruralarea_rain};
        ArrayAdapter<String> rainAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, rainTitle);
        rainList.setAdapter(rainAdapter);
        rainAdapter = new ArrayAdapter<String>(this, R.layout.ui_3_musictab_color, rainTitle);
        rainList.setAdapter(rainAdapter);
        rainMd = new MediaPlayer[rainSong.length];
        //바다 노래
        int[] seaSong = {R.raw.sand_beach_waves, R.raw.mongdol_beach_waves1, R.raw.mongdol_beach_waves2, R.raw.rock_beach_waves, R.raw.gravel_beach_waves,
                R.raw.getrock_beach_waves, R.raw.big_wave};

        ArrayAdapter<String> seaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seaTitle);
        seaList.setAdapter(seaAdapter);
        seaAdapter = new ArrayAdapter<String>(this, R.layout.ui_3_musictab_color, seaTitle);
        seaList.setAdapter(seaAdapter);

        seaMd = new MediaPlayer[seaSong.length];
        //바다 노래

        //String[] windTitle = {"힐링을 위한 바람","폭풍우치는 바람","창문가에 들리는 바람","진한 바람","조금씩 줄어드는 바람","잔잔한 봄바람","시원한 바람","숨속의 바람","백색소음 바람","바람"}

        int[] windSong = {R.raw.sound_healing_wind, R.raw.storm_wind,R.raw.window_sound_wind,R.raw.deep_noise_wind ,
                R.raw.littleby_little_noise_wind, R.raw.spring_wind
                , R.raw.cool_wind,R.raw.forest_in_the_wind,
                R.raw.white_noise_wind,R.raw.wind_sound,};

        ArrayAdapter<String> windAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, windTitle);
        windList.setAdapter(windAdapter);
        windAdapter = new ArrayAdapter<String>(this, R.layout.ui_3_musictab_color, windTitle);
        windList.setAdapter(windAdapter);

        windMd = new MediaPlayer[windSong.length];


        //새 노래 클릭했을때
        rainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //새노래가 실행중일때 다른 새노래 클릭시 실행중인 새노래 정지하고 새로 클릭한 새노래 실행
                if (rain_isPlaying) {
                    rain_isPlaying = false;
                    for (int i = 0; i < rainMd.length; i++) {
                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].stop();
                        rainMd[i].release();
                    }
                }
                //바다노래 실행하고있으면 바다노래 정지
                if (sea_isPlaying) {
                    sea_isPlaying = false;
                    for (int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].stop();
                        seaMd[i].release();
                        seaThread.stop();
                    }
                }
                if(wind_isPlaying) {
                    wind_isPlaying = false;
                    for (int i = 0; i < windMd.length; i++) {
                        currentMediaPlayPosition = windMd[i].getCurrentPosition();
                        windMd[i].stop();
                        windMd[i].release();
                    }
                }
                for (int i = 0; i < rainSong.length; i++) {
                    rainMd[i] = MediaPlayer.create(UI_3_Musictab.this, rainSong[i]);
                }

                rainMd[position].start();
                for (int i = position; i < rainSong.length - 1; i++) {
                    rainMd[i].setNextMediaPlayer(rainMd[i + 1]);

                }

                rain_isPlaying = true;
                nextPosition = position;
                rainHandler = new MusicHandler();
                rainThread = new MusicThread(rain_isPlaying, rainTitle, position);

                rainThread.start();

            }
        });
        //바다 노래 클릭했을때

        seaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (sea_isPlaying) {
                    sea_isPlaying = false;
                    for (int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].stop();
                        seaMd[i].release();
                    }
                }
                if (rain_isPlaying) {
                    rain_isPlaying = false;
                    for (int i = 0; i < rainMd.length; i++) {
                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].stop();
                        rainMd[i].release();
                    }
                }
                if(wind_isPlaying) {
                    wind_isPlaying = false;
                    for (int i = 0; i < windMd.length; i++) {
                        currentMediaPlayPosition = windMd[i].getCurrentPosition();
                        windMd[i].stop();
                        windMd[i].release();
                    }
                }
                for (int i = 0; i < seaSong.length; i++) {
                    seaMd[i] = MediaPlayer.create(UI_3_Musictab.this, seaSong[i]);

                }
                seaMd[position].start();
                for (int i = position; i < seaSong.length - 1; i++) {
                    seaMd[i].setNextMediaPlayer(seaMd[i + 1]);

                }

                // musicName.setText(seaTitle[position]);

              /*  Log.d("현재음악위치 : ", Integer.toString(seaMd[position].getCurrentPosition()));
                Log.d("재생시간 : ", Integer.toString(seaMd[position].getDuration()));*/
                sea_isPlaying = true;
                nextPosition = position;
                seaHandler = new MusicHandler();
                seaThread = new MusicThread(sea_isPlaying, seaTitle, position);
                seaThread.start();

            }
        });

        windList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (sea_isPlaying) {
                    sea_isPlaying = false;
                    for (int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].stop();
                        seaMd[i].release();
                    }
                }
                if (rain_isPlaying) {
                    rain_isPlaying = false;
                    for (int i = 0; i < rainMd.length; i++) {
                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].stop();
                        rainMd[i].release();
                    }
                }
                if(wind_isPlaying) {
                    wind_isPlaying = false;
                    for (int i = 0; i < windMd.length; i++) {
                        currentMediaPlayPosition = windMd[i].getCurrentPosition();
                        windMd[i].stop();
                        windMd[i].release();
                    }
                }
                for (int i = 0; i < windSong.length; i++) {
                    windMd[i] = MediaPlayer.create(UI_3_Musictab.this, windSong[i]);

                }
                windMd[position].start();
                for (int i = position; i < windSong.length - 1; i++) {
                    windMd[i].setNextMediaPlayer(windMd[i + 1]);

                }
                wind_isPlaying = true;
                nextPosition = position;
                windHandler = new MusicHandler();
                windThread = new MusicThread(wind_isPlaying, windTitle, position);
                windThread.start();
            }

        });

        //바람 노래와 관련된 부분

        //노래 일시정지
        musicPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rain_isPlaying) {
                    for (int i = 0; i < rainMd.length; i++) {
                        currentMediaPlayPosition = rainMd[i].getCurrentPosition();
                        rainMd[i].pause();
                    }

                } else if (sea_isPlaying) {
                    for (int i = 0; i < seaMd.length; i++) {
                        currentMediaPlayPosition = seaMd[i].getCurrentPosition();
                        seaMd[i].pause();
                    }
                }else if(wind_isPlaying){
                    for (int i = 0; i < windMd.length; i++) {
                        currentMediaPlayPosition = windMd[i].getCurrentPosition();
                        windMd[i].pause();
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
                } else if (wind_isPlaying) {
                    for (int i = 0; i < windMd.length; i++) {
                        if (windMd[i] != null && !windMd[i].isPlaying()) {

                            windMd[i].start();
                            windMd[i].seekTo(currentMediaPlayPosition);
                        }
                    }
                }

            }
        });
        //새 , 바람 , 바다 탭 이미지 설정
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        tabLayout.getTabAt(0).setIcon(R.drawable.rain);
        tabLayout.getTabAt(1).setIcon(R.drawable.sea);
        tabLayout.getTabAt(2).setIcon(R.drawable.wind);
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
                if(networkStatus.isConnected(context))
                {
                    Log.d("networkstatus","연결됨");
                    Intent intent = new Intent(getApplicationContext(), UI_4_StatisticsTab_Bar.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Log.d("networkstatus","연결안됨");
                    Toast.makeText(UI_3_Musictab.this, "통계를 보려면 인터넷에 연결된 상태여야 합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                    startActivity(intent);
                }
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
            case 0:
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



    class MusicThread extends Thread {
        boolean musicIsPlaying;
        int position;
        String title[];
        MusicThread(){}

        MusicThread(boolean musicIsPlaying, String title[], int position) {
            this.musicIsPlaying = musicIsPlaying;
            this.position = position;
            this.title = title;
        }

        public void run() {

            while (musicIsPlaying) {
                try {
                    if (sea_isPlaying == true) {
                        Log.d("현재재생위치 : ", Integer.toString(seaMd[position].getCurrentPosition()));
                        Log.d("재생 시간 : ", Integer.toString(seaMd[position].getDuration()));
                        Log.d("현재 실행중인 음악제목 ", seaTitle[position]);
                        Log.d("포지션 값 ", "다음곡을 재생할거다");
                        Message msg = seaHandler.obtainMessage();
                        seaHandler.sendMessage(msg);
                        Thread.sleep(seaMd[position].getDuration());
                        rain_isPlaying = false;
                        wind_isPlaying = false;

                    }  if (rain_isPlaying == true) {
                        Log.d("현재재생위치 : ", Integer.toString(rainMd[position].getCurrentPosition()));
                        Log.d("재생 시간 : ", Integer.toString(rainMd[position].getDuration()));
                        Log.d("현재 실행중인 음악제목 ", rainTitle[position]);
                        Log.d("포지션 값 ", "다음곡을 재생할거다");
                        wind_isPlaying = false;
                        sea_isPlaying = false;
                        Message msg = rainHandler.obtainMessage();
                        rainHandler.sendMessage(msg);
                        Thread.sleep(rainMd[position].getDuration());
                    } if(wind_isPlaying == true) {
                        Log.d("현재재생위치 : ", Integer.toString(windMd[position].getCurrentPosition()));
                        Log.d("재생 시간 : ", Integer.toString(windMd[position].getDuration()));
                        Log.d("현재 실행중인 음악제목 ", windTitle[position]);
                        Log.d("포지션 값 ", "다음곡을 재생할거다");
                        rain_isPlaying = false;
                        sea_isPlaying = false;
                        Message msg = windHandler.obtainMessage();
                        windHandler.sendMessage(msg);
                        Thread.sleep(windMd[position].getDuration());
                    }
                    //Log.d("인덱스 값 : " , Integer.toString(position));
                    position++;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


    }

    class MusicHandler extends Handler {




        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);


            Log.d("nextPosition 값 : ",  Integer.toString(nextPosition));
            if (sea_isPlaying == true) {
                musicName.setText(seaTitle[nextPosition]);

            } if (rain_isPlaying == true) {
                musicName.setText(rainTitle[nextPosition]);

            } if (wind_isPlaying == true) {
                musicName.setText(windTitle[nextPosition]);
            }
            nextPosition++;
        }
    }

}

