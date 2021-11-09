package com.example.Sleeper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class UI_1_1_MyAdapter extends FragmentStateAdapter {
    public int mCount; //4
    public UI_1_1_MyAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    //사용자 생성 메소드 - createFragment라는 이름의 함수
    public Fragment createFragment(int position) {
        int index = getRealPosition(position);
        if(index==0){
            return new UI_1_1_Fragement1();
        }
        else if(index==1){
            return new UI_1_1_Fragement2();
        }
        else if(index==2){
            return new UI_1_1_Fragement3();
        }
        else {
            return new UI_1_1_Fragement4();
        }
    }

    @Override
    public int getItemCount() { //이미지 슬라이드 할수 있는 횟수
        return 4;
    }

    public int getRealPosition(int position) {
        return position % mCount;
    }
}
