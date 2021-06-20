package com.example.sleep_project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.common.util.MapUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Firebaseget {
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    static HashMap<String,String> hashMap;
    public Firebaseget(){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        hashMap = new HashMap<>();
    }
    //설정한 정보 가져오는 파이어베이스
    public void Personaldataget(){
        //파이어베이스 값 가져오는 방법****
        databaseRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot postSnapshot1 : postSnapshot.getChildren()){
                        if(postSnapshot1.getKey().equals("PersonalInfo")){
                            hashMap.put(postSnapshot1.getKey(),String.valueOf(postSnapshot1.getValue()));
                            Log.d("classfiretest",hashMap.get("PersonalInfo"));
                        }else{
                            hashMap.put("PersonalInfo","null"); //PersonalInfo가 없다면 PersonalInfo 라는 키 값에 null이라는 문자열넣어줌
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        if(hashMap.get("PersonalInfo").equals("null")){ //왜 위에서 저장했는데 널인것이지..
//            Log.d("emptytest","비어있음");
//        }else{
//            Log.d("emptytest","비어있지않음");
//        }
//        Log.d("classfiretest",hashMap.get("PersonalInfo"));
    }
    //날짜 정보 가져오는 파이어베이스
    public void getDatedata(){

    }
}
