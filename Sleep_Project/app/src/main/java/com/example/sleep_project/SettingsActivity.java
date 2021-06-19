package com.example.sleep_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    public boolean checksetting;

    public static Context context_pref;
    public static SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if(pref==null){
            checksetting=false;
        }else{
            checksetting=true;
        }
        Log.d("checksetting", String.valueOf(checksetting));
        if(!checksetting){
            Intent intent  = new Intent(getApplicationContext(),UI_2_Maintimertab.class);
            startActivity(intent);
            checksetting=true;
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            pref = PreferenceManager.getDefaultSharedPreferences(getActivity()); //이 클래스에서 저장한 pref를 SharedPreference 객체 형태로 저장
            context_pref = this.getContext();
        }
    }
}