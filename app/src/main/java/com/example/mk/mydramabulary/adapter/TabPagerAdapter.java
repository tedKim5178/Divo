package com.example.mk.mydramabulary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.mk.mydramabulary.activities.MainActivity;
import com.example.mk.mydramabulary.fragments.DramaFragment;
import com.example.mk.mydramabulary.fragments.VocaFragment;

/**
 * Created by mk on 2017-02-10.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter{
    private int tabCount;
    private static final String TAG = MainActivity.class.getSimpleName();
    DramaFragment dramaFragment;
    VocaFragment vocaFragment;
    public TabPagerAdapter(FragmentManager fm, int tabCount){
        super(fm);
        this.tabCount = tabCount;
    }



    @Override
    public Fragment getItem(int position) {



        switch (position) {
            case 0:
                if( vocaFragment == null){
                    Log.d(TAG,"Fragment voca");
                    vocaFragment = new VocaFragment();
                    return vocaFragment;
                }else{
                    return vocaFragment;
                }
            case 1:
                // 여기서 전달해줘야한다..!
                if( dramaFragment == null){
                    Log.d(TAG,"Fragment voca");
                    dramaFragment = new DramaFragment();
                    return dramaFragment;
                }else{
                    return dramaFragment;
                }

            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
