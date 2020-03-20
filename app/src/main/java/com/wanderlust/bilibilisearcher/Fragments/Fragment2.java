package com.wanderlust.bilibilisearcher.Fragments;

import com.wanderlust.bilibilisearcher.R;

/**
 * 国创番剧 Fragment
 * @author Wanderlust 2020.1.22
 */
public class Fragment2 extends BaseFragment {

    @Override
    protected int getViewLayoutID() {
        return R.layout.fragment_cn_season;
    }

    @Override
    protected int getRecyclerViewID() {
        return R.id.recycler_cn_season;
    }

    @Override
    protected int getRefreshLayoutID() {
        return R.id.srl_cn_season;
    }

    @Override
    protected int getFloatingButtonID() {
        return R.id.fab_cn_season;
    }

    @Override
    protected String getType() {
        return "4";
    }

}