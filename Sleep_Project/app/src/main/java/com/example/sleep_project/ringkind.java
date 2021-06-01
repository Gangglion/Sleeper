package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;

public class ringkind extends AppCompatActivity {
    ListView baseMusicList;
    ArrayList<String> mp3List;
    String mp3Path = Environment.getExternalStorageDirectory().getPath()+"/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringkind_layout);
        ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        mp3List = new ArrayList<String>();
        File[] listFiles = new File(mp3Path).listFiles();
        String fileName, extName;
        for(File file : listFiles) {
            fileName = file.getName();
            extName = fileName.substring(fileName.length() - 3) ;
            if(extName.equals((String) "ogg")) {
                mp3List.add(fileName);
            }
        }
        baseMusicList = (ListView)findViewById(R.id.baseMusicList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mp3List);
        baseMusicList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        baseMusicList.setAdapter(adapter);
        baseMusicList.setItemChecked(0, true);
    }
}