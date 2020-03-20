package com.wanderlust.bilibilisearcher.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Video {

    private String title;    //番剧（影视）名称
    private String subtitle; //副标题，显示附加信息
    private String badge;    //视频特殊标签
    private String cover;    //封面图片地址
    private String order;    //按特定顺序排序的提示信息
    private String videoID;  //该集番剧（影视）的ID
    private String classID;  //整部番剧（影视）的ID

    public Video(JSONObject data) throws JSONException {
        this.title    = data.getString("title");
        this.subtitle = data.getString("index_show");
        this.badge    = data.getString("badge");
        this.cover    = data.getString("cover");
        this.order    = data.getString("order");
        this.videoID  = data.getString("media_id");
        this.classID  = data.getString("season_id");
    }

    public Video() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }

    @Override
    public String toString() {
        return
            "title='" + title + '\'' +
            ", subtitle='" + subtitle + '\'' +
            ", badge='" + badge + '\'' +
            ", cover='" + cover + '\'' +
            ", order='" + order + '\'' +
            ", videoID='" + videoID + '\'' +
            ", classID='" + classID + '\'';
    }
}
