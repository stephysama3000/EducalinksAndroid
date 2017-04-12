package com.example.robert.pruebavolley.Clases;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return (position == 0)? "Tab 1" : "Tab2" ;
    }
    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public Fragment getItem(int position) {
        return null;//(position == 0)? new MensajesEntFragment() : new MensajesElim() ;
    }
}