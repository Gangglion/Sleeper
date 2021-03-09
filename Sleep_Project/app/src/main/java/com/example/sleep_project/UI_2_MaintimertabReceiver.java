package com.example.sleep_project;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UI_2_MaintimertabReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        String get_yout_string = intent.getExtras().getString("state");      // intent로 부터 전달받은 string 함수
        Intent service_intent = new Intent(context, UI_2_MaintimertabRing.class);  // UI_2_MaintimertabRing 서비스 intent 생성
        service_intent.putExtra("state", get_yout_string); // UI_2_MaintimertabRing으로 extra string값 보내기

        // MaintimertabRing 서비스 시작
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }else{
            this.context.startService(service_intent);
        }
    }
}