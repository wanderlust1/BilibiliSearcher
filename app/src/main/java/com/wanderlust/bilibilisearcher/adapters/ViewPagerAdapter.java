package com.wanderlust.bilibilisearcher.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public static final String[] tabTitles = {"番剧", "国创", "电影", "电视剧", "纪录片"};

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    //空方法体，防止滑动ViewPager时销毁Fragment
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {}
}