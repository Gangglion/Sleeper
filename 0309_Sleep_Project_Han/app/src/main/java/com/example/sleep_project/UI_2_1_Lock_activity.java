package com.example.sleep_project;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UI_2_1_Lock_activity extends AppCompatActivity {

    private WindowManager.LayoutParams params;
    private float brightness; // 밝기값은 float형으로 저장되어 있습니다.
    private float changeable; //변동되는 밝기값
    private boolean count=false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_2_1_lock_layout);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        final ImageView imgoff=(ImageView)findViewById(R.id.lockImg);
        params = getWindow().getAttributes();//화면 정보 불러오기
        brightness = params.screenBrightness; //기존밝기 미리 저장

        imgoff.setOnClickListener((v) ->  {
            /*                Toast.makeText(MainActivity.this,Float.toString(brightness),Toast.LENGTH_SHORT).show();*/
            if (count == true) {
                //기능 껐을때
                imgoff.setImageResource(R.drawable.lock3);
                // 기존 밝기로 설정
                params.screenBrightness = brightness;
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                String nowbright = Float.toString(brightness);//바뀐 이후의 밝기 String 타입으로 저장
                Toast.makeText(UI_2_1_Lock_activity.this, nowbright, Toast.LENGTH_SHORT).show();
                count = false;
            } else {
                //기능켰을때
                imgoff.setImageResource(R.drawable.lock2);
                // 최저 밝기로 설정
                params.screenBrightness = 0.1f;
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                changeable = params.screenBrightness; //변경된 밝기 저장
                String nowbright = Float.toString(changeable);//바뀐 이후의 밝기 String 타입으로 저장
                Toast.makeText(UI_2_1_Lock_activity.this, nowbright, Toast.LENGTH_SHORT).show();
                count = true;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //생명주기 onStop()에서 백그라운드 상태면 true를 Logcat에 출력
        Log.d("tmdguq",String.valueOf(isAppIsInBackground(this)));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String runapp=getTopPackageName(this);
        Log.d("runapp",runapp);
    }

    //이벤트가 백그라운드인지 확인하는 메소드 - 백그라운드면 true 반환
    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
    //안드로이드 기기에서 실행중인 앱 가져오는 메소드
    public static String getTopPackageName(@NonNull Context context) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        long lastRunAppTimeStamp = 0L;

        final long INTERVAL = 1000 * 60 * 5;
        final long end = System.currentTimeMillis();
        // 1 minute ago
        final long begin = end - INTERVAL;

        LongSparseArray packageNameMap = new LongSparseArray<>();
        final UsageEvents usageEvents = usageStatsManager.queryEvents(begin, end);
        while (usageEvents.hasNextEvent()) {
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            if(isForeGroundEvent(event)) {
                packageNameMap.put(event.getTimeStamp(), event.getPackageName());
                if(event.getTimeStamp() > lastRunAppTimeStamp) {
                    lastRunAppTimeStamp = event.getTimeStamp();
                }
            }
        }

        return String.valueOf(packageNameMap.get(lastRunAppTimeStamp, ""));
    }
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
