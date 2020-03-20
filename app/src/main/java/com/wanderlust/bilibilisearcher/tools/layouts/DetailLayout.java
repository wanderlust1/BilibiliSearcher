package com.wanderlust.bilibilisearcher.tools.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wanderlust.bilibilisearcher.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailLayout extends RelativeLayout {

    private TextView mTitle;
    private TextView mExpand;
    private TextView mMessage;

    private boolean isExpand = false; //判断是否展开

    public DetailLayout(Context context) {
        super(context);
        init(context);
    }

    public DetailLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DetailLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.sub_layout_detail_msg, this, true);
        mExpand = findViewById(R.id.detail_sub_expand);
        mTitle = findViewById(R.id.detail_sub_title);
        mMessage = findViewById(R.id.detail_sub_msg);
        mExpand.setOnClickListener(v -> {
            if (isExpand) {
                isExpand = false;
                mMessage.setMaxLines(3);
                mExpand.setText("更多");
                mExpand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_more, 0);
            } else {
                isExpand = true;
                mMessage.setMaxLines(Integer.MAX_VALUE);
                mExpand.setText("收起");
                mExpand.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expand_less, 0);
            }
        });
    }

    public void setTitle(String title) {
        this.mTitle.setText(title);
    }

    public void setText(String message) {
        this.mMessage.setText(message);
    }

    public void setExpandVisibility(int visibility) {
        this.mExpand.setVisibility(visibility);
        this.mMessage.setMaxLines(Integer.MAX_VALUE);
    }

}
