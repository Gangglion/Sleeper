package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

public class UI_1_4_UserInfo_Activity extends AppCompatActivity {

    private int age;
    private String job,sex;//입력한 값을 가져와 파이어베이스에 올리는 메소드의 인자로 들어갈것임
    Button nextbtn;
    RadioGroup sexselect;
    Spinner selectage,selectjob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_1_4_userinfo_layout);
        //나이 드롭박스 항목 세팅
        Spinner agespinner = (Spinner) findViewById(R.id.selectage);
        ArrayAdapter<CharSequence> ageadapter = ArrayAdapter.createFromResource(this,
                R.array.age_array, android.R.layout.simple_spinner_item);
        ageadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        agespinner.setAdapter(ageadapter);
        agespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //텍스트 색 흰색, 사이즈20으로 바꿔주기
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)adapterView.getChildAt(0)).setTextSize(20);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //직업 드롭박스 항목 세팅
        Spinner jobspinner = (Spinner) findViewById(R.id.selectjob);
        ArrayAdapter<CharSequence> jobadapter = ArrayAdapter.createFromResource(this,
                R.array.job_array, android.R.layout.simple_spinner_item);
        jobadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobspinner.setAdapter(jobadapter);
        jobspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //텍스트 색 흰색, 사이즈20으로 바꿔주기
                ((TextView)adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                ((TextView)adapterView.getChildAt(0)).setTextSize(20);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sexselect = (RadioGroup)findViewById(R.id.sexradiobtn);
        selectage = (Spinner)findViewById(R.id.selectage);
        selectjob = (Spinner)findViewById(R.id.selectjob);
        

        nextbtn = (Button)findViewById(R.id.userinfo_nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextintent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                startActivity(nextintent);
                finish();
            }
        });
    }
}