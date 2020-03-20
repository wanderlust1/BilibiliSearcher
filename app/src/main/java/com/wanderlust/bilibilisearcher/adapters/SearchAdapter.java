package com.wanderlust.bilibilisearcher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.entity.VideoSearch;
import com.wanderlust.bilibilisearcher.tools.Tool;
import com.wanderlust.bilibilisearcher.tools.interfaces.ItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int TYPE_ERROR  = 0;
    private final int TYPE_NORMAL = 1;

    private List<VideoSearch> mList;

    private String message;   //提示信息界面的文本
    private int drawable = 0; //提示信息界面的图片

    private Context mContext;

    private ItemClickListener listener;

    public SearchAdapter(Context context) {
        this.mContext = context;
        this.mList = new ArrayList<>();
        update(mContext.getResources().getString(R.string.search_hint), R.drawable.img_search);
    }

    /**
     * 更新RecyclerView
     * @param list 更新的内容列表
     */
    public void update(List<VideoSearch> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    /**
     * 当搜索异常时更新RecyclerView
     * @param msg 提示信息
     */
    public void update(String msg, @DrawableRes int drawable) {
        this.mList.clear();
        this.message = msg;
        this.drawable = drawable;
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener listener) {
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageView cover;
        TextView tag, label, title, subtitle, score, count, msg, cv, desc;
        ViewHolder(View itemView) {
            super(itemView);
            this.view     = itemView;
            this.cover    = itemView.findViewById(R.id.search_item_cover);
            this.tag      = itemView.findViewById(R.id.search_item_cover_tag);
            this.label    = itemView.findViewById(R.id.search_item_label);
            this.title    = itemView.findViewById(R.id.search_item_title);
            this.subtitle = itemView.findViewById(R.id.search_item_subtitle);
            this.score    = itemView.findViewById(R.id.search_item_score);
            this.desc     = itemView.findViewById(R.id.search_item_desc);
            this.msg      = itemView.findViewById(R.id.search_item_msg);
            this.cv       = itemView.findViewById(R.id.search_item_cv);
            this.count    = itemView.findViewById(R.id.search_item_count);
        }
    }

    static class MsgViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView text;
        ImageView img;
        MsgViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.text = itemView.findViewById(R.id.search_msg_text);
            this.img = itemView.findViewById(R.id.search_msg_icon);
        }
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        if (viewType == TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item_search, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.recycler_item_search_msg, parent, false);
            return new MsgViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            VideoSearch item = mList.get(position);
            bindItem(viewHolder, item);
            viewHolder.view.setOnClickListener(v -> listener.onItemClick("1", ""));
        } else if (holder instanceof MsgViewHolder) {
            MsgViewHolder viewHolder = (MsgViewHolder) holder;
            viewHolder.text.setText(message);
            if (drawable != 0) {
                ViewGroup.LayoutParams params = viewHolder.img.getLayoutParams();
                params.width = Tool.dp2px(mContext,
                    drawable == R.drawable.img_loading_error ||
                    drawable == R.drawable.img_loading ? 165 : 120);
                viewHolder.img.setLayoutParams(params);
                viewHolder.img.setImageResource(drawable);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.isEmpty() ? TYPE_ERROR : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return mList.isEmpty() ? 1 : mList.size();
    }

    //绑定内容
    @SuppressLint("SetTextI18n")
    private void bindItem(ViewHolder holder, VideoSearch item) {
        //标题 副标题 简介 标签
        holder.title.setText(Html.fromHtml(item.getTitle()));
        holder.subtitle.setText(item.getOrgTitle().equals(
            Html.fromHtml(item.getTitle()).toString()) ? "" : item.getOrgTitle());
        holder.desc.setText(item.getDesc());
        holder.label.setText(item.getLabel());
        //时间 地区 风格
        List<String> msgs = new ArrayList<>();
        if (!item.getDate().isEmpty()) {
            msgs.add(item.getDate());
        }
        if (!item.getArea().isEmpty()) {
            msgs.add(item.getArea());
        }
        if (!item.getStyle().isEmpty()) {
            msgs.add(item.getStyle());
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < msgs.size(); i++) {
            builder.append(msgs.get(i)).append(i == msgs.size() - 1 ? "" :  " | ");
        }
        holder.msg.setText(builder.toString());
        //声优/演员表
        StringBuilder cvmsg = new StringBuilder();
        if (!item.getStaff().isEmpty()) {
            String[] staffs = item.getStaff().split("#");
            cvmsg.append(staffs.length >= 1 ? staffs[0] : "");
        }
        if (!item.getCv().isEmpty()) {
            String[] cvs = item.getCv().split("#");
            cvmsg.append((item.getStaff().isEmpty() || item.getCv().isEmpty()) ? "" : " | ")
                .append((cvs.length >= 1) ? cvs[0] : "")
                .append((cvs.length >= 2) ? " " + cvs[1] : "");
        }
        holder.cv.setText(cvmsg.toString().isEmpty() ? "暂无演职员信息" : cvmsg.toString());
        //评分
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.count.getLayoutParams();
        if (item.getScore().isEmpty() || item.getCount().isEmpty()) {
            holder.score.setVisibility(View.GONE);
            params.topMargin = Tool.dp2px(mContext, 10);
            holder.count.setText("暂无评分");
        } else {
            holder.score.setVisibility(View.VISIBLE);
            SpannableString sp = new SpannableString(item.getScore() + "分");
            RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.5f);
            sp.setSpan(sizeSpan, sp.length() - 1, sp.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.score.setText(sp);
            holder.count.setText(Tool.numFormat(item.getCount(), "人"));
            params.topMargin = Tool.dp2px(mContext, -3);
        }
        holder.count.setLayoutParams(params);
        //图片标签
        if (!item.getBanner().isEmpty()) {
            holder.tag.setVisibility(View.VISIBLE);
            holder.tag.setBackground(mContext.getDrawable(item.getBanner().equals("会员专享")
                ? R.drawable.bg_cover_tag_1
                : item.getBanner().equals("付费观看")
                ? R.drawable.bg_cover_tag_2 : R.drawable.bg_cover_tag_3));
            holder.tag.setText(item.getBanner());
        } else {
            holder.tag.setVisibility(View.INVISIBLE);
        }
        //封面图片
        Glide.with(mContext).load(item.getCover())
            .bitmapTransform(new RoundedCornersTransformation(mContext, 28, 0,  //圆角
                RoundedCornersTransformation.CornerType.ALL))
            .into(holder.cover);
    }

}
