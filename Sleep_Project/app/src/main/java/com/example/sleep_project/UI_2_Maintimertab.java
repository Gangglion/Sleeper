package com.example.sleep_project;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UI_2_Maintimertab extends AppCompatActivity implements Runnable{
    Button lockmsg,main, music, statistics, accountbtn, setBtn, setbtn2, confirm,plusBtn,lockcall;
    private FirebaseAuth mAuth;
    AlarmManager alarm_manager;
    TimePicker sleep_timePicker, alarm_timePicker;
    Context context;
    PendingIntent pendingIntent;
    LinearLayout locklayout, timelayout, belllayout,sleepTime_breakTime_View;
    Calendar cal = Calendar.getInstance();
    Calendar calendar;
    int hour, minute, second,result, answer;
    TextView question,waketxt,sleepTimeView,breakTimeView;

    Intent alarm_intent;
    EditText putanswer;
    //화면밝기 관련 선언부
    private WindowManager.LayoutParams params;
    private float brightness; // 밝기값은 float형으로 저장되어 있습니다.
    private float changeable; //변동되는 밝기값
    boolean checkTh=false; //스레드 무한루프 빠져나가기 위한 bool변수
    AudioManager mediaVol;
    CheckPackageNameThread checkPackageNameThread;//가장 최근에 실행한 어플 가져오기 위한 스레드 선언문
    String running=""; //실행중인 어플 저장하는 String
    //특정조건에 맞춰 취소키 안먹게 하기
    @Override
    public void onBackPressed() {
        if(locklayout.getVisibility()==View.VISIBLE){
            //lock 레이아웃이 보일 경우 뒤로가기 키가 안먹는다
        }else{
            super.onBackPressed();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///////
        AlarmQuestion alarmQ = new AlarmQuestion(); //알람화면 랜덤문제생성과 그에 따른 답 생성이 들어있는 클래스
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        setContentView(R.layout.ui_2_maintimertab);
        mAuth = FirebaseAuth.getInstance(); //연결된 계정 불러오기
        timelayout = (LinearLayout)findViewById(R.id.timelayout); //타이머있는 화면
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        accountbtn = (Button) findViewById(R.id.accountTab); //계정관리기능 버튼
        setBtn = (Button) findViewById(R.id.setBtn); //설정완료 버튼
        setbtn2 = (Button) findViewById(R.id.settingbtn); // 설정 버튼
        plusBtn = (Button)findViewById(R.id.plusBtn); //리스트 추가 버튼

        //전화버튼
        lockcall = (Button)findViewById(R.id.lockcall);

        //메시지 버튼
        lockmsg = (Button)findViewById(R.id.lockmsg);

        //기상 예약시간, 취침 예약시간
        sleepTimeView = (TextView)findViewById(R.id.sleepTimeView);
        breakTimeView = (TextView)findViewById(R.id.breakTimeView);
        sleepTime_breakTime_View = (LinearLayout)findViewById(R.id.sleepTime_breakTime_View);


        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_List.class);
                startActivity(intent);
            }
        });
        main.setBackgroundColor(Color.GREEN);
        //알람소리 관련 선언부
        mediaVol = (AudioManager)getSystemService(Context.AUDIO_SERVICE); //기능 시작과 동시에 볼륨 줄이기 위한 선언
        this.context = this;


        //알람화면 랜덤문제 코드
        question = (TextView)findViewById(R.id.question);
        putanswer = (EditText)findViewById(R.id.answer);
        String temp=alarmQ.getQuestion();
        //Log.d("alarmQ",alarmQ.getQuestion());
        question.setText(temp); //문제 TextView에 설정
        //Log.d("alarmQ 텍스트뷰",question.getText().toString());
        answer = alarmQ.getAnswer(); //정답 설정
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람매니저 설정

        sleep_timePicker = findViewById(R.id.sleepTime); // 잠들시간 타임피커 설정
        alarm_timePicker = findViewById(R.id.breakTime); // 일어날시간 타임피커 설정
        calendar = Calendar.getInstance(); // Calendar 객체 생성

        alarm_intent = new Intent(this.context, UI_2_2_AlarmReceiver.class); // 알람리시버 intent 생성
        //잠금화면 관련
        locklayout = (LinearLayout)findViewById(R.id.locklayout);
        waketxt = (TextView)findViewById(R.id.waketxt);

        //알람화면 관련
        belllayout = (LinearLayout)findViewById(R.id.belllayout);
        confirm = (Button) findViewById(R.id.confirm); //문제 답 입력하는 확인버튼
        //화면전환에 이용될 스레드 선언
        Thread th = new Thread(UI_2_Maintimertab.this);
        //잠금화면 나타났을때 화면 어둡게 하기위한 부분
        final ImageView imgoff=(ImageView)findViewById(R.id.lockImg);
        params = getWindow().getAttributes();//화면 정보 불러오기
        brightness = params.screenBrightness; //기존밝기 미리 저장

        //알람화면에서 문제에 대한 답 입력후 확인버튼 눌렀을 때 동작하는 기능
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent alarm_off=new Intent(getApplicationContext(),UI_2_2_AlarmRing.class); //알람인텐트 전역변수
                result = Integer.parseInt(putanswer.getText().toString());
                //문제랑 입력값이랑 같을 시 알람 종료
                if(result == answer) {
                    checkTh=true;
                    stopService(alarm_off);
                    timelayout.setVisibility(View.VISIBLE);
                    locklayout.setVisibility(View.INVISIBLE);
                    setbtn2.setVisibility(View.VISIBLE);
                    belllayout.setVisibility(View.INVISIBLE);
                    sleepTime_breakTime_View.setVisibility(View.INVISIBLE);
                    //알람 취소 하는 부분
                    alarm_manager.cancel(pendingIntent);
                    alarm_intent.putExtra("state","alarm off");
                    sendBroadcast(alarm_intent);
                }
            }
        });
        //설정완료 버튼 누르면 실행되는 스레드 - 화면전환도 관련되어있음
        setBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                th.start();
                // calendar에 시간 세팅
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timePicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timePicker.getMinute());

                // 알람시간을 가져오는 명령
                //int A_hour = alarm_timePicker.getHour();
                //int A_minute = alarm_timePicker.getMinute();

                // 잠금시간을 가져오는 명령
                int L_hour = sleep_timePicker.getHour();
                int L_minute = sleep_timePicker.getMinute();
                //Toast.makeText(getApplicationContext(), "설정", Toast.LENGTH_LONG).show();
                Toast.makeText(UI_2_Maintimertab.this, L_hour + "시 " + L_minute + "분" + "에 화면이 잠깁니다 ", Toast.LENGTH_SHORT).show();

                sleepTime_breakTime_View.setVisibility(View.VISIBLE);
                timelayout.setVisibility(View.INVISIBLE);
                setBtn.setVisibility(View.INVISIBLE);
                String str = "오전";
                int sleep_hour = sleep_timePicker.getHour();
                int alarm_hour = alarm_timePicker.getHour();
                if(sleep_hour > 11) {
                    str = "오후";
                    sleep_hour -= 12;
                    sleepTimeView.setText("취침 예약 시간 :" + str + " " + sleep_hour + "시 " + sleep_timePicker.getMinute()+"분");
                }else {
                    sleepTimeView.setText("취침 예약 시간 :" + str + " " + sleep_hour + "시 " + sleep_timePicker.getMinute()+"분");
                }
                if(alarm_hour > 11) {
                    str = "오후";
                    alarm_hour -= 12;
                    breakTimeView.setText("취침 예약 시간 :" + str + " " + alarm_hour + "시 " + alarm_timePicker.getMinute()+"분");
                }else {
                    breakTimeView.setText("취침 예약 시간 :" + str + " " + alarm_hour + "시 " + alarm_timePicker.getMinute()+"분");
                }
            }
        });
        //////////////////////////////////////////////////////////화면이동 관련///////////////////////////////////////////////////////
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                finish();
            }
        });
        lockcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locklayout.setVisibility(View.INVISIBLE);
                Uri uri = Uri.parse("tel:");
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                startActivity(intent);
            }
        });
        lockmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locklayout.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.putExtra("sms_body","");
                intent.setData(Uri.parse("smsto:" + Uri.encode((""))));
                startActivity(intent);
            }
        });
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
            }
        });
        accountbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_5_AccountTab.class);
                startActivity(intent);
                finish();
            }
        });
        setbtn2.setOnClickListener(new View.OnClickListener() { // UI변경 후 좌측 상단 설정버튼 - 태현
            @Override  // 6번 UI 액티비티 불러오기
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UI_6_SettingActivity.class);
                startActivity(intent);
            }
        });
        //테스트용 임시 코드
        checkPackageNameThread = new CheckPackageNameThread();
        checkPackageNameThread.start();
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (hour == sleep_timePicker.getHour() &&minute == sleep_timePicker.getMinute()) {
                //최저 음량으로 설정
                //mediaVol.setStreamVolume(AudioManager.STREAM_RING,(int)(mediaVol.getStreamMaxVolume(AudioManager.STREAM_RING)*0),0); //java.lang.SecurityException: Not allowed to change Do Not Disturb state
                mediaVol.setRingerMode(AudioManager.RINGER_MODE_SILENT); //음소거 하는 코드
                // 최저 밝기로 설정
                params.screenBrightness = 0.1f;
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                changeable = params.screenBrightness; //변경된 밝기 저장
                String nowbright = Float.toString(changeable);//바뀐 이후의 밝기 String 타입으로 저장
                //Toast.makeText(UI_2_Maintimertab.this, nowbright, Toast.LENGTH_SHORT).show();
                //화면 변환 관련
                timelayout.setVisibility(View.INVISIBLE);
                locklayout.setVisibility(View.VISIBLE);
                setbtn2.setVisibility(View.INVISIBLE);
                belllayout.setVisibility(View.INVISIBLE);
                sleepTime_breakTime_View.setVisibility(View.INVISIBLE);
                if (sleep_timePicker.getHour() <= alarm_timePicker.getHour() && sleep_timePicker.getMinute() < alarm_timePicker.getMinute()) {
                    waketxt.setText((alarm_timePicker.getHour() - hour) + "시간 "
                            + (alarm_timePicker.getMinute() - minute) + "분 남았습니다");
                }
                Log.i("성공", "성공");
            } else if (hour == alarm_timePicker.getHour() && minute == alarm_timePicker.getMinute()) {
                //기존 음량으로 설정
                //mediaVol.setStreamVolume(AudioManager.STREAM_RING,(int)(mediaVol.getStreamMaxVolume(AudioManager.STREAM_RING)*1),0);
                mediaVol.setRingerMode(AudioManager.RINGER_MODE_NORMAL); //음소거 푸는 코드
                // 기존 밝기로 설정
                params.screenBrightness = brightness;
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                String nowbright = Float.toString(brightness);//바뀐 이후의 밝기 String 타입으로 저장
                //Toast.makeText(UI_2_Maintimertab.this, nowbright, Toast.LENGTH_SHORT).show();
                alarm_intent.putExtra("state", "alarm on");  // receiver에 string 값 넘겨주기

                pendingIntent = PendingIntent.getBroadcast(UI_2_Maintimertab.this, 0, alarm_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent); // 알람셋팅
                //화면 변환 관련
                timelayout.setVisibility(View.INVISIBLE);
                locklayout.setVisibility(View.INVISIBLE);
                setbtn2.setVisibility(View.INVISIBLE);
                belllayout.setVisibility(View.VISIBLE);
            } else {
                Log.i("실패", "실패");
            }

        }
    };
    @Override
    public void run() {
        hour = cal.get(cal.HOUR_OF_DAY);
        minute = cal.get(cal.MINUTE);
        second = cal.get(cal.SECOND);
        while (!checkTh) {
            second++;
            if (second > 59) {
                second = 0;
                minute++;
            }
            if (minute > 59) {
                hour++;
                minute = 0;
            }
            if (hour > 23) {
                hour = 0;
            }
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
            try {
                Log.i("Time", "hour" + hour + ", minute" + minute + ", second" + second);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    //특정조건 하에서 메뉴키 막기 위한 코드
    @Override
    protected void onPause() {
        super.onPause();
        if(locklayout.getVisibility()==View.VISIBLE){
            ActivityManager activityManager = (ActivityManager) getApplicationContext()
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }else{
        }
    }

    /////하단 부분은 다른 앱 실행 감지시 종료 혹은 이 앱으로 덮어씌우기 위한 메소드 입니다/////
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
/*        //생명주기 onStop()에서 백그라운드 상태면 true를 Logcat에 출력 - reStart시에는 백그라운드가 아니므로 로그캣에 false를 반환할 것
        Log.d("tmdguq",String.valueOf(isAppIsInBackground(this)));*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    //설치되있는 어플목록 리스트로 반환하는 메소드 - 현재 액티비티에서 사용하는 메소드는 아니지만 나중에 설정탭에서 사용가능
    private ArrayList<String> installedApp(){
        final PackageManager pm = getPackageManager();//설치된 모든 앱리스트 가져오기 위해 선언
        List<ApplicationInfo> list=pm.getInstalledApplications(0); //설치된 정보를 받아와 리스트형식으로 저장
        ArrayList<String> installed = new ArrayList<>();
        for(ApplicationInfo applicationInfo : list){
            String pName=applicationInfo.packageName;
            installed.add(pName);
        }
        return installed;
    }
    //앱이 포그라운드 상태인지 확인하는 메소드
    private static boolean isForeGroundEvent(UsageEvents.Event event) {
        if(event == null) return false;
        if(BuildConfig.VERSION_CODE >= 29)
            return event.getEventType() == UsageEvents.Event.ACTIVITY_RESUMED;
        return event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND;
    }
    //가장 마지막에 실행된 어플의 패키지를 리턴해주는 메소드
    public static String getPackageName(@NonNull Context context) {

        // UsageStatsManager 선언
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);

        long lastRunAppTimeStamp = 0L;

        // 얼마만큼의 시간동안 수집한 앱의 이름을 가져오는지 정하기 (begin ~ end 까지의 앱 이름을 수집한다)
        final long INTERVAL = 10000;
        final long end = System.currentTimeMillis();
        // 1 minute ago
        final long begin = end - INTERVAL;

        //
        LongSparseArray packageNameMap = new LongSparseArray<>();

        // 수집한 이벤트들을 담기 위한 UsageEvents
        final UsageEvents usageEvents = usageStatsManager.queryEvents(begin, end);

        // 이벤트가 여러개 있을 경우 (최소 존재는 해야 hasNextEvent가 null이 아니니까)
        while (usageEvents.hasNextEvent()) {

            // 현재 이벤트를 가져오기
            UsageEvents.Event event = new UsageEvents.Event();
            usageEvents.getNextEvent(event);

            // 현재 이벤트가 포그라운드 상태라면 = 현재 화면에 보이는 앱이라면
            if(isForeGroundEvent(event)) {
                // 해당 앱 이름을 packageNameMap에 넣는다.
                packageNameMap.put(event.getTimeStamp(), event.getPackageName());
                // 가장 최근에 실행 된 이벤트에 대한 타임스탬프를 업데이트 해준다.
                if(event.getTimeStamp() > lastRunAppTimeStamp) {
                    lastRunAppTimeStamp = event.getTimeStamp();
                }
            }
        }
        // 가장 마지막까지 있는 앱의 이름을 리턴해준다.
        return packageNameMap.get(lastRunAppTimeStamp, "").toString();
    }
    //앱 실행기록을 위한 권한 체크
    private boolean checkPermission(){
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                .getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getApplicationContext().getPackageName());
        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (getApplicationContext().checkCallingOrSelfPermission(
                    android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        }
        else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }
        return granted;
    }
    //가장 최근에 실행한 어플의 패키지를 주기적으로 리턴해주기 위한 스레드
    private class CheckPackageNameThread extends Thread{
        public void run(){
            while(true){
                if(!checkPermission()) continue;
                running = getPackageName(getApplicationContext());
                Log.d("tmdguq running",running);
                //해당 어플이 아닌 다른 어플이 실행되었을 경우 종료 - 현재 if문이 작동하지 않는다...
                if(!running.equals("")){
                    Log.d("tmdguq alert",getPackageName(getApplicationContext())+"실행되고있습니다");
                    //am.killBackgroundProcesses(running);
                    ActivityManager am = (ActivityManager)getSystemService(ACTIVITY_SERVICE);

                    am.restartPackage( getPackageName() );
                }else
                {

                }
                try {
                    sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
