package com.wanderlust.bilibilisearcher.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.entity.Filter;
import com.wanderlust.bilibilisearcher.tools.layouts.FlowLayout;
import com.wanderlust.bilibilisearcher.tools.layouts.SingleFlowLayout;
import com.wanderlust.bilibilisearcher.tools.interfaces.FilterObserver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索条件列表adapter
 */
public class FilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "FilterAdapter";

    private List<Filter> mList;
    private Map<String, String> mParamsMap;

    private Context mContext;

    private FilterObserver mObserver;

    public FilterAdapter(FilterObserver observer) {
        this.mList = new ArrayList<>();
        this.mObserver = observer;
    }

    /**
     * 更新RecyclerView
     * @param list 更新的内容列表
     */
    public void update(List<Filter> list) {
        this.mList = list;
        resetFilter();
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).
            inflate(R.layout.recycler_item_selector, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            Filter filter = mList.get(position);
            viewHolder.setIsRecyclable(false);
            viewHolder.title.setText(filter.getTitle());
            viewHolder.fl.setAdapter(new ArrayList<>(filter.getValues().keySet()),
                (FlowLayout.ItemView<String>) (item, holder1, inflate, i) -> {
                    //每个tag的点击事件
                    holder1.setText(R.id.tv_tag_text, item);
                    holder1.setOnClickListener(v -> viewHolder.fl.select(i));
                });
            viewHolder.fl.setOnSelectedListener((index) -> {
                //如果选择发生更改，则通知Fragment
                if (filter.getSelectedIndex() != index) {
                    filter.setSelectedIndex(index);
                    mParamsMap.put(filter.getField(), filter.getValueByIndex(index));
                    notifyFilterChanged();
                }
            });
            //如果存在已选择项则直接加载
            viewHolder.fl.select(filter.getSelectedIndex());
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //重置选择器，每项默认选择第一项，并通知Fragment所选项已更改
    public void resetFilter() {
        if (mParamsMap == null) {
            mParamsMap = new LinkedHashMap<>();
        } else {
            mParamsMap.clear();
        }
        for (Filter filter : mList) {
            filter.setSelectedIndex(0);
            mParamsMap.put(filter.getField(), filter.getValueByIndex(0));
        }
        notifyFilterChanged();
        notifyDataSetChanged();
    }

    //通知Fragment筛选项已更改，发送已选参数信息
    private void notifyFilterChanged() {
        List<String> selected = new ArrayList<>();
        for (Filter filter : mList) {
            if (!filter.getValueByIndex(filter.getSelectedIndex()).equals("-1")) {
                if (filter.getField().equals("order")) {
                    selected.add("按" + filter.getKeyByIndex(filter.getSelectedIndex()) + "排序");
                } else {
                    selected.add(filter.getKeyByIndex(filter.getSelectedIndex()));
                }
            }
        }
        mObserver.onFilterChanged(mParamsMap, selected);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView title;
        SingleFlowLayout fl;
        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.title = itemView.findViewById(R.id.item_selector_title);
            this.fl = itemView.findViewById(R.id.item_selector_fl);
        }
    }

}