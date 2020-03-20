package com.wanderlust.bilibilisearcher.Fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.adapters.DefaultAdapter;
import com.wanderlust.bilibilisearcher.adapters.FilterAdapter;
import com.wanderlust.bilibilisearcher.entity.Filter;
import com.wanderlust.bilibilisearcher.entity.Video;
import com.wanderlust.bilibilisearcher.tools.OkHttpEngine;
import com.wanderlust.bilibilisearcher.tools.ToastUtils;
import com.wanderlust.bilibilisearcher.tools.Tool;
import com.wanderlust.bilibilisearcher.tools.URLBuilder;
import com.wanderlust.bilibilisearcher.tools.interfaces.FilterObserver;
import com.wanderlust.bilibilisearcher.tools.interfaces.ItemClickListener;
import com.wanderlust.bilibilisearcher.tools.layouts.VideoDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 主布局的Fragment基类
 * @author Wanderlust 2020.1.19
 */
public abstract class BaseFragment extends Fragment implements FilterObserver, ItemClickListener {

    private final String TAG = "BaseFragment";

    private DefaultAdapter mDefaultAdapter;
    private FilterAdapter  mFilterAdapter;

    private BottomSheetDialog mDialog;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private FloatingActionButton mButton;

    private GridLayoutManager gridManager;
    private LinearLayoutManager linearManager;

    private Resources mRes;

    private Map<String, String> params; //当前筛选参数列表
    private List<Video> mList; //数据列表

    private int currPage = 1; //当前已加载到的最大页码
    private int hasNext  = 1; //是否还有下一页
    private boolean isCreated = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getViewLayoutID(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isCreated = true;
        Activity activity = Objects.requireNonNull(getActivity());
        mRes = activity.getResources();
        initFilterDialog(activity);
        initRecyclerView(activity);
        initRefreshLayout(activity);
        resetDefaultAdapter();
        initFloatingButton(activity);
        if (getUserVisibleHint()) {
            lazyLoadFilter();
        }
    }

