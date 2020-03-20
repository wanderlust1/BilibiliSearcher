package com.wanderlust.bilibilisearcher.Fragments;

import com.wanderlust.bilibilisearcher.R;

/**
 * 电影 Fragment
 * @author Wanderlust 2020.1.22
 */
public class Fragment3 extends BaseFragment {

    @Override
    protected int getViewLayoutID() {
        return R.layout.fragment_movie;
    }

    @Override
    protected int getRecyclerViewID() {
        return R.id.recycler_movie;
    }

    @Override
    protected int getRefreshLayoutID() {
        return R.id.srl_movie;
    }

    @Override
    protected int getFloatingButtonID() {
        return R.id.fab_movie;
    }

    @Override
    protected String getType() {
        return "2";
    }

}