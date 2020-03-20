package com.wanderlust.bilibilisearcher.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;

import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.adapters.SearchAdapter;
import com.wanderlust.bilibilisearcher.entity.VideoSearch;
import com.wanderlust.bilibilisearcher.tools.OkHttpEngine;
import com.wanderlust.bilibilisearcher.tools.ToastUtils;
import com.wanderlust.bilibilisearcher.tools.Tool;
import com.wanderlust.bilibilisearcher.tools.URLBuilder;
import com.wanderlust.bilibilisearcher.tools.interfaces.ItemClickListener;
import com.wanderlust.bilibilisearcher.tools.layouts.VideoDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements ItemClickListener {

    @BindView(R.id.et_search_keyword) EditText     mInput;
    @BindView(R.id.search_appbar)     AppBarLayout mAppBar;
    @BindView(R.id.search_clear_btn)  ImageView    mBtnClear;
    @BindView(R.id.search_rv_list)    RecyclerView mRecyclerView;

    private List<VideoSearch> mList;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchAdapter(this);
        mAdapter.setItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mList = new ArrayList<>();
        //动态设置AppBarLayout高度
        double statusBarHeight = Tool.getStatusBarHeight(this);
        ViewGroup.MarginLayoutParams p1 = (ViewGroup.MarginLayoutParams) mAppBar.getLayoutParams();
        p1.height = (int) (statusBarHeight + Tool.dp2px(this, 50));
        mAppBar.setLayoutParams(p1);
        //设置RecyclerView上外边距
        ViewGroup.MarginLayoutParams p2 = (ViewGroup.MarginLayoutParams) mRecyclerView.getLayoutParams();
        p2.topMargin = -(int) statusBarHeight;
        mRecyclerView.setLayoutParams(p2);
        //开启键盘搜索
        mInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String input = mInput.getText().toString().trim();
                if (input.isEmpty()) {
                    ToastUtils.make(this, R.string.search_input_empty);
                } else {
                    mAdapter.update("搜索中...", R.drawable.img_loading);
                    mList.clear();
                    onSearch(input); //执行搜索
                }
                return true;
            }
            return false;
        });
        //清除按钮事件
        mBtnClear.setOnClickListener(v -> mInput.setText(""));
        //清除按钮的显示与消失
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void afterTextChanged(Editable s) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBtnClear.setVisibility(s.length() == 0 ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void onSearch(String input) {
        Observable.just(
            "media_bangumi", "media_ft"
        ).map(type ->
            OkHttpEngine.instance(this).getSync(URLBuilder.searchAPI(input, type, 1))
        ).map(response -> {
            JSONObject data = new JSONObject(response);
            if (data.getInt("code") != 0) {
                throw new IllegalArgumentException("illegal input");
            }
            JSONObject child = data.getJSONObject("data");
            return child.isNull("result") ? new JSONArray() : child.getJSONArray("result");
        }).filter(
            array -> array != null && array.length() != 0
        ).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(
            Schedulers.newThread()
        ).subscribe(array -> { //onNext处理
            for (int i = 0; i < array.length(); i++) {
                mList.add(new VideoSearch(array.getJSONObject(i)));
            }
            Tool.closeKeyboard(this, mInput); //收起键盘
        }, throwable -> { //onError处理
            throwable.printStackTrace();
            if (throwable instanceof IllegalArgumentException) {
                //搜索关键词含有非法内容（大概率是）
                mAdapter.update(getResources().getString(R.string.search_illegal), R.drawable.img_loading_error);
            } else if (throwable instanceof UnknownHostException) {
                //网络断开
                mAdapter.update(getResources().getString(R.string.network_disconnect), R.drawable.img_loading_error);
            } else if (throwable instanceof SocketTimeoutException) {
                //搜索超时
                mAdapter.update(getResources().getString(R.string.network_timeout), R.drawable.img_loading_error);
            } else {
                //搜索遇到未知异常
                mAdapter.update(getResources().getString(R.string.unknown_error) + "："
                    + throwable.getMessage(), R.drawable.img_loading_error);
            }
        }, () -> { //onComplete处理
            if (mList.isEmpty()) {
                //搜索结果为空
                mAdapter.update(getResources().getString(R.string.search_empty), R.drawable.img_search_error);
            } else {
                mAdapter.update(mList);
            }
        });
    }

    @Override
    public void onItemClick(String id, String url) {/*
        VideoDialog videoDialog = new VideoDialog(this);
        videoDialog.initialize(id);
        videoDialog.show();*/
    }

}
