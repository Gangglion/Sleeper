package com.example.Sleeper;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class ringkind extends AppCompatActivity {
    ListView basicMusicList;
    private final static int REQUESTCODE_RINGTONE_PICKER = 1000;
    private String m_strRingToneUri;
    prefvalue prefOb = new prefvalue();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringkind_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        showRingtoneChooser();
    }
    private void showRingtoneChooser(){
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "알람 음악을 선택하세요!");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        //-- 알림 선택창이 떴을 때, 기본값으로 선택되어질 ringtone설정
        String choice = m_strRingToneUri;
        if (m_strRingToneUri != null && m_strRingToneUri.isEmpty()) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(m_strRingToneUri));
        }else{
        }
        this.startActivityForResult(intent, REQUESTCODE_RINGTONE_PICKER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODE_RINGTONE_PICKER) {
            if (resultCode == RESULT_OK) { // -- 알림음 재생하는 코드 -- //
                Uri ring = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                if (ring != null) {
                    m_strRingToneUri = ring.toString();
                    Toast.makeText(getApplicationContext(),"선택한 벨소리의 uri : " + m_strRingToneUri,Toast.LENGTH_SHORT).show();
                    //선택한 벨소리 sharedPreferences에 저장 - 이후 다시 눌렀을때 선택한 값 유지되면 됨
                    SharedPreferences.Editor editor = prefOb.pref.edit();
                    editor.putString("alarmring",m_strRingToneUri);
                    editor.commit();
                } else {
                    m_strRingToneUri = null;
                }
            }
        }
        finish();
    }

//       basicMusicList = (ListView)findViewById(R.id.baseMusicList);
//       setTitle("알림음");
//        int[] basicSong = {R.raw.life_is_good_cello_quartet,R.raw.life_is_good_elec,R.raw.over_the_horizon,R.raw.clock_sound,R.raw.long_call,R.raw.emergency_ring_tone};
//        String[] basicTitle = {"Life's Good(Cello Quartet)","Life's Good(Elec)",
//                "Over the Horizon","Clock","Long Phone Call"
//                ,"Emergency Ring Tone"
//                };
//        ArrayAdapter<String> basicAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice,basicTitle);
//        basicMusicList.setAdapter(basicAdapter);
//
//        //기본음악 목록 클릭했을때 radio 버튼이 체크됨
//        basicMusicList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//
//        //기본음악 목록 클릭했을때
//        basicMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });
}