package com.example.sleep_project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;

public class UI_6_SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_6_settings_layout);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings, new UI_6_Alert_Settings_activity.SettingsFragment()).commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("설정");  //액션바 제목설정
            //TODO : 액션바 뒤로가기
        }
    }

    public static class UI_6_SettingsFragment extends PreferenceFragmentCompat {
        SharedPreferences prefs;
        ListPreference alertreference; //알림음 종류 설정
        //SettingSave settingsave = new SettingSave();

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.ui_6_setting_preference,rootKey);
            /*prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            alertreference = (ListPreference)findPreference("alert_list");
            if(!prefs.getString("alert_list","").equals("")){
                alertreference.setSummary(prefs.getString("alert_list",""));
            }*/
        }
/*
        public void onResume() {
            super.onResume();
            //설정값 변경리스너..등록
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }
        @Override
        public void onPause() {
            super.onPause();
            prefs.registerOnSharedPreferenceChangeListener(listener);
        }

        SharedPreferences.OnSharedPreferenceChangeListener listener= new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.equals("morningQ")){
                    boolean Q_value= prefs.getBoolean("morningQ", false);
                    //Toast.makeText(getActivity(), "문제유무 : "+ Q_value, Toast.LENGTH_SHORT).show();
                }else if(key.equals("alert_list")){
                    String alert_value=prefs.getString("alert_list","소리");
                    //TODO : 선택한 값에따라 Noti 방식 설정 - 다른 클래스에서 이 값들을 불러오거나, 타 클래스에 저장된 값을 불러오는 작업이 필요
                    //Toast.makeText(getActivity(), "알림방법 설정 : "+ alert_value, Toast.LENGTH_SHORT).show();
                }
                alertreference.setSummary(prefs.getString("alert_list",""));
            }
        };
*/
    }
}
