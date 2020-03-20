package com.wanderlust.bilibilisearcher.tools.interfaces;

/**
 * 监听viewType更改的监听器
 * @see com.wanderlust.bilibilisearcher.Fragments.BaseFragment
 * @author Wanderlust 2020.1.27
 */
public interface TypeChangeListener {

    /**
     * 当viewType更改时调用
     * @param isNormal 判断当前viewType是否为正常显示的Grid列表布局
     */
    void onTypeChange(boolean isNormal);

}
