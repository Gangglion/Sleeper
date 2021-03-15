package com.example.sleep_project;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class UI_2_1_backgroundService extends Service {
    Thread backth; //백그라운드에서 작동할 스레드 선언
    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
