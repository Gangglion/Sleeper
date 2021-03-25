package com.example.sleep_project;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Switch;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import java.util.Map;

public class UI_6_SettingsFragment extends PreferenceFragment {
    SharedPreferences prefs;
    ListPreference soundPreference; //알림음 종류 설정

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.ui_6_setting_preference, rootKey);
        soundPreference = (ListPreference) findPreference(("alert_list"));

        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.d("tmdguq", prefs.getAll().toString()); //설정을 변경하면 자동으로 sharedpreference에 "xml에지정한키 : 해당값" 꼴로 저장됨
                if(prefs.getBoolean("morningQ",true)==true){
                    //보류
                }
            }
        });
    }
    public String getSharedP(){
        return prefs.getAll().toString();
    }
}