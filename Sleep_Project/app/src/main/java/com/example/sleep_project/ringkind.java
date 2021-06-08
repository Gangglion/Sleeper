package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

public class ringkind extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =  1;
    ListView baseMusicList;
    private String songNames[];
    private ArrayList<Uri> songs;
    //MP3 경로를 가질 문자열 배열
    String[] resultPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ringkind_layout);
        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("mp3");
        String[] selectionArgsMp3 = new String[] {mimeType};
        Cursor cursor = null;
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            return;
        }
        cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME}, selectionMimeType, selectionArgsMp3, null);

        if(cursor.getCount() != 0) {
            ArrayList<Uri> arrayList = new ArrayList<Uri>();
            songNames = new String[cursor.getCount()];

            while(cursor.moveToNext()){
                Uri contentUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));

                arrayList.add(contentUri);

                songNames[cursor.getPosition()] = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3","");

                Log.d("uri : ", contentUri.toString());
                Log.d("position",songNames[cursor.getPosition()]);
            }
            songs = arrayList;
        }
        baseMusicList = (ListView)findViewById(R.id.baseMusicList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,songNames);
        baseMusicList.setAdapter(adapter);

    }
}