    private void initRecyclerView(Activity activity) {
        mList = new ArrayList<>();
        mRecyclerView = activity.findViewById(getRecyclerViewID());
        linearManager = new LinearLayoutManager(activity);
        linearManager.setOrientation(LinearLayoutManager.VERTICAL);
        gridManager = new GridLayoutManager(activity, 3);
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return i == mList.size() + 1 || i == 0 ? 3 : 1;
            }
        });
        mRecyclerView.addItemDecoration(Tool.getGridDecoration(activity, 3));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                onRecyclerViewScroll();
            }
        });
        mDefaultAdapter = new DefaultAdapter();
        mDefaultAdapter.setTypeChangeListener(isNormal ->
            mRecyclerView.setLayoutManager(isNormal ? gridManager : linearManager));
        mDefaultAdapter.setOnItemClickListener(this);
        mDefaultAdapter.setHeadClickListener(v -> mDialog.show());
        mDefaultAdapter.setHeadIconClickListener(v -> {
            mFilterAdapter.resetFilter();
            ToastUtils.make(activity, "筛选已重置");
        });
        mRecyclerView.setAdapter(mDefaultAdapter);
    }

    private void initRefreshLayout(Activity activity) {
        mRefreshLayout = activity.findViewById(getRefreshLayoutID());
        mRefreshLayout.setOnRefreshListener(() -> {
            if (params == null) {
                lazyLoadFilter();
            } else {
                resetDefaultAdapter(); //如果当前存在url，则无需再次加载筛选条件
            }
            mRefreshLayout.setRefreshing(false);
        });
    }

    @SuppressLint("CheckResult, InflateParams")
    private void initFilterDialog(Activity activity) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_selector, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_dialog_selector);
        mFilterAdapter = new FilterAdapter(this);
        recyclerView.setAdapter(mFilterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(Tool.getFilterDecoration(activity));
        mDialog = new BottomSheetDialog(activity, R.style.BottomSheetDialog);
        mDialog.setTitle(null);
        mDialog.setContentView(view);
        //设置最大高度为0.75倍屏幕高度，且dialog不能展开至全屏状态
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) view.getParent());
        behavior.setPeekHeight((int) (0.75 * mRes.getDisplayMetrics().heightPixels));
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = behavior.getPeekHeight();
        view.setLayoutParams(params);
    }

    private void initFloatingButton(Activity activity) {
        mButton = activity.findViewById(getFloatingButtonID());
        mButton.setOnClickListener(v -> mRecyclerView.smoothScrollToPosition(0));
        ObjectAnimator showAnim = ObjectAnimator.ofFloat(mButton, "translationY", 200, 0);
        showAnim.setDuration(200);
        ObjectAnimator hideAnim = ObjectAnimator.ofFloat(mButton, "translationY", 0, 200);
        hideAnim.setDuration(200);
        mButton.hide();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView r, int dx, int dy) {
                if (r.getLayoutManager() instanceof GridLayoutManager) {
                    if (dy > 12 && mButton.isShown()) {
                        mButton.hide();
                        hideAnim.start();
                    } else if (dy < -12 && !mButton.isShown()) {
                        mButton.show();
                        showAnim.start();
                    }
                    int p = ((GridLayoutManager) r.getLayoutManager()).findFirstVisibleItemPosition();
                    if (p == 0) {
                        mButton.hide();
                        hideAnim.start();
                    }
                }
            }
        });
    }

    //重置列表adapter
    private void resetDefaultAdapter() {
        currPage = 1;
        hasNext = 1;
        mList.clear();
        mDefaultAdapter.update(mRes.getString(R.string.loading), R.drawable.img_loading);
        if (params != null) {
            loadData();
        }
    }

    //当fragment对用户可见时，加载数据
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreated && isVisibleToUser && mList.isEmpty()) {
            if (params == null) {
                lazyLoadFilter();
            } else {
                resetDefaultAdapter(); //如果当前存在url，则无需再次加载筛选条件
            }
        }
    }

    //当筛选条件变化时调用
    @Override
    public void onFilterChanged(Map<String, String> params, List<String> selected) {
        this.params = params;
        resetDefaultAdapter();
        //获取每个当前已筛选的参数，更改列表头项的信息
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selected.size(); i++) {
            builder.append(selected.get(i)).append(i < selected.size() - 1 ? " • " : "");
        }
        mDefaultAdapter.updateHeader(builder.toString());
    }

    //加载数据
    @SuppressLint("CheckResult")
    protected void loadData() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String url = new URLBuilder()
                .set(params)
                .type(getType())
                .pageSize(mRes.getInteger(R.integer.single_page_count))
                .page(currPage)
                .build();
            emitter.onNext(OkHttpEngine.instance(getActivity()).getSync(url));
        }).map(s ->
            new JSONObject(s).getJSONObject("data")
        ).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(
            Schedulers.newThread()
        ).subscribe(data -> {
            currPage++;
            hasNext = data.getInt("has_next");
            if (data.getInt("total") == 0) {
                mDefaultAdapter.update(mRes.getString(R.string.search_empty), R.drawable.img_search_error);
            } else {
                JSONArray array = data.getJSONArray("list");
                List<Video> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    list.add(new Video(array.getJSONObject(i)));
                }
                mList.addAll(list);
                mDefaultAdapter.update(mList, hasNext == 1);
            }
        }, throwable -> exceptionHandle(throwable));
    }

    //加载筛选条件数据
    @SuppressLint("CheckResult")
    private void lazyLoadFilter() {
        List<Filter> filters = new ArrayList<>();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String url = URLBuilder.indexAPI(getType());
            emitter.onNext(OkHttpEngine.instance(getActivity()).getSync(url));
        }).map(s ->
            new JSONObject(s).getJSONObject("data")
        ).filter(
            data -> data != null
        ).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(
            Schedulers.newThread()
        ).subscribe(data -> {
            //排序方式
            JSONArray order = data.getJSONArray("order");
            Filter filter = new Filter();
            filter.setTitle("排序方式");
            filter.setField("order");
            LinkedHashMap<String, String> map = new LinkedHashMap<>();
            for (int i = 0; i < order.length(); i++) {
                map.put(order.getJSONObject(i).getString("name"),
                    order.getJSONObject(i).getString("field"));
            }
            filter.setValues(map);
            filters.add(filter);
            //筛选条件
            JSONArray array = data.getJSONArray("filter");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONArray values = object.getJSONArray("values");
                Filter f = new Filter();
                f.setTitle(object.getString("name"));
                f.setField(object.getString("field"));
                LinkedHashMap<String, String> m = new LinkedHashMap<>();
                for (int j = 0; j < values.length(); j++) {
                    m.put(values.getJSONObject(j).getString("name"),
                        values.getJSONObject(j).getString("keyword"));
                }
                f.setValues(m);
                filters.add(f);
            }
            mFilterAdapter.update(filters);
        }, throwable -> exceptionHandle(throwable));
    }

    //滑动到底部更新下一页数据
    private void onRecyclerViewScroll() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            //lastPosition为当前显示的最后的item的位置
            int lastPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (lastPosition == mRecyclerView.getLayoutManager().getItemCount() - 1 && hasNext == 1) {
                loadData(); //有下一页，加载下一页
            }
        }
    }

    //当列表内的item被点击时，弹出视频详情对话框
    @Override
    public void onItemClick(String id, String url) {
        if (getType().equals("1") || getType().equals("4")) {
            VideoDialog videoDialog = new VideoDialog(getActivity());
            videoDialog.initialize(id, url);
            videoDialog.show();
        }
    }

    //异常处理
    private void exceptionHandle(Throwable e) {
        e.printStackTrace();
        if (e instanceof SocketTimeoutException) {
            mDefaultAdapter.update(getResources().getString(R.string.network_timeout), R.drawable.img_loading_error);
        } else if (e instanceof UnknownHostException) {
            mDefaultAdapter.update(getResources().getString(R.string.network_disconnect),
                R.drawable.img_loading_error);
        } else {
            mDefaultAdapter.update(getResources().getString(
                R.string.unknown_error) + "：" + e.getMessage(),
                R.drawable.img_loading_error);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        currPage = 1;
        hasNext = 1;
        mList.clear();
        mRes = null;
    }

    /**
     * @return Fragment设置ContentView的布局ID
     */
    @LayoutRes
    protected abstract int getViewLayoutID();

    /**
     * @return 绑定RecyclerView的布局ID
     */
    @IdRes
    protected abstract int getRecyclerViewID();

    /**
     * @return 绑定RefreshLayout的布局ID
     */
    @IdRes
    protected abstract int getRefreshLayoutID();

    /**
     * @return 绑定FloatingButton的布局ID
     */
    @IdRes
    protected abstract int getFloatingButtonID();

    /**
     * @return 数据URL的season_type取值
     */
    protected abstract String getType();

}