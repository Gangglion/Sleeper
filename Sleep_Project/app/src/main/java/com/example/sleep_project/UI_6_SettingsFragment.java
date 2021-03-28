package com.example.sleep_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Map;

public class UI_6_SettingsFragment extends PreferenceFragment {
    SharedPreferences prefs;
    ListPreference alertreference; //알림음 종류 설정
    //SettingSave settingsave = new SettingSave();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.ui_6_setting_preference);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String alert_value=prefs.getString("alert_list","");
        //Log.d("alert_value",alert_value);
    }
    public void onResume() {
        super.onResume();
        //설정값 변경리스너..등록
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }
    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(listener);
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
        }
    };

}