package com.wanderlust.bilibilisearcher.tools.interfaces;

/**
 * 监听item的点击
 * @see com.wanderlust.bilibilisearcher.Fragments.BaseFragment
 * @author Wanderlust 2020.1.31
 */
public interface ItemClickListener {

    /**
     * @param id 当前点击的item的video类的media_id
     */
    void onItemClick(String id, String url);

}
