package com.example.Sleeper;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class Firebaseget {
    FirebaseDatabase database;
    DatabaseReference databaseRef;
    HashMap<String,String> hashMap;
    String firekey;
    DataVO dataVO;
    public Firebaseget(){
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();
        hashMap = new HashMap<>();
    }
    //설정한 정보 가져오는 파이어베이스
    public void Personaldataget() throws InterruptedException {
        //파이어베이스 값 가져오는 방법****
        CountDownLatch latch = new CountDownLatch(1);
        databaseRef.addChildEventListener(new ChildEventListener() {  // message는 child의 이벤트를 수신합니다.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    for(DataSnapshot postSnapshot1 : postSnapshot.getChildren()){
                        if(postSnapshot1.getKey().equals("PersonalInfo")){
                            firekey = postSnapshot1.getKey();
                            hashMap.put(postSnapshot1.getKey(),String.valueOf(postSnapshot1.getValue()));
                            Log.d("classfiretest",hashMap.get(firekey));
                            latch.countDown();
                        }else{
                            hashMap.put("PersonalInfo","null"); //PersonalInfo가 없다면 PersonalInfo 라는 키 값에 null이라는 문자열넣어줌
                        }
                    }
                }
                if(hashMap.isEmpty()){ //함수내부에선 비어있지않음
                    Log.d("classfiretest"," void함수내부 hashmap 비어있음");
                }else{
                    Log.d("classfiretest","void함수내부 hashmap 비어있지않음");
                    dataVO = new DataVO(hashMap);
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
        latch.await();
        if(dataVO.getHashMap().isEmpty()){ //함수내부에선 비어있지않음
            Log.d("classfiretest"," 함수내부 hashmap 비어있음");
        }else{
            Log.d("classfiretest","함수내부 hashmap 비어있지않음");
        }
    }
    //날짜 정보 가져오는 파이어베이스
    public void getDatedata(){

    }
}
