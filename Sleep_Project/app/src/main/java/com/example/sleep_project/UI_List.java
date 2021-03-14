package com.example.sleep_project;

import android.os.Bundle;
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
        String[] sList = {"새노래1", "새노래2", "새노래3", "새노래4", "새노래5", "새노래6",
                "새노래7", "새노래8", "새노래9", "새노래10", "새노래11"
                , "새노래12", "새노래13", "새노래14", "새노래15", "새노래16", "새노래17", "새노래18"};
        ArrayAdapter<String>  sleepAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sList);
        sleepList.setAdapter(sleepAdapter);


        breakList = (ListView)findViewById(R.id.breakList);
        String[] bList = {"10:00","14:00"};
        ArrayAdapter<String> breakAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bList);
        breakList.setAdapter(breakAdapter);
        breakCnt.setText("기상 예약시간 개수는 " + bList.length +"개 입니다.");
        sleepCnt.setText("취침 예약 시간 개수는 " + sList.length +"개 입니다.");
    }
}