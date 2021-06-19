package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UI_1_4_UserInfo_Activity extends AppCompatActivity {

    private String username,sex,age,job;//입력한 값을 가져와 파이어베이스에 올리는 메소드의 인자로 들어갈것임
    Button nextbtn,seeservicebtn,seepersonalbtn;
    RadioGroup sexselect;
    Spinner selectage,selectjob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_1_4_userinfo_layout);
        //TODO : 파이어베이스 내에 계정이름에 해당하는 정보가 들어가있을경우 해당 액티비티 스킵
//        if(/*파이어베이스 값 있는지*/){
//            Intent skipintent = new Intent(getApplicationContext(),UI_2_Maintimertab.class);
//            startActivity(skipintent);
//            finish();
//        }

        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d("datacheck", "ValueEventListener : " + snapshot.getKey());
//                    Log.d("datacheck", "ValueEventListener : " + snapshot);
//                    Log.d("datacheck", "ValueEventListener : " + snapshot.getChildrenCount());
//                    Log.d("datacheck", "ValueEventListener : " + snapshot.getChildren());
//                    Log.d("datacheck", "ValueEventListener : " + snapshot.getValue());
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        Log.d("datacheck", "ValueEventListener : " + snapshot1.getValue());
                        HashMap <String,String> map = (HashMap<String, String>) snapshot1.getValue(); //hashmap은 JSONArray로 바꿀수없다 -> snapshot1.getvalue() 는 hashmap이다??
                        Log.d("checkhash",map.toString());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        //다음 버튼 눌렀을때 실행되는 내용 - 파이어베이스에 값 저장, 성공시 다음화면으로 이동
        nextbtn = (Button)findViewById(R.id.userinfo_nextbtn);
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //라디오버튼에 눌린 값 저장
                    int id = sexselect.getCheckedRadioButtonId();
                    RadioButton selecttxtsex = (RadioButton)findViewById(id);
                    sex = selecttxtsex.getText().toString();
                    //나이 저장
                    Spinner agespinner = (Spinner)findViewById(R.id.selectage);
                    age = agespinner.getSelectedItem().toString();
                    //직업 저장
                    Spinner jobspinner = (Spinner)findViewById(R.id.selectjob);
                    job = jobspinner.getSelectedItem().toString();
/*                    Log.d("checkdata",sex);
                    Log.d("checkdata",age);
                    Log.d("checkdata",job);*/

                    //데이터베이스에 올라갈 유저이름 가져옴
                    AboutLogin aboutLogin = new AboutLogin();
                    username = aboutLogin.getUser().getDisplayName();

                    firebasepost firebasepost = new firebasepost(username,sex,age,job);
                    firebasepost.postFirebaseDatabase(true);

                    Intent nextintent = new Intent(getApplicationContext(), UI_2_Maintimertab.class);
                    startActivity(nextintent);
                    finish();
                }catch(Exception e){
                    Log.d("firebaseError",e.toString());
                    Toast.makeText(getApplicationContext(),"정보 입력 에러. 다시 확인해주십시오",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //서비스 이용약관 보기
        seeservicebtn = (Button)findViewById(R.id.seeservice);
        seeservicebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent seeserviceintent = new Intent(getApplicationContext(), seeservicetab.class);
                startActivity(seeserviceintent);
            }
        });
        //개인정보 방침 보기
        seepersonalbtn = (Button)findViewById(R.id.seepersonal);
        seepersonalbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seepersonalintent = new Intent(getApplicationContext(), personaltab.class);
                startActivity(seepersonalintent);
            }
        });
    }
}