package com.wanderlust.bilibilisearcher.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.wanderlust.bilibilisearcher.Fragments.Fragment2;
import com.wanderlust.bilibilisearcher.Fragments.Fragment5;
import com.wanderlust.bilibilisearcher.Fragments.Fragment4;
import com.wanderlust.bilibilisearcher.Fragments.Fragment3;
import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.adapters.ViewPagerAdapter;
import com.wanderlust.bilibilisearcher.tools.Tool;

import java.util.ArrayList;
import java.util.List;

import com.wanderlust.bilibilisearcher.Fragments.Fragment1;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.main_toolbar) Toolbar     mToolbar;
    @BindView(R.id.main_tab)     TabLayout   mTabLayout;
    @BindView(R.id.main_vp)      ViewPager   mViewPager;
    @BindView(R.id.main_search)  ImageButton mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tool.setSemiStatusBarDisable(getWindow());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTabLayout = findViewById(R.id.main_tab);
        mToolbar = findViewById(R.id.main_toolbar);
        mViewPager = findViewById(R.id.main_vp);
        mSearchButton.setOnClickListener(this);
        setSupportActionBar(mToolbar);
        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());
        fragments.add(new Fragment5());
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_search: {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        }
    }

}
