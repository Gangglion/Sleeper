package com.example.Sleeper;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UI_4_StatisticsTab_Bar extends AppCompatActivity {

    //DB에서 통계 데이터 가져오기 위한 변수 선언
    String result; //asyncTask에서 가져온 String 저장하기 위한 변수
    String [][] arrayResult = new String[7][3]; //asyncTask 에서 가져온 String 쪼개서 배열에 맞게 매핑
    String sendmsg = "read_data";
    float[] arrayTotalTime = new float[7];
    int[] totalH = new int[7];
    int[] totalM = new int[7];
    //하단 표 관련 텍스트뷰
    TextView date1,date2,date3,date4,date5,date6,date7,totaltime1,totaltime2,totaltime3,totaltime4,totaltime5,totaltime6,totaltime7,
            settime1,settime2,settime3,settime4,settime5,settime6,settime7;
    float[] totalTime;
    //AboutLogin aboutLogin = new AboutLogin();
    //String userId = aboutLogin.getUser().getEmail();
    ////////////////////////////////////////////////

    BarChart barChart;
    TextView searchEndDay,searchStartDay;

    Button main, music, statistics, settings,menuOpen,startDateBtn,endDateBtn,searchBtn;
    Date   currentTime = Calendar.getInstance().getTime();
    SimpleDateFormat yearFormay = new SimpleDateFormat("yyyy", Locale.getDefault());
    SimpleDateFormat monthFormay = new SimpleDateFormat("MM", Locale.getDefault());
    SimpleDateFormat dayFormay = new SimpleDateFormat("dd", Locale.getDefault());

    //달력 시작날짜
    int startCurYear = Integer.parseInt(yearFormay.format(currentTime));
    int startCurMonth = Integer.parseInt(monthFormay.format(currentTime));
    int startCurDay = Integer.parseInt(dayFormay.format(currentTime));
    String startCal = "";
    //달력 종료날짜
    int endCurYear = Integer.parseInt(yearFormay.format(currentTime));
    int endCurMonth = Integer.parseInt(monthFormay.format(currentTime));
    int endCurDay = Integer.parseInt(dayFormay.format(currentTime));
    String endCal = "";
    //어떤 통계인지 텍스트뷰를 통해서 알려줌
    TextView statisticsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_bar);

        barChart = (BarChart) findViewById(R.id.bar_chart);
        searchStartDay = (TextView)findViewById(R.id.searchStartDay);
        searchEndDay = (TextView)findViewById(R.id.searchEndDay);
        startDateBtn = findViewById(R.id.startDateBtn);
        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStartDate();
            }
        });
        endDateBtn = findViewById(R.id.endDateBtn);
        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEndDate();
            }
        });
        searchBtn = findViewById(R.id.searchBtn);

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
                        if(item.getItemId() == R.id.SleepTime_Pie) {
                            Intent intent = new Intent(getApplicationContext(), UI_4_StatisticsTab_Pie.class);
                            startActivity(intent);
                            finish();
                        }else if(item.getItemId() == R.id.SleepTime_Bar) {
                            Intent intent = new Intent(getApplicationContext(), UI_4_StatisticsTab_Bar.class);
                            startActivity(intent);
                            finish();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        //DbTask 실행해서 통계 그래프 값
        result ="";
        try
        {
            result = new DbTask(sendmsg).execute(sendmsg,"o0tmdguq0o@gmail.com","","").get();
            //이자리에 String 쪼개서 배열에 저장하는 함수 호출
            SliceStr(result);
            totalTime = TotalSleepTime(arrayResult);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    result = new DbTask(sendmsg).execute(sendmsg,"o0tmdguq0o@gmail.com",startCal,endCal).get();
                    //이자리에 String 쪼개서 배열에 저장하는 함수 호출
                    SliceStr(result);
                    totalTime = TotalSleepTime(arrayResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BarInit();
                TableInput();
            }
        });
        BarInit();
        TableInput();
    }

    private void BarInit() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
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
            theDates.add(SliceDate(arrayResult[i][2]));
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
                int y=0, m=0, d=0;
                y = year;
                m = month+1;
                d = dayOfMonth;
                searchStartDay.setText(y+"/"+m+"/"+d);
                //Log.d("calendertest",startCurYear+"\t"+startCurMonth+"\t"+startCurDay+" <<<이부분이 달력화면에서 확인 눌렀을때 뜨면 값이 들어가있는 상태");
                startCal = Integer.toString(y)+"-"+Integer.toString(m)+"-"+Integer.toString(d);
            }
        }, startCurYear, startCurMonth -1, startCurDay);
        datePickerDialog.setMessage("메시지");
        datePickerDialog.show();
    }
    void showEndDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int y=0, m=0, d=0;
                y = year;
                m = month+1;
                d = dayOfMonth;
                searchEndDay.setText(y+"/"+m+"/"+d);
                //Log.d("calendertest",endCurYear+"\t"+endCurMonth+"\t"+endCurDay+" <<<이부분이 달력화면에서 확인 눌렀을때 뜨면 값이 들어가있는 상태");
                endCal = Integer.toString(y)+"-"+Integer.toString(m)+"-"+Integer.toString(d);
            }
        },  endCurYear, endCurMonth-1, endCurDay);
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
        for(int i=0;i< arrayResult.length;i++)
        {
            Log.d("resultDebug",arrayResult[i][0]);
            Log.d("resultDebug",arrayResult[i][1]);
            Log.d("resultDebug",arrayResult[i][2]);
        }
    }
    //문자열 쪼개서 배열로 매핑하는 함수
    String SliceDate(String str)
    {
        String sliceDate="";
        String[] try1 = str.split("-");
        sliceDate = try1[1]+"/"+try1[2];
        return sliceDate;
    }

    //arrayResult 의 시간값 2개 계산하여 잔시간 도출하는 함수
    float[] TotalSleepTime(String[][] arrayResult)
    {
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
            totalH[i] = atH-stH;
            totalM[i] = atM-stM;
            arrayTotalTime[i]=(float)(atH-stH)+((float)(atM-stM)/100f);
        }
        return arrayTotalTime;
    }

    //하단 표 텍스트 넣는 메소드
    void TableInput()
    {
        date1 = (TextView) findViewById(R.id.date1);
        date1.setText(arrayResult[0][2]);
        date2 = (TextView) findViewById(R.id.date2);
        date2.setText(arrayResult[1][2]);
        date3 = (TextView) findViewById(R.id.date3);
        date3.setText(arrayResult[2][2]);
        date4 = (TextView) findViewById(R.id.date4);
        date4.setText(arrayResult[3][2]);
        date5 = (TextView) findViewById(R.id.date5);
        date5.setText(arrayResult[4][2]);
        date6 = (TextView) findViewById(R.id.date6);
        date6.setText(arrayResult[5][2]);
        date7 = (TextView) findViewById(R.id.date7);
        date7.setText(arrayResult[6][2]);

        totaltime1 = (TextView) findViewById(R.id.totaltime1);
        totaltime1.setText(totalH[0] + " : "+totalM[0]);
        totaltime2 = (TextView) findViewById(R.id.totaltime2);
        totaltime2.setText(totalH[1] + " : "+totalM[1]);
        totaltime3 = (TextView) findViewById(R.id.totaltime3);
        totaltime3.setText(totalH[2] + " : "+totalM[2]);
        totaltime4 = (TextView) findViewById(R.id.totaltime4);
        totaltime4.setText(totalH[3] + " : "+totalM[3]);
        totaltime5 = (TextView) findViewById(R.id.totaltime5);
        totaltime5.setText(totalH[4] + " : "+totalM[4]);
        totaltime6 = (TextView) findViewById(R.id.totaltime6);
        totaltime6.setText(totalH[5] + " : "+totalM[5]);
        totaltime7 = (TextView) findViewById(R.id.totaltime7);
        totaltime7.setText(totalH[6] + " : "+totalM[6]);

        settime1 = (TextView) findViewById(R.id.settime1);
        settime1.setText(arrayResult[0][0]+"~"+arrayResult[0][1]);
        settime2 = (TextView) findViewById(R.id.settime2);
        settime2.setText(arrayResult[1][0]+"~"+arrayResult[1][1]);
        settime3 = (TextView) findViewById(R.id.settime3);
        settime3.setText(arrayResult[2][0]+"~"+arrayResult[2][1]);
        settime4 = (TextView) findViewById(R.id.settime4);
        settime4.setText(arrayResult[3][0]+"~"+arrayResult[3][1]);
        settime5 = (TextView) findViewById(R.id.settime5);
        settime5.setText(arrayResult[4][0]+"~"+arrayResult[4][1]);
        settime6 = (TextView) findViewById(R.id.settime6);
        settime6.setText(arrayResult[5][0]+"~"+arrayResult[5][1]);
        settime7 = (TextView) findViewById(R.id.settime7);
        settime7.setText(arrayResult[6][0]+"~"+arrayResult[6][1]);
    }
}
