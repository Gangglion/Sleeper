//UI_1_4_UserInfo_Activity에서 입력한 유저정보를 파이어베이스에 올리는 클래스 + 수면시간 또한 올림
package com.example.sleep_project;

import android.text.format.DateFormat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class firebasepost {
    private String username;
    private String gender;
    private String age;
    private String job;
    private String sleeptime;
    private String waketime;
    private String howsleep;
    public String now;


    public String getGender() {
        return gender;
    }
    public String getAge() {
        return age;
    }
    public String getJob() {
        return job;
    }
    public String getUsername() {
        return username;
    }
    public String getSleeptime(){
        return sleeptime;
    }
    public String getWaketime(){
        return waketime;
    }
    public String getHowsleep(){
        return howsleep;
    }

    public firebasepost(){

    }
    public firebasepost(String username,String gender, String age, String job){
        this.username=username;
        this.gender = gender;
        this.age = age;
        this.job = job;
    }
    public firebasepost(String username,String sleeptime, String waketime){
        this.username = username;
        this.sleeptime = sleeptime;
        this.waketime = waketime;
        String s_t[] = sleeptime.split(":");
        int sleepH_value = Integer.parseInt(s_t[0]);
        int sleepM_value = Integer.parseInt(s_t[1]);
        String w_t[] = waketime.split(":");
        int wakeH_value = Integer.parseInt(w_t[0]);
        int wakeM_value = Integer.parseInt(w_t[1]);

        if(wakeH_value<sleepH_value) {
            wakeH_value += 24;
            howsleep = String.valueOf(wakeH_value - sleepH_value);
        }else{
            howsleep = String.valueOf(wakeH_value - sleepH_value);
        }
        howsleep+="시간";
        if(wakeM_value<sleepM_value){
            wakeM_value+=60;
            howsleep =howsleep+(wakeM_value - sleepM_value);
        }else{
            howsleep=howsleep+(wakeM_value - sleepM_value);
        }
        howsleep+="분";
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("gender", gender);
        result.put("age", age);
        result.put("job", job);
        return result;
    }
    public void postFirebaseDatabase(boolean add){
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            postValues = toMap();
        }
        childUpdates.put("/UserInfo/" + username + "/PersonalInfo/", postValues);
        mPostReference.updateChildren(childUpdates);
    }
    public Map<String, Object> toMaptime() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("예약취침시간", sleeptime);
        result.put("예약기상시간", waketime);
        result.put("취침시간", howsleep);
        return result;
    }
    public void postFirebaseDatabaseTime(boolean add){
        long nowt = System.currentTimeMillis();
        now = (String) DateFormat.format("yyyy-MM-dd", nowt);
        DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            postValues = toMaptime();
        }
        childUpdates.put("/UserInfo/" + username + "/" +now + "/", postValues);
        mPostReference.updateChildren(childUpdates);
    }
}
