package com.wanderlust.bilibilisearcher.Fragments;

import com.wanderlust.bilibilisearcher.R;

/**
 * 纪录片 Fragment
 * @author Wanderlust 2020.1.22
 */
public class Fragment5 extends BaseFragment {

    @Override
    protected int getViewLayoutID() {
        return R.layout.fragment_documentary;
    }

    @Override
    protected int getRecyclerViewID() {
        return R.id.recycler_documentary;
    }

    @Override
    protected int getRefreshLayoutID() {
        return R.id.srl_documentary;
    }

    @Override
    protected int getFloatingButtonID() {
        return R.id.fab_documentary;
    }

    @Override
    protected String getType() {
        return "3";
    }

}