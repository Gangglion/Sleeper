package com.example.Sleeper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UI_2_2_AlarmReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        //Intent alarmIntent = new Intent("android.intent.action.sec");

        String get_yout_string = intent.getExtras().getString("state");      // intent로 부터 전달받은 string 함수 -
                                                                                    // 설정완료를 누르면 Alarm_on 이라는 문자열 저장
        Intent alarmIntent = new Intent(context, UI_2_2_AlarmRing.class);  // UI_2_MaintimertabRing 서비스 intent 생성
                                                                            // UI_2_MaintimertabRing 서비스 intent 생성
        alarmIntent.putExtra("state", get_yout_string); // UI_2_MaintimertabRing으로 extra string값 보내기

        // MaintimertabRing 서비스 시작
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(alarmIntent);
        }else{
            this.context.startService(alarmIntent);
        }
    }
}