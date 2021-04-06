package com.example.sleep_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class SleepComparisonStatistics extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics1);
        setTitle("수면시간비교");

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.appUseTime:
                intent = new Intent(SleepComparisonStatistics.this,AppTimeStatistics.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sleepTime:

                intent = new Intent(SleepComparisonStatistics.this,UI_4_Statisticstab.class);
                startActivity(intent);
                finish();
                break;

        }

        return true;
    }
}

