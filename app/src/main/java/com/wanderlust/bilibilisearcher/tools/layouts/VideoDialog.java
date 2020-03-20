package com.wanderlust.bilibilisearcher.tools.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.entity.VideoDetail;
import com.wanderlust.bilibilisearcher.tools.OkHttpEngine;
import com.wanderlust.bilibilisearcher.tools.Tool;
import com.wanderlust.bilibilisearcher.tools.URLBuilder;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class VideoDialog extends BottomSheetDialog {

    private View mView;

    @BindView(R.id.detail_btn_cancel) ImageButton mBtnCancel;
    @BindView(R.id.detail_bar_title)  TextView    mBarTitle;
    @BindView(R.id.detail_cover)      ImageView   mCover;
    @BindView(R.id.detail_title)      TextView    mTitle;
    @BindView(R.id.detail_subtitle)   TextView    mSubtitle;
    @BindView(R.id.detail_score)      TextView    mScore;
    @BindView(R.id.detail_count)      TextView    mScoreCount;
    @BindView(R.id.detail_payment)    TextView    mPayment;
    @BindView(R.id.detail_ep)         TextView    mEp;
    @BindView(R.id.detail_area_msg)   TextView    mArea;
    @BindView(R.id.detail_play_count) TextView    mPlayCount;
    @BindView(R.id.detail_tags_title) TextView    mTagsTitle;
    @BindView(R.id.detail_tags)       FlowLayout  mTags;
    @BindView(R.id.detail_cv)         DetailLayout mCv;
    @BindView(R.id.detail_staff)      DetailLayout mStaff;
    @BindView(R.id.detail_desc)       DetailLayout mDesc;
    @BindView(R.id.detail_img_err)    ImageView    mErrImg;
    @BindView(R.id.detail_tv_err)     TextView     mErrText;
    @BindView(R.id.detail_ll_err)     LinearLayout mErrLayout;
    @BindView(R.id.detail_coo)        CoordinatorLayout mContainer;

    private String id;
    private String url;
    private VideoDetail mVideo;

    private BottomSheetBehavior mBehavior;
    private Context mContext;

    public VideoDialog(@NonNull Context context) {
        super(context, R.style.FullScreenBottomSheetDialog);
    }

    public void initialize(String id, String url) {
        this.id = id;
        this.url = url;
        mContext = getContext();
        mView = View.inflate(mContext, R.layout.fragment_video_detail, null);
        setContentView(mView);
        ButterKnife.bind(this);
        setFullScreen();
        loadData();
    }

    @OnClick(R.id.detail_btn_cancel)
    public void onClick() {
        if (isShowing()) {
            dismiss();
        }
    }

    private void initView() {
        //标题
        mTitle.setText(mVideo.getTitle());
        mSubtitle.setText(mVideo.getSubTitle());
        //评分
        if (mVideo.getScore().isEmpty() || mVideo.getCount().isEmpty()) {
            mScore.setVisibility(View.GONE);
            mScoreCount.setText("暂无评分");
        } else {
            mScore.setText(mVideo.getScore());
            int c = Integer.parseInt(mVideo.getCount());
            String count = (c > 9999
                ? new DecimalFormat("#.00").format(((double) c) / 10000) + "万人" : c + "人");
            mScoreCount.setText(count);
        }
        //付费信息
        if (!mVideo.getPayment().isEmpty()) {
            mPayment.setText(mVideo.getPayment());
        } else {
            mPayment.setVisibility(View.GONE);
        }
        //集数
        mEp.setText(mVideo.getEpisode());
        //地区时间
        List<String> list1 = new ArrayList<>();
        if (!mVideo.getType().isEmpty()) {
            list1.add(mVideo.getType());
        }
        if (!mVideo.getArea().isEmpty()) {
            list1.add(mVideo.getArea());
        }
        if (!mVideo.getDate().isEmpty()) {
            list1.add(mVideo.getDate());
        }
        StringBuilder builder1 = new StringBuilder();
        for (int i = 0; i < list1.size(); i++) {
            builder1.append(list1.get(i)).append(i == list1.size() - 1 ? "" :  " • ");
        }
        mArea.setText(builder1.toString());
        //播放量
        List<String> list2 = new ArrayList<>();
        if (!mVideo.getPlays().isEmpty()) {
            list2.add(Tool.numFormat(mVideo.getPlays(), "播放"));
        }
        if (!mVideo.getDanmakus().isEmpty()) {
            list2.add(Tool.numFormat(mVideo.getDanmakus(), "弹幕"));
        }
        if (!mVideo.getFavorites().isEmpty()) {
            list2.add(Tool.numFormat(mVideo.getFavorites(), "追番"));
        }
        StringBuilder builder2 = new StringBuilder();
        for (int i = 0; i < list2.size(); i++) {
            builder2.append(list2.get(i)).append(i == list2.size() - 1 ? "" :  " • ");
        }
        mPlayCount.setText(builder2.toString());
        //封面
        Glide.with(mContext).load(url)
            .bitmapTransform(new RoundedCornersTransformation(mContext, 28, 0,
                RoundedCornersTransformation.CornerType.ALL))
            .into(mCover);
        //标签
        if (mVideo.getTag().isEmpty()) {
            mTags.setAdapter(Collections.singletonList("暂无标签"));
        } else {
            mTags.setAdapter(mVideo.getTag());
        }
        //简介
        mDesc.setTitle("简介");
        mDesc.setText(mVideo.getDesc());
        mDesc.setExpandVisibility(View.GONE);
        //声优
        mCv.setTitle("角色声优");
        mCv.setText(mVideo.getCv());
        //制作
        mStaff.setTitle("制作信息");
        mStaff.setText(mVideo.getStaff());
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String url = URLBuilder.detailAPI(id);
            Log.d("2", url);
            emitter.onNext(OkHttpEngine.instance(mContext).getSync(url));
        }).map(text ->
            new JSONObject(text).getJSONObject("result")
        ).observeOn(
            AndroidSchedulers.mainThread()
        ).subscribeOn(
            Schedulers.newThread()
        ).subscribe(data -> {
            Log.d("2", data.toString());
            mVideo = new VideoDetail(data);
            initView();
            mErrLayout.setVisibility(View.GONE);
        }, throwable -> {
            throwable.printStackTrace();
            mErrImg.setImageResource(R.drawable.img_loading_error);
            mErrLayout.setVisibility(View.VISIBLE);
            if (throwable instanceof SocketTimeoutException) {
                mErrText.setText(R.string.network_timeout);
            } else if (throwable instanceof UnknownHostException) {
                mErrText.setText(R.string.network_disconnect);
            } else {
                mErrText.setText(throwable.getMessage());
            }
        });
    }

    //设置对话框全屏
    private void setFullScreen() {
        int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
        int statusHeight = Tool.getStatusBarHeight(mContext);
        ViewGroup.LayoutParams params = mView.getLayoutParams();
        params.height = screenHeight;
        mView.setLayoutParams(params);
        mBehavior = BottomSheetBehavior.from((View) mView.getParent());
        mBehavior.setPeekHeight(params.height);
        getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mContainer.setPadding(0, statusHeight, 0, 0);
    }

}
