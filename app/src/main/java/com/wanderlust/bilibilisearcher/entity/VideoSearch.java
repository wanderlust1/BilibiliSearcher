package com.wanderlust.bilibilisearcher.entity;

import android.text.Html;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.annotations.NonNull;

public class VideoSearch {

    private String title;    //标题
    private String orgTitle; //原标题
    private String label;    //标题标签
    private String cover;    //图片地址
    private String banner;   //图片右上角标签
    private String desc;     //简介
    private String score;    //评分
    private String count;    //评分人数
    private String area;     //地区
    private String style;    //风格
    private String cv;       //cv表
    private String staff;    //职员表
    private String date;     //开播日期
    private String videoID;  //该集番剧（影视）的ID
    private String classID;  //整部番剧（影视）的ID

    public VideoSearch(@NonNull JSONObject object) throws JSONException {
        this.title = object.isNull("title") ? "" :
            object.getString("title")
                .replace("class=\"keyword\"", "color='#E86A8D'")
                .replace("em", "font");
        this.orgTitle = object.isNull("org_title") ? "" : Html.fromHtml(object.getString("org_title")).toString();
        this.label = object.isNull("season_type_name") ? "" : object.getString("season_type_name");
        this.cover = object.isNull("cover") ? "" : "http:" + object.getString("cover");
        this.banner = object.isNull("angle_title") ? "" : object.getString("angle_title");
        this.desc = object.isNull("desc") ? "" : object.getString("desc").replace("\n", "");
        this.score = object.isNull("media_score") ? "" : object.getJSONObject("media_score").getString("score");
        this.count = object.isNull("media_score") ? "" : object.getJSONObject("media_score").getString("user_count");
        this.style =  object.isNull("styles") ? "" : object.getString("styles");
        this.area = object.isNull("areas") ? "" : object.getString("areas");
        this.staff = object.isNull("staff") ? "" : object.getString("staff").replace("\n", "#").replace("： ", "：");
        this.cv = object.isNull("cv") ? "" : object.getString("cv").replace("\n", "#").replace("： ", "：");
        if (!object.isNull("fix_pubtime_str") && !object.getString("fix_pubtime_str").isEmpty()) {
            this.date = object.getString("fix_pubtime_str");
        } else if (!object.isNull("pubtime")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
            this.date = format.format(new Date((object.getLong("pubtime") + 24 * 60 * 60) * 1000));
        } else {
            this.date = "";
        }
        this.classID = object.getString("season_id");
        this.videoID = object.getString("media_id");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOrgTitle() {
        return orgTitle;
    }

    public void setOrgTitle(String orgTitle) {
        this.orgTitle = orgTitle;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getStaff() {
        return staff;
    }

    public void setStaff(String staff) {
        this.staff = staff;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        return "{" +
            "title='" + title + '\'' +
            ", orgTitle='" + orgTitle + '\'' +
            ", label='" + label + '\'' +
            ", cover='" + cover + '\'' +
            ", banner='" + banner + '\'' +
            ", desc='" + (desc.length() > 30 ? desc.substring(0, 25) : desc) + "...\'" +
            ", score='" + score + '\'' +
            ", count='" + count + '\'' +
            ", area='" + area + '\'' +
            ", style='" + style + '\'' +
            ", cv='" + (cv.length() > 20 ? cv.substring(0, 15) : cv) + "...\'" +
            ", staff='" + (staff.length() > 20 ? staff.substring(0, 15) : staff) + "...\'" +
            ", date='" + date + '\'' +
            '}';
    }

}
