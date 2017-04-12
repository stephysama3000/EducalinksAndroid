package com.example.robert.pruebavolley.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.robert.pruebavolley.R;

/**
 * A simple {@link Fragment} subclass.
 */

public class PagosFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private AdapterPager adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        pager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new AdapterPager(getChildFragmentManager());
        pager.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pagos, container, false);

        //tabs.setShouldExpand(true);
        return rootView;

    }


    public class AdapterPager extends FragmentPagerAdapter {

        private final String[] TITLES = { "Deudas Pendientes", "Deudas pagadas" };

        public AdapterPager(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return DeudasPendientesFragment.newInstance(position);
                case 1:
                    return DeudasPagadasFragment.newInstance(position);
                case 2:
                default:
                    return null;
            }

        }

        public void refresh() {
            adapter.notifyDataSetChanged();
        }

    }


}
