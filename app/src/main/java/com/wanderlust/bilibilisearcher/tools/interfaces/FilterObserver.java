package com.wanderlust.bilibilisearcher.tools.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 监听选择器布局的选择更改的监听器，监听者为
 * @see com.wanderlust.bilibilisearcher.Fragments.BaseFragment
 * @author Wanderlust 2020.1.27
 */
public interface FilterObserver {

    /**
     * 当选择的tag更改时调用
     * @param params 更改后的搜索参数，用于生成URL，进行网络请求
     * @param selected 更改后的搜索参数的中文名称列表，用于显示在列表头项
     */
    void onFilterChanged(Map<String, String> params, List<String> selected);

}
