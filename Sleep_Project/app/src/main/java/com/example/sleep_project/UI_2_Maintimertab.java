package com.example.sleep_project;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UI_2_Maintimertab extends AppCompatActivity{
    Button lockmsg,main, music, statistics,Settings, setBtn, confirm,plusBtn,lockcall,lockclear;
    private FirebaseAuth mAuth;
    AlarmManager alarm_manager;
    TimePicker sleep_timePicker, alarm_timePicker;
    Context context;
    PendingIntent pendingIntent;
    LinearLayout locklayout, timelayout, belllayout,sleepTime_breakTime_View, bottom_menu;
    Calendar cal = Calendar.getInstance();
    Calendar calendar;
    int hour, minute, second,result, answer;
    TextView question,waketxt,sleepTimeView,breakTimeView;

    String setsleepT; //상단바알림에 띄울 문자열 저장변수
    NotificationCompat.Builder builder; //상단바 관련 builder
    NotificationManager manager; //상단바 관련 manager

    Intent alarm_intent;
    EditText putanswer;
    //화면밝기 관련 선언부
    private WindowManager.LayoutParams params;
    private float brightness; // 기존밝기 저장
    prefvalue prefOb; //설정해둔 값 가져오기 위한 SharedPreference 접근객체

    boolean checkTh=false; //스레드 무한루프 빠져나가기 위한 bool변수
    public AudioManager mediaVol;
    String running=""; //실행중인 어플 저장하는 String

    //기능 실행시 화면 변경 기능을 수행할 핸들러 선언 및 초기화문
    valueHandler handler = new valueHandler();
    Thread thread;

    boolean dlgcheck=false;
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
        setContentView(R.layout.ui_2_maintimertab);

        prefOb = new prefvalue();
        if(prefOb.pref == null){
            Intent gosetting = new Intent(getApplicationContext(),SettingsActivity.class);
            startActivity(gosetting);
        }
        ///////
        AlarmQuestion alarmQ = new AlarmQuestion(); //알람화면 랜덤문제생성과 그에 따른 답 생성이 들어있는 클래스
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /////////////////////////////////////////////
        mAuth = FirebaseAuth.getInstance(); //연결된 계정 불러오기
        timelayout = (LinearLayout)findViewById(R.id.timelayout); //타이머있는 화면
        bottom_menu = (LinearLayout)findViewById(R.id.bottom_menu); //하단 메뉴 레이아웃
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        Settings = (Button) findViewById(R.id.Settings); //설정기능 버튼
        setBtn = (Button) findViewById(R.id.setBtn); //설정완료 버튼
        plusBtn = (Button)findViewById(R.id.plusBtn); //리스트 추가 버튼
        lockclear = (Button)findViewById(R.id.lockclear);

        //전화버튼
        lockcall = (Button)findViewById(R.id.lockcall);

        //메시지 버튼
        lockmsg = (Button)findViewById(R.id.lockmsg);

        //기상 예약시간, 취침 예약시간
        sleepTimeView = (TextView)findViewById(R.id.sleepTimeView);
        breakTimeView = (TextView)findViewById(R.id.breakTimeView);
        sleepTime_breakTime_View = (LinearLayout)findViewById(R.id.sleepTime_breakTime_View);


        Intent intent = getIntent();
        int sleepHour = intent.getIntExtra("SleepHour",0);
        int sleepMin = intent.getIntExtra("SleepMin",1);
        int breakHour = intent.getIntExtra("BreakHour",2);
        int breakMin = intent.getIntExtra("BreakMin",3);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_List.class);

                startActivity(intent);
            }
        });
        //알람소리 관련 선언부
        mediaVol = (AudioManager)getSystemService(Context.AUDIO_SERVICE); //기능 시작과 동시에 볼륨 줄이기 위한 선언
        this.context = this;

        question = (TextView)findViewById(R.id.question);
        putanswer = (EditText)findViewById(R.id.answer);
        String temp=alarmQ.getQuestion();
        answer = alarmQ.getAnswer(); //정답 설정
        Intent alarm_off=new Intent(getApplicationContext(),UI_2_2_AlarmRing.class); //알람인텐트 전역변수
        //알람화면 랜덤문제 코드 - prefOb에 모닝콜 안뜨게 하면 문제가 안뜨고, 확인버튼만 누르면 알람이 꺼지게 해야함
