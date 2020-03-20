package com.wanderlust.bilibilisearcher.tools.layouts;

import android.content.Context;
import android.util.AttributeSet;

import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.tools.interfaces.OnSelectedListener;

import java.util.List;

/**
 * 【用于单选的】流式标签布局
 * @author Wanderlust 2020.1.26
 */
public class SingleFlowLayout extends FlowLayout {

    private int lastSelected = -1;

    private OnSelectedListener listener;

    public SingleFlowLayout(Context context) {
        super(context);
    }

    public SingleFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重载setAdapter，默认使用tag_text.xml作为布局
     */
    public void setAdapter(List<?> list, ItemView mItemView) {
        super.setAdapter(list, R.layout.tag_selector_text, mItemView);
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        this.listener = listener;
    }

    public void select(int index) { //选中指定位置的tag，并将原来选中的tag取消选中
        if (index != lastSelected && getChildCount() > 0 && index >= 0) {
            if (lastSelected >= 0) {
                getChildAt(lastSelected).setActivated(false);
            }
            getChildAt(index).setActivated(true);
            lastSelected = index;
            listener.onSelect(lastSelected);
        }
    }

}
