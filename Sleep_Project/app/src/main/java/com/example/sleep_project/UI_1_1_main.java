package com.example.sleep_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import me.relex.circleindicator.CircleIndicator3;

public class UI_1_1_main extends AppCompatActivity {

    private ViewPager2 mPager;
    private FragmentStateAdapter pagerAdapter;
    private int num_page = 4;
    private CircleIndicator3 mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_1_1_main);
        //상단 액션바 숨기는 코드
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //ViewPager2
        mPager = findViewById(R.id.viewpager); // UI_1_main.xml의 viewpager2의 id 값 불러옴
        //Adapter
        pagerAdapter = new UI_1_1_MyAdapter(this, num_page); // FragementStateAdapter 타입의 객체 초기화
        mPager.setAdapter(pagerAdapter); //viewpager에 adapter 붙임
        //Indicator
        mIndicator = findViewById(R.id.indicator); //UI_1_main.xml의 indicator id값 불러옴
        mIndicator.setViewPager(mPager);
        mIndicator.createIndicators(num_page,0); //위에 지정한 num_page만큼 indicator 생성
        //ViewPager Setting
        mPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mPager.setOffscreenPageLimit(3);
        Button gotologinLBtn=findViewById(R.id.nextbtn); //다음버튼 id값 불러옴

        mPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (positionOffsetPixels == 0) {
                    mPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position%num_page);
                //Log.d("o0tmdguq0o",Integer.toString(position%num_page));
                //4번째 이미지에서 버튼이 활성화 되기 위함
                if(position!=3){
                    gotologinLBtn.setEnabled(false);
                }else{
                    gotologinLBtn.setEnabled(true);
                }
            }
        });
        //"다음"버튼 클릭시 로그인화면으로 이동시킴
        gotologinLBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //버튼 클릭시 권한설정화면으로 넘어가는 기능
                Intent permis=new Intent(v.getContext(), UI_1_2_permission_activity.class);
                startActivity(permis);
                finish();//다음 액티비티로 넘어가면 이 페이지는 종료함 - 따라서 뒤로가기 버튼 눌러도 이 화면으로 돌아오지 않음
            }
        });
    }
}