//        if(prefOb.getQuesvalue()){
//            question.setText(temp); //문제 TextView에 설정
//        }
        question.setText(temp); //문제 TextView에 설정


        //Log.d("alarmQ",alarmQ.getQuestion());

        //Log.d("alarmQ 텍스트뷰",question.getText().toString());
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람매니저 설정

        sleep_timePicker = findViewById(R.id.sleepTime); // 잠들시간 타임피커 설정
        sleep_timePicker.setHour(sleepHour);
        sleep_timePicker.setMinute(sleepMin);
        alarm_timePicker = findViewById(R.id.breakTime); // 일어날시간 타임피커 설정


            alarm_timePicker.setHour(breakHour);
            alarm_timePicker.setMinute(breakMin);



        /////////////////////////////////타임피커 색 변경을 위한 코드//////////////////////////////////////
        int hour_NumberPicker_id = Resources.getSystem().getIdentifier("hour", "id", "android");
        int minute_NumberPicker_id = Resources.getSystem().getIdentifier("minute", "id", "android");
        int daynight_id=Resources.getSystem().getIdentifier("amPm","id","android");
        NumberPicker hourNumberPicker1 = (NumberPicker)sleep_timePicker.findViewById(hour_NumberPicker_id);
        NumberPicker minuteNumberPicker1 = (NumberPicker)sleep_timePicker.findViewById(minute_NumberPicker_id);
        NumberPicker daynight1=(NumberPicker) sleep_timePicker.findViewById(daynight_id);
        NumberPicker hourNumberPicker2 = (NumberPicker)alarm_timePicker.findViewById(hour_NumberPicker_id);
        NumberPicker minuteNumberPicker2 = (NumberPicker)alarm_timePicker.findViewById(minute_NumberPicker_id);
        NumberPicker daynight2=(NumberPicker) alarm_timePicker.findViewById(daynight_id);
        TimepickerColorChange colorChange = new TimepickerColorChange();
        colorChange.setNumberPickerTextColor(hourNumberPicker1, Color.WHITE);
        colorChange.setNumberPickerTextColor(hourNumberPicker2, Color.WHITE);
        colorChange.setNumberPickerTextColor(minuteNumberPicker1, Color.WHITE);
        colorChange.setNumberPickerTextColor(minuteNumberPicker2, Color.WHITE);
        colorChange.setNumberPickerTextColor(daynight1,Color.WHITE);
        colorChange.setNumberPickerTextColor(daynight2,Color.WHITE);
        colorChange.setNumberPickerDividerColour(hourNumberPicker1,this);
        colorChange.setNumberPickerDividerColour(hourNumberPicker2,this);
        colorChange.setNumberPickerDividerColour(minuteNumberPicker1,this);
        colorChange.setNumberPickerDividerColour(minuteNumberPicker2,this);
        colorChange.setNumberPickerDividerColour(daynight1,this);
        colorChange.setNumberPickerDividerColour(daynight2,this);
        //////////////////////////////////////////////////////////////////////////////////////////////////

        calendar = Calendar.getInstance(); // Calendar 객체 생성

        alarm_intent = new Intent(this.context, UI_2_2_AlarmReceiver.class); // 알람리시버 intent 생성
        //잠금화면 관련
        locklayout = (LinearLayout)findViewById(R.id.locklayout);
        waketxt = (TextView)findViewById(R.id.waketxt);

        //알람화면 관련
        belllayout = (LinearLayout)findViewById(R.id.belllayout);
        confirm = (Button) findViewById(R.id.confirm); //문제 답 입력하는 확인버튼

        //잠금화면 나타났을때 화면 어둡게 하기위한 부분
        final ImageView imgoff=(ImageView)findViewById(R.id.lockImg);
        params = getWindow().getAttributes();//화면 정보 불러오기
        brightness = params.screenBrightness; //기존밝기 미리 저장

        //설정완료 버튼 누르면 실행되는 스레드 - 화면전환도 관련되어있음
        setBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                thread = new LockRunThread();
                thread.start();
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
                    setsleepT = "취침 예약 시간 : " + str + " " + sleep_hour + "시 " + sleep_timePicker.getMinute()+"분";
                }else {
                    sleepTimeView.setText("취침 예약 시간 :" + str + " " + sleep_hour + "시 " + sleep_timePicker.getMinute()+"분");
                    setsleepT = "취침 예약 시간 :" + str + " " + sleep_hour + "시 " + sleep_timePicker.getMinute()+"분";
                }
                if(alarm_hour > 11) {
                    str = "오후";
                    alarm_hour -= 12;
                    breakTimeView.setText("기상 예약 시간 :" + str + " " + alarm_hour + "시 " + alarm_timePicker.getMinute()+"분");
                }else {
                    breakTimeView.setText("기상 예약 시간 :" + str + " " + alarm_hour + "시 " + alarm_timePicker.getMinute()+"분");
                }
                showNoti(setsleepT);

                //설정시간 파이어베이스에 넘김
                AboutLogin aboutLogin = new AboutLogin();
                if(aboutLogin.checklogin()){
                    String username = aboutLogin.getUser().getDisplayName();
                    String sleeptime = sleep_timePicker.getHour()+":"+sleep_timePicker.getMinute();
                    String waketime = alarm_timePicker.getHour()+":"+alarm_timePicker.getMinute();
                    firebasepost firebasepost = new firebasepost(username,sleeptime,waketime);
                    firebasepost.postFirebaseDatabaseTime(true);
                }
            }
        });
        lockclear.setOnClickListener(new View.OnClickListener() { //잠금상태에서 잠금해제 버튼 눌렀을때 동작 - 문제점 : 스레드가 안멈춤
            @Override
            public void onClick(View v) {
                checkTh = true;
                thread.interrupt();
                stopService(alarm_off);
                timelayout.setVisibility(View.VISIBLE);
                locklayout.setVisibility(View.INVISIBLE);
                belllayout.setVisibility(View.INVISIBLE);
                sleepTime_breakTime_View.setVisibility(View.INVISIBLE);
                setBtn.setVisibility(View.VISIBLE);
                bottom_menu.setVisibility(View.VISIBLE);
            }
        });
        //알람화면에서 문제에 대한 답 입력후 확인버튼 눌렀을 때 동작하는 기능
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result = Integer.parseInt(putanswer.getText().toString());
                //문제랑 입력값이랑 같을 시 알람 종료
                if(result == answer) {
                    checkTh = true;
                    thread.interrupt();
                    stopService(alarm_off);
                    timelayout.setVisibility(View.VISIBLE);
                    locklayout.setVisibility(View.INVISIBLE);
                    belllayout.setVisibility(View.INVISIBLE);
                    sleepTime_breakTime_View.setVisibility(View.INVISIBLE);
                    setBtn.setVisibility(View.VISIBLE);
                    bottom_menu.setVisibility(View.VISIBLE);
                    //알람 취소 하는 부분
                    alarm_manager.cancel(pendingIntent);
                    alarm_intent.putExtra("state","alarm off");
                    sendBroadcast(alarm_intent);
                }
            }
        });
        //////////////////////////////////////////////////////////화면이동 관련///////////////////////////////////////////////////////
        main.setBackgroundColor(Color.GRAY);
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
        Settings.setOnClickListener(new View.OnClickListener() { // UI변경 후 좌측 상단 설정버튼 - 태현
            @Override  // 6번 UI 액티비티 불러오기
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(dlgcheck){
            AlertDialog.Builder dlg = new AlertDialog.Builder(UI_2_Maintimertab.this);
            dlg.setTitle("지금은 수면시간입니다!"); //제목
            dlg.setMessage("수면시간동안 어플의 사용이 제한됩니다!"); // 메시지
            dlg.setIcon(R.drawable.sleeper_icon); // 아이콘 설정
            //버튼 클릭시 동작
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which) {
                    //토스트 메시지
                    //Toast.makeText(UI_2_Maintimertab.this,"확인을 눌르셨습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            dlg.show();
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

    //상단 알림띄우기 위함
    public void showNoti(String sleepT){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, UI_2_Maintimertab.class);
        intent.putExtra("setSleepT",sleepT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //알림창 제목
        builder.setContentTitle("Sleeper가 당신이 자기로 시간을 알려줍니다!");
        //알림창 메시지
        builder.setContentText(sleepT);
        //알림창 아이콘
        builder.setSmallIcon(R.drawable.sleeper_icon);
        //알림창 터치시 상단 알림상태창에서 알림이 자동으로 삭제되게 합니다.
        builder.setAutoCancel(false);
        //pendingIntent를 builder에 설정 해줍니다.
        // 알림창 터치시 인텐트가 전달할 수 있도록 해줍니다.
        builder.setContentIntent(pendingIntent); Notification notification = builder.build();
        //알림창 실행
        manager.notify(1,notification);
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
    //잠금상태에서 실행할 스레드 클래스 생성
    class LockRunThread extends Thread{
        public void run() {
            while (!checkTh) {
                long crrTime = System.currentTimeMillis();
                String getTime = (String) DateFormat.format("k:mm:ss", crrTime);
                String time[] = getTime.split(":");
                for(int i=0;i<time.length;i++){
                    Log.d("timecheck", time[i]);
                }
                hour = Integer.parseInt(time[0]);
                minute = Integer.parseInt(time[1]);
                second = Integer.parseInt(time[2]);
                Log.i("Time", "hour" + hour + ", minute" + minute + ", second" + second);
                Message msg = handler.obtainMessage();
                handler.sendMessage(msg);
                if (!checkPermission()) continue;
                running = getPackageName(getApplicationContext());
                //해당 어플이 아닌 다른 어플이 실행되었을 경우 종료 - 현재 if문이 작동하지 않는다...
                if (!running.equals("")) {
                    Log.d("tmdguq_alert", running + "실행되고있습니다");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (android.provider.Settings.canDrawOverlays(context)) {
                            if (!running.equals(getClass().getPackage().getName())) {
                                Intent sIntent = new Intent(context, UI_2_Maintimertab.class);
                                sIntent.putExtra("action", "tts");
                                sIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                sIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(sIntent);
                                dlgcheck=true;
                            }
                            else{
                                dlgcheck=false;
                            }
                        }
                    }
                } else {

                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //스레드에 메시지 보내줄 핸들러 생성 - 스레드 상태에서 UI 바꾸기 위함
    class valueHandler extends Handler{
        public void handleMessage(@NonNull Message msg){
            super.handleMessage(msg);
            Log.d("sleepPicker", String.valueOf(sleep_timePicker.getHour())+ "  " + String.valueOf(sleep_timePicker.getMinute()));
            if (hour == sleep_timePicker.getHour() && minute == sleep_timePicker.getMinute()) {
                //최저 음량으로 설정
                mediaVol.setRingerMode(AudioManager.RINGER_MODE_SILENT); //음소거 하는 코드
                // 최저 밝기로 설정
                params.screenBrightness = (prefOb.getbrightvalue()/100.0f); //설정해둔 밝기로 화면 어둡게 함
//                params.screenBrightness = 0.1f; //설정해둔 밝기로 화면 어둡게 함
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                //화면 변환 관련
                timelayout.setVisibility(View.INVISIBLE);
                locklayout.setVisibility(View.VISIBLE);
                bottom_menu.setVisibility(View.INVISIBLE);
                belllayout.setVisibility(View.INVISIBLE);
                sleepTime_breakTime_View.setVisibility(View.INVISIBLE);
                if (sleep_timePicker.getHour() <= alarm_timePicker.getHour() && sleep_timePicker.getMinute() < alarm_timePicker.getMinute()) {
                    waketxt.setText((alarm_timePicker.getHour() - hour) + "시간 "
                            + (alarm_timePicker.getMinute() - minute) + "분 남았습니다");
                }
                Log.i("현재상태", "잠금상태");
                //Log.i("성공", "성공");
            } else if (hour == alarm_timePicker.getHour() && minute == alarm_timePicker.getMinute()) {
                //기존 음량으로 설정
                mediaVol.setRingerMode(AudioManager.RINGER_MODE_NORMAL); //음소거 푸는 코드
                // 기존 밝기로 설정
                params.screenBrightness = brightness;
                // 밝기 설정 적용
                getWindow().setAttributes(params);
                String nowbright = Float.toString(brightness);//바뀐 이후의 밝기 String 타입으로 저장
                Toast.makeText(UI_2_Maintimertab.this, "변경되는 밝기값은 "+nowbright+" 입니다.", Toast.LENGTH_SHORT).show();
                alarm_intent.putExtra("state", "alarm on");  // receiver에 string 값 넘겨주기

                pendingIntent = PendingIntent.getBroadcast(UI_2_Maintimertab.this, 0, alarm_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        pendingIntent); // 알람셋팅
                //화면 변환 관련
                timelayout.setVisibility(View.INVISIBLE);
                locklayout.setVisibility(View.INVISIBLE);
                bottom_menu.setVisibility(View.INVISIBLE);
                belllayout.setVisibility(View.VISIBLE);
                Log.i("현재상태", "기상상태");
            } else {
                Log.i("실패", "실패");
            }
        }
    }
}
