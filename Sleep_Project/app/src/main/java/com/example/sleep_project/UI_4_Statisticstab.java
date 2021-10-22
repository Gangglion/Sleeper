package com.example.sleep_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class UI_4_Statisticstab extends AppCompatActivity {

    int y=0, m=0, d=0, h=0, mi=0;

    //DB에서 통계 데이터 가져오기 위한 변수 선언
    String result; //asyncTask에서 가져온 String 저장하기 위한 변수
    String [][] arrayResult = new String[7][3]; //asyncTask 에서 가져온 String 쪼개서 배열에 맞게 매핑
    String sendmsg = "read_data";
    //AboutLogin aboutLogin = new AboutLogin();
    //String userId = aboutLogin.getUser().getEmail();
    ////////////////////////////////////////////////

    BarChart barChart;
    TextView minuteTextview,searchEndDay,searchStartDay;

    Button main, music, statistics, settings,menuOpen;
    Intent intent;
    MenuItem menuItem;
    prefvalue prefOb;
    int selYear,selMonth,selDay;
    Date   currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat yearFormay = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat monthFormay = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat dayFormay = new SimpleDateFormat("dd", Locale.getDefault());

    int curYear = Integer.parseInt(yearFormay.format(currentTime));
    int curMonth = Integer.parseInt(monthFormay.format(currentTime))-1;
    int curday = Integer.parseInt(dayFormay.format(currentTime));
    //어떤 통계인지 텍스트뷰를 통해서 알려줌
    TextView statisticsTitle;
    ArrayList<String> dateChart = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics1);
        barChart = (BarChart) findViewById(R.id.bar_chart);
        searchStartDay = (TextView)findViewById(R.id.searchStartDay);
        searchEndDay = (TextView)findViewById(R.id.searchEndDay);
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDate();
            }
        });
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDate();
            }
        });

        statisticsTitle = (TextView)findViewById(R.id.statisticsName);
        statisticsTitle.setText("수면시간 통계");
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //menuItem = (MenuItem)findViewById(R.id.sleepTime);
        //menuItem.setEnabled(false);
        //메뉴버튼
        menuOpen = (Button)findViewById(R.id.menuOpen);
        menuOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),v);
                getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.sleepTimecomparison) {
                            Intent intent = new Intent(getApplicationContext(), SleepComparisonStatistics.class);
                            startActivity(intent);
                            finish();
                        }else if(item.getItemId() == R.id.appUseTime) {
                            Intent intent = new Intent(getApplicationContext(), AppTimeStatistics.class);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        //DbTask 실행해서 통계 그래프 값 hashmap에 저장
        result ="";
        try {
            result = new DbTask(sendmsg).execute(sendmsg,"o0tmdguq0o@gmail.com").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //이자리에 String 쪼개서 배열에 저장하는 함수 호출
        SliceStr(result);
        //Log.d("result_Debug",result);

        /////////////////////////////////////////////
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        settings = (Button) findViewById(R.id.settingtab); //계정관리기능 버튼

        statistics.setBackgroundColor(Color.GRAY);
        //메인 탭으로 이동
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(intent);
                finish();
            }
        });
        //음악 탭으로 이동
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UI_3_Musictab.class);
                startActivity(intent);
                finish();
            }
        });
        //설정 탭으로 이동
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BarInit();
    }

    private void BarInit() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float[] totalTime = TotalSleepTime(arrayResult);
        //바 차트 막대 길이 지정하는 부분
        for(int i=0;i<7;i++)
        {
            barEntries.add(new BarEntry(i,totalTime[i]));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        ArrayList<String> theDates = new ArrayList<>();
        //바 차트 항목 이름 - 날짜 지정하는 부분
        for(int i=0;i<7;i++)
        {
            theDates.add(arrayResult[i][2]);
        }
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        BarData theData = new BarData(barDataSet);
        barChart.setData(theData);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.getXAxis().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); // X축 텍스트컬러설정
        barChart.getXAxis().setGridColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); // X축 줄의 컬러 설정
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);

        barChart.getAxisLeft().setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); //Y축 왼쪽 텍스트 컬러 설정
        barChart.getAxisLeft().setGridColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); // Y축 줄의 컬러 설정
        barChart.getAxisLeft().setAxisMinimum(0f);
        //barChart.getAxisLeft().setAxisMaximum(10f);

        YAxis yAxisRight = barChart.getAxisRight(); //Y축의 오른쪽면 설정
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawGridLines(false);
        //y축의 활성화를 제거함

        Legend legend = barChart.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);//하단 왼쪽에 설정
        legend.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white)); // 레전드 컬러 설정

        barChart.animateXY(0,800);
    }
    //달력띄우는 메소드 2가지
    void showStartDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;
                searchStartDay.setText(y+"/"+m+"/"+d);
            }
        },curYear,curMonth,curday);

        datePickerDialog.setMessage("메시지");
        datePickerDialog.show();
    }
    void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;
                searchEndDay.setText(y+"/"+m+"/"+d);
            }
        },curYear,curMonth,curday);
        datePickerDialog.setMessage("메시지");
        datePickerDialog.show();
    }

    //문자열 쪼개서 배열로 매핑하는 함수
    void SliceStr(String str)
    {
        String[] try1 = str.split("//");
        for(int i=0;i<arrayResult.length;i++)
        {
            String[] temp = try1[i].split(",");
            for(int j=0;j<arrayResult[i].length;j++)
            {
                arrayResult[i][j] = temp[j];
            }
        }
    }

    //arrayResult 의 시간값 2개 계산하여 잔시간 도출하는 함수
    float[] TotalSleepTime(String[][] arrayResult)
    {
        float[] arrayTotalTime = new float[7];
        for(int i=0;i<arrayResult.length;i++)
        {
            int stH = Integer.parseInt(arrayResult[i][0].substring(0,2));
            int atH = Integer.parseInt(arrayResult[i][1].substring(0,2));
            int stM = Integer.parseInt(arrayResult[i][0].substring(3,5));
            int atM = Integer.parseInt(arrayResult[i][1].substring(3,5));

            if(stH>atH && atH<12 && stH>12)
            {
                atH+=24;
            }
            if(atM<stM)
            {
                atM+=60;
                atH-=1;
            }
            arrayTotalTime[i]=(float)(atH-stH)+((float)(atM-stM)/100f);
        }
        return arrayTotalTime;
    }
}
