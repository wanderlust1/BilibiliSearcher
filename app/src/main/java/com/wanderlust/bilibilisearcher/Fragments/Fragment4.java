package com.wanderlust.bilibilisearcher.Fragments;

import com.wanderlust.bilibilisearcher.R;

/**
 * 电视剧 Fragment
 * @author Wanderlust 2020.1.22
 */
public class Fragment4 extends BaseFragment {

    @Override
    protected int getViewLayoutID() {
        return R.layout.fragment_drama;
    }

    @Override
    protected int getRecyclerViewID() {
        return R.id.recycler_drama;
    }

    @Override
    protected int getRefreshLayoutID() {
        return R.id.srl_drama;
    }

    @Override
    protected int getFloatingButtonID() {
        return R.id.fab_drama;
    }

    @Override
    protected String getType() {
        return "5";
    }

}