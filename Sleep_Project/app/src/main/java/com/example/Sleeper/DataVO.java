package com.example.Sleeper;

import java.util.HashMap;

public class DataVO {
    private HashMap<String,String> hashMap;

    public DataVO(HashMap<String,String> hashMap){
        this.hashMap = hashMap;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }
}
