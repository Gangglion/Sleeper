//알람문제 랜덤생성 클래스
package com.example.sleep_project;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmQuestion {
    int firstNum,secondNum,thirdNum,answer;
    String ques,weekdayYo;
    Date weekday;
    SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.getDefault());
    public AlarmQuestion(){
        firstNum=(int)(Math.random() * 100)+1;
        secondNum = (int)(Math.random() * 100)+1;
        thirdNum = (int)(Math.random() * 10) + 1;
        //요일 나타내기
        weekday = Calendar.getInstance().getTime();
        weekdayYo = weekdayFormat.format(weekday);
    }
    public String setQuestion(String weekdayYo){
        this.weekdayYo = weekdayYo;
        switch(weekdayYo){
            case"Mon":
                ques=(firstNum +" + " + secondNum + " x " + thirdNum);
                break;
            case"Tue":
                ques=(firstNum +" + " + secondNum + " - " + thirdNum);
                break;
            case"Wed":
                ques=(thirdNum +" x " + secondNum + " - " + firstNum);
                break;
            case"Thu":
                ques=(firstNum +" + " + secondNum + " + " + thirdNum);
                break;
            case"Fri":
                ques=( secondNum + " x " + thirdNum);
                break;
            case"Sat":
                ques=(firstNum +" - " + secondNum );
                break;
            case"Sun":
                ques=(firstNum +" + " + thirdNum);
                break;
        }
        return ques;
    }
    public int getAnswer(){
        switch(weekdayYo){
            case"Mon":
                answer = firstNum + secondNum * thirdNum;
                break;
            case"Tue":
                answer=firstNum + secondNum - thirdNum;
                break;
            case"Wed":
                answer=firstNum * secondNum - thirdNum;
                break;
            case"Thu":
                answer=firstNum + secondNum + thirdNum;
                break;
            case"Fri":
                answer= secondNum * thirdNum;
                break;
            case"Sat":
                answer=firstNum - secondNum;
                break;
            case"Sun":
                answer=firstNum + thirdNum;
                break;
        }
        return answer;
    }
}
