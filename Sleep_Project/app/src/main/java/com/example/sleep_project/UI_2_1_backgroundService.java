/*
package com.example.sleep_project;

import android.app.Service;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UI_2_1_backgroundService extends Service {
    backthread backth; //백그라운드에서 작동할 스레드 선언
    ArrayList<String> running = new ArrayList<>();
    @Override public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        backth=new backthread();
        backth.stopForever();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        backth = new backthread();
        backth.start();
    }
}
class backthread extends Thread{
    boolean isRun=true;
    public void stopForever(){
        synchronized (this){
            this.isRun=false;
        }
    }
    public void run(){
        while(isRun){

        }
    }
    //현재 안드로이드 화면에 실행중인 어플 패키지명 반환하는 메소드
    private static String getrunningapp(@NonNull Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        //long lastRunAppTimeStamp = 0L;

        final long INTERVAL = 1000 * 60 * 5;
        final long end = System.currentTimeMillis();
        // 1 minute ago
        final long begin = end - INTERVAL;

        String running="";
        //LongSparseArray packageNameMap = new LongSparseArray<>();
        final UsageEvents usageEvents = usageStatsManager.queryEvents(begin, end);
        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);
            //이벤트가 포그라운드 인지 확인하는 메소드
            if(isForeGroundEvent(event)) {
                running=event.getPackageName();
*/
/*                if(event.getTimeStamp() > lastRunAppTimeStamp) {
                    lastRunAppTimeStamp = event.getTimeStamp();
                }*//*

            }
        }
        return running;
    }
    //이벤트가 포그라운드 이벤트인지 확인하는 메소드
    private static boolean isForeGroundEvent(UsageEvents.Event event) {
        if(event == null) {
            return false;
        }

        if(BuildConfig.VERSION_CODE >= 29) {
            return event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED;
        }

        return event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND;
    }
}
*/
