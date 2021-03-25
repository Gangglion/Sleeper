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
    public String getQuestion(){
        switch(weekdayYo){
            case"월":
                ques=(firstNum +" + " + secondNum + " x " + thirdNum);
                break;
            case"화":
                ques=(firstNum +" + " + secondNum + " - " + thirdNum);
                break;
            case"수":
                ques=(thirdNum +" x " + secondNum + " - " + firstNum);
                break;
            case"목":
                ques=(firstNum +" + " + secondNum + " + " + thirdNum);
                break;
            case"금":
                ques=( secondNum + " x " + thirdNum);
                break;
            case"토":
                ques=(firstNum +" - " + secondNum );
                break;
            case"일":
                ques=(firstNum +" + " + thirdNum);
                break;
        }
        return ques;
    }
    public int getAnswer(){
        switch(weekdayYo){
            case"월":
                answer = firstNum + secondNum * thirdNum;
                break;
            case"화":
                answer=firstNum + secondNum - thirdNum;
                break;
            case"수":
                answer=firstNum * secondNum - thirdNum;
                break;
            case"목":
                answer=firstNum + secondNum + thirdNum;
                break;
            case"금":
                answer= secondNum * thirdNum;
                break;
            case"토":
                answer=firstNum - secondNum;
                break;
            case"일":
                answer=firstNum + thirdNum;
                break;
        }
        return answer;
    }
}
