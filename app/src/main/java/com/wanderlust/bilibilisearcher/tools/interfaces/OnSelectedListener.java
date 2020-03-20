package com.wanderlust.bilibilisearcher.tools.interfaces;

/**
 * 监听选择器布局的选择是否更改的监听器，监听者为
 * @see com.wanderlust.bilibilisearcher.adapters.FilterAdapter
 * @author Wanderlust 2020.1.27
 */
public interface OnSelectedListener {

    /**
     * 当选择的tag更改时调用
     * @param index 更改后的tag的位置
     */
    void onSelect(int index);

}
