package com.example.Sleeper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UI_List extends AppCompatActivity {
    ListView sleepList,breakList;
    TextView sleepCnt,breakCnt;
    Button test;
    int sleepHour,sleepMin,breakHour,breakMin;
    String[] array,array2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_list);
        sleepCnt = (TextView)findViewById(R.id.sleepCnt);
        breakCnt =(TextView)findViewById(R.id.breakCnt);
        sleepList = (ListView)findViewById(R.id.sleepList);
        test = (Button)findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                intent.putExtra("SleepHour",sleepHour);
                intent.putExtra("BreakHour",breakHour);
                intent.putExtra("SleepMin",sleepMin);
                intent.putExtra("BreakMin",breakMin);
                startActivity(intent);
            }
        });
        String[] sList = {"22:00","23:00","24:00"};

        ArrayAdapter<String>  sleepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sList);
        sleepList.setAdapter(sleepAdapter);
        sleepAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_maintimerlist_color,sList);
        sleepList.setAdapter(sleepAdapter);
        sleepList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sleepCnt.setText("취침 예약시간 : " + sList[position]);
                array = sList[position].split(":");
                sleepHour = Integer.parseInt(array[0]);
                sleepMin = Integer.parseInt(array[1]);
            }
        });

        breakList = (ListView)findViewById(R.id.breakList);
        String[] bList = {"06:00","07:00"};
        ArrayAdapter<String> breakAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bList);
        breakList.setAdapter(breakAdapter);
        breakAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_maintimerlist_color,bList);
        breakList.setAdapter(breakAdapter);

        breakList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                breakCnt.setText("기상 예약시간 :  " + bList[position]);
                array2 = bList[position].split(":");
                breakHour = Integer.parseInt(array2[0]);
                breakMin = Integer.parseInt(array2[1]);

            }
        });
        



    }
}