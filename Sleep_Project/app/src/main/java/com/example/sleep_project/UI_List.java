package com.example.sleep_project;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class UI_List extends AppCompatActivity {
    ListView sleepList,breakList;
    TextView sleepCnt,breakCnt;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_list);
        sleepCnt = (TextView)findViewById(R.id.sleepCnt);
        breakCnt =(TextView)findViewById(R.id.breakCnt);
        sleepList = (ListView)findViewById(R.id.sleepList);
        String[] sList = {"10:30","11:00","11:30"};
        ArrayAdapter<String>  sleepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sList);
        sleepList.setAdapter(sleepAdapter);
        sleepAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_maintimerlist_color,sList);
        sleepList.setAdapter(sleepAdapter);
        sleepList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sleepCnt.setText("취침 예약시간 : " + sList[position]);
            }
        });

        breakList = (ListView)findViewById(R.id.breakList);
        String[] bList = {"10:00","14:00"};
        ArrayAdapter<String> breakAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bList);
        breakList.setAdapter(breakAdapter);
        breakAdapter = new ArrayAdapter<String>(this,R.layout.ui_3_maintimerlist_color,bList);
        breakList.setAdapter(breakAdapter);

        breakList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                breakCnt.setText("기상 예약시간 :  " + bList[position]);

            }
        });
        



    }
}