package com.example.sleep_project;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class UI_6_Alarm_Settings_activity extends AppCompatActivity {
//알람 셋팅 액티비티의 기능을 구현할 클래스
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_6_setting_layout);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        /* if (actionBar != null) */
        {
            actionBar.setDisplayHomeAsUpEnabled(true); // 상단 액션바 뒤로가기 설정
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.ui_arlam_setting, rootKey); // 알람 셋팅 액티비티의 ui를 가져옴
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Back button pressed.", Toast.LENGTH_SHORT).show();
    }
        }


