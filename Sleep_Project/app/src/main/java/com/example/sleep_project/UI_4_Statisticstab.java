package com.example.sleep_project;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;


public class UI_4_Statisticstab extends AppCompatActivity {

    int y=0, m=0, d=0, h=0, mi=0;

    BarChart barChart;
    TextView minuteTextview;

    Button main, music, statistics, account,menuOpen;
    Intent intent;
    MenuItem menuItem;
    prefvalue prefOb;
    //어떤 통계인지 텍스트뷰를 통해서 알려줌
    TextView statisticsTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics1);
//        prefOb = new prefvalue();
//        int tempBright = prefOb.getbrightvalue();
//        Log.d("prefbright",String.valueOf(tempBright));
        setContentView(R.layout.statistics1);

        barChart = (BarChart) findViewById(R.id.bar_chart);
        BarInit();

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDate();
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



        /////////////////////////////////////////////
        main = (Button) findViewById(R.id.main); //메인기능버튼
        music = (Button) findViewById(R.id.music); //음악기능 버튼
        statistics = (Button) findViewById(R.id.statistics); //통계기능 버튼
        account = (Button) findViewById(R.id.accounttab); //계정관리기능 버튼

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
        //계정관리 탭으로 이동
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void BarInit() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(0f, 44f));
        barEntries.add(new BarEntry(1f, 88f));
        barEntries.add(new BarEntry(2f, 41f));
        barEntries.add(new BarEntry(3f, 85f));
        barEntries.add(new BarEntry(4f, 96f));
        barEntries.add(new BarEntry(5f, 25f));
        barEntries.add(new BarEntry(6f, 10f));
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("Mars");
        theDates.add("Avril");
        theDates.add("Dec");
        theDates.add("May");
        theDates.add("OCt");
        theDates.add("Nov");
        theDates.add("Fir");
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(theDates));
        BarData theData = new BarData(barDataSet);//----Line of error
        barChart.setData(theData);
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
    }
    void showDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                y = year;
                m = month+1;
                d = dayOfMonth;

            }
        },2021, 6, 21);

        datePickerDialog.setMessage("메시지");
        datePickerDialog.show();
    }

}
