package com.example.sleep_project;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ringkind extends AppCompatActivity {
    ListView basicMusicList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringkind_layout);
       basicMusicList = (ListView)findViewById(R.id.baseMusicList);
       setTitle("알림음");
        int[] basicSong = {R.raw.life_is_good_cello_quartet,R.raw.life_is_good_elec,R.raw.over_the_horizon,R.raw.clock_sound,R.raw.long_call,R.raw.emergency_ring_tone};
        String[] basicTitle = {"Life's Good(Cello Quartet)","Life's Good(Elec)",
                "Over the Horizon","Clock","Long Phone Call"
                ,"Emergency Ring Tone"
                };
        ArrayAdapter<String> basicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,basicTitle);
        basicMusicList.setAdapter(basicAdapter);

        //기본음악 목록 클릭했을때 radio 버튼이 체크됨
        basicMusicList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //기본음악 목록 클릭했을때
        basicMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}