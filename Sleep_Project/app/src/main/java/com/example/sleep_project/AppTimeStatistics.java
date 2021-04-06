package com.example.sleep_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class AppTimeStatistics extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics1);
        setTitle("앱 사용시간");
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.sleepTime:
                intent = new Intent(AppTimeStatistics.this,UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sleepTimecomparison:
                intent = new Intent(AppTimeStatistics.this,SleepComparisonStatistics.class);
                startActivity(intent);
                finish();
                break;
        }

        return true;
    }


}

