package com.wanderlust.bilibilisearcher.adapters;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wanderlust.bilibilisearcher.R;
import com.wanderlust.bilibilisearcher.entity.Video;
import com.wanderlust.bilibilisearcher.tools.interfaces.ItemClickListener;
import com.wanderlust.bilibilisearcher.tools.interfaces.TypeChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 主布局的recyclerView的adapter
 * @author Wanderlust 2020.1.19
 */
public class DefaultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "DefaultAdapter";

    private List<Video> mList; //内容列表

    private String message;    //提示信息（通常是加载或出错提示）
    private String headText;   //头部信息（已选的筛选条件）
    private int drawable = 0;  //提示信息界面的图片
    private boolean hasNext;

    private Context mContext;

    private TypeChangeListener   typeChangeListener;
    private View.OnClickListener headClickListener;
    private View.OnClickListener iconClickListener;
    private ItemClickListener    itemClickListener;

    protected final int TYPE_ERROR   = 0;
    protected final int TYPE_NORMAL  = 1;
    protected final int TYPE_LOADING = 2;
    protected final int TYPE_HEAD    = 3;

    public DefaultAdapter() {
        this.mList = new ArrayList<>();
    }

    /**
     * 更新RecyclerView
     * @param list 更新的内容列表
     */
    public void update(List<Video> list, boolean hasNext) {
        this.mList = list;
        this.hasNext = hasNext;
        notifyDataSetChanged();
        typeChangeListener.onTypeChange(true);
    }

    /**
     * 当数据加载异常时更新RecyclerView
     * @param msg 提示信息
     */
    public void update(String msg, @DrawableRes int drawable) {
        if (mList.isEmpty()) { //更新整个视图
            this.message = msg;
            this.drawable = drawable;
            notifyDataSetChanged();
            typeChangeListener.onTypeChange(false);
        } else { //更新底部提示栏
            notifyItemChanged(getItemCount() - 1, msg);
            typeChangeListener.onTypeChange(true);
        }
    }

    public void updateHeader(String text) {
        headText = text;
    }

    public void setHeadClickListener(View.OnClickListener listener) {
        this.headClickListener = listener;
    }

    public void setHeadIconClickListener(View.OnClickListener listener) {
        this.iconClickListener = listener;
    }

    public void setTypeChangeListener(TypeChangeListener listener) {
        this.typeChangeListener = listener;
    }

    public void setOnItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(mContext == null) {
            mContext = parent.getContext();
        }
        switch (viewType) {
            case TYPE_NORMAL: {
                View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_item_main, parent, false);
                return new ViewHolder(view);
            }
            case TYPE_LOADING: {
                View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_item_load_more, parent, false);
                return new LoadingViewHolder(view);
            }
            case TYPE_HEAD: {
                View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_item_main_head, parent, false);
                return new HeadViewHolder(view);
            }
            default: {
                View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.recycler_item_main_msg, parent, false);
                return new ErrorViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            initialize((ViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            //判断当前单页item个数是否小于单页个数固定值，如果小于，则提示无更多内容
            ((LoadingViewHolder) holder).text.setText(!hasNext ?
                R.string.no_more_content : R.string.loading);
            ((LoadingViewHolder) holder).icon.setVisibility(!hasNext ?
                View.GONE : View.VISIBLE);
        } else if (holder instanceof ErrorViewHolder) {
            ((ErrorViewHolder) holder).text.setText(message);
            ((ErrorViewHolder) holder).img.setImageResource(drawable);
        } else if (holder instanceof HeadViewHolder) {
            ((HeadViewHolder) holder).icon.setOnClickListener(iconClickListener);
            ((HeadViewHolder) holder).view.setOnClickListener(headClickListener);
            ((HeadViewHolder) holder).text.setText(headText != null ? headText : "");
        }
    }

    //局部刷新RecyclerView的item
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else if (holder instanceof LoadingViewHolder && payloads.get(0) != null) {
            //将最底部的提示文本替换为msg
            ((LoadingViewHolder) holder).text.setText((String) payloads.get(0));
            ((LoadingViewHolder) holder).icon.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mList.isEmpty() ?
            (position == 0 ? TYPE_HEAD : TYPE_ERROR) :
            (position == getItemCount() - 1 ?  TYPE_LOADING :
            (position == 0 ? TYPE_HEAD : TYPE_NORMAL));
    }

    @Override
    public int getItemCount() {
        return mList.isEmpty() ? 2 : mList.size() + 2;
    }

    protected void initialize(ViewHolder holder, int position) {
        Video item = mList.get(position - 1);
        //标题 副标题 排序依据信息
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        holder.order.setText(item.getOrder());
        //封面右上角标签
        if (!item.getBadge().isEmpty()) {
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setBackground(mContext.getDrawable(item.getBadge().equals("会员专享")
                ? R.drawable.bg_cover_tag_1
                : item.getBadge().equals("付费观看")
                ? R.drawable.bg_cover_tag_2 : R.drawable.bg_cover_tag_3));
            holder.label.setText(item.getBadge());
        } else {
            holder.label.setVisibility(View.INVISIBLE);
        }
        //封面
        Glide.with(mContext)
            .load(item.getCover())
            .diskCacheStrategy(DiskCacheStrategy.RESULT)
            .into(holder.cover);
        //点击事件
        holder.view.setOnClickListener(v -> itemClickListener.onItemClick(item.getClassID(), item.getCover()));
    }

    //主要内容ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView label, order, title, subtitle;
        ImageView cover;
        ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            this.title = itemView.findViewById(R.id.main_item_title);
            this.subtitle = itemView.findViewById(R.id.main_item_subtitle);
            this.label = itemView.findViewById(R.id.main_item_cover_tag);
            this.cover = itemView.findViewById(R.id.main_item_cover);
            this.order = itemView.findViewById(R.id.main_item_order_msg);
        }
    }

    //底部提示信息ViewHolder
    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar icon;
        TextView text;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.item_loading_icon);
            this.text = itemView.findViewById(R.id.item_loading_text);
        }
    }

    //顶部筛选条件信息ViewHolder
    static class HeadViewHolder extends RecyclerView.ViewHolder {
        View view;
        ImageButton icon;
        TextView text;
        public HeadViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.icon = itemView.findViewById(R.id.item_main_head_button);
            this.text = itemView.findViewById(R.id.item_main_head_text);
        }
    }

    //全屏占位信息ViewHolder
    static class ErrorViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView text;
        ImageView img;
        ErrorViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            this.text = itemView.findViewById(R.id.main_item_msg_text);
            this.img = itemView.findViewById(R.id.main_item_msg_icon);
        }
    }

}
