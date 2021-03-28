package com.example.sleep_project;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class SettingSave {
    private String lightvalue;
    private String alertvalue;
    private boolean question;

    public void setLightvalue(String lightvalue){
        this.lightvalue=lightvalue;
    }
    public String getLightvalue(){
        return this.lightvalue;
    }
    public void setAlertvalue(String alarmvalue){
        this.alertvalue=alarmvalue;
    }
    public String getAlertvalue(){
        return this.alertvalue;
    }
    public void setQuestion(boolean question){
        this.question=question;
    }
    public boolean getQuestion(){
        return question;
    }

    public SettingSave(){
        //TODO : 생성자 실행시 sharedPreference에 있는 값을 가져와 객체에 저장한다. 이후 처음 앱 실행시 설정된 값에 따라 바꿔줌
        Log.d("checkclass", alertvalue+"안뜨면 null");
    }
}
