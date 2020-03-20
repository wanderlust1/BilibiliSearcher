package com.wanderlust.bilibilisearcher.entity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件实体类
 */
public class Filter {

    private String title; //搜索条件的中文名称     eg.地区
    private String field; //搜索条件的get参数名称  eg.area
    private LinkedHashMap<String, String> values; //搜索条件的每个具体中文名称及其对应get参数值，eg.中国：1

    private int selectedIndex = -1; //该搜索条件已选值的位置

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(LinkedHashMap<String, String> values) {
        this.values = values;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    //返回参数Map中的第index个元素的值
    public String getValueByIndex(int index) {
        List<String> keys = new ArrayList<>(values.keySet());
        return values.get(keys.get(index));
    }

    //返回参数Map中的第index个元素的键
    public String getKeyByIndex(int index) {
        List<String> keys = new ArrayList<>(values.keySet());
        return keys.get(index);
    }

    @NotNull @Override
    public String toString() {
        return "Filter{" +
            "title='" + title + '\'' +
            ", field='" + field + '\'' +
            ", values=" + values +
            '}';
    }

}
