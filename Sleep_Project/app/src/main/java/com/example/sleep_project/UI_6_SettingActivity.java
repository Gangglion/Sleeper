package com.example.sleep_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

import android.os.Bundle;

public class UI_6_SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_6_settings_layout);
        getSupportActionBar().setTitle("설정");
    }
}