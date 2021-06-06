//UI_1_4_UserInfo_Activity에서 입력한 유저정보를 파이어베이스에 올리는 클래스
package com.example.sleep_project;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class firebasepost {
    private String username;
    private String gender;
    private String age;
    private String job;

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

    public firebasepost(){

    }
    public firebasepost(String username,String gender, String age, String job){
        this.username=username;
        this.gender = gender;
        this.age = age;
        this.job = job;
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
}
