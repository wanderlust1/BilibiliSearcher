package com.wanderlust.bilibilisearcher.entity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoDetail {

    private String title;     //标题
    private String subTitle;  //原标题
    private String cover;     //封面图片地址

    private String score;     //评分
    private String count;     //评分人数

    private String payment;   //付费标签
    private String episode;   //集数信息

    private String type;      //视频类型
    private String area;      //地区
    private String date;      //开播日期

    private String plays;     //播放量
    private String danmakus;  //弹幕量
    private String favorites; //订阅量

    private List<String> tag;  //风格标签
    private String cv;   //cv表
    private String staff;//职员表
    private String desc;       //简介

    public VideoDetail(JSONObject data) throws JSONException {
        title = data.isNull("title") ? "暂无信息" : data.getString("title");
        subTitle = data.isNull("origin_name") ? "暂无信息" : data.getString("origin_name");
        if (!data.isNull("media")) {
            JSONObject media = data.getJSONObject("media");
            cover = media.isNull("cover") ? "" : media.getString("cover");
            score = media.isNull("rating") ? ""
                : media.getJSONObject("rating").isNull("score") ? ""
                : media.getJSONObject("rating").getString("score");
            count = media.isNull("rating") ? ""
                : media.getJSONObject("rating").isNull("count") ? ""
                : media.getJSONObject("rating").getString("count");
            type = media.isNull("type_name") ? "" : media.getString("type_name");
            episode = media.isNull("episode_index") ? ""
                : media.getJSONObject("episode_index").isNull("index_show") ? ""
                : media.getJSONObject("episode_index").getString("index_show");
        } else {
            cover = ""; score = ""; count = ""; type = ""; episode = "";
        }
        payment = data.isNull("payment") ? ""
            : data.getJSONObject("payment").isNull("vip_promotion") ? ""
            : data.getJSONObject("payment").getString("vip_promotion");
        area = data.isNull("area") ? "暂无信息" : data.getString("area");
        String[] t = (data.isNull("pub_time") ? "" : data.getString("pub_time")).split(" ");
        date = t.length > 1 ? t[0] : "暂无信息";
        plays = data.isNull("play_count") ? "暂无信息" : data.getString("play_count");
        danmakus = data.isNull("danmaku_count") ? "暂无信息" : data.getString("danmaku_count");
        favorites = data.isNull("favorites") ? "暂无信息" : data.getString("favorites");
        tag = new ArrayList<>();
        if (!data.isNull("tags")) {
            JSONArray array = data.getJSONArray("tags");
            for (int i = 0; i < array.length(); i++) {
                tag.add(array.getJSONObject(i).getString("tag_name"));
            }
        }
        StringBuilder builder = new StringBuilder();
        if (!data.isNull("actor")) {
            JSONArray array2 = data.getJSONArray("actor");
            for (int i = 0; i < array2.length(); i++) {
                builder.append(array2.getJSONObject(i).getString("role")).append("：").
                    append(array2.getJSONObject(i).getString("actor")).
                    append(i >= array2.length() - 1 ? "" : "\n");
            }
        }
        cv = builder.toString().isEmpty() ? "QAQ!暂时还没有找到相关的资料" : builder.toString();
        staff = data.isNull("staff") || data.getString("staff").isEmpty() ?
            "QAQ!暂时还没有找到相关的资料" : data.getString("staff");
        desc = data.isNull("evaluate") || data.getString("evaluate").isEmpty() ?
            "QAQ!暂时还没有找到相关的资料" : data.getString("evaluate");
        Log.d("1", toString());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
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

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlays() {
        return plays;
    }

    public void setPlays(String plays) {
        this.plays = plays;
    }

    public String getDanmakus() {
        return danmakus;
    }

    public void setDanmakus(String danmakus) {
        this.danmakus = danmakus;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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

    @Override
    public String toString() {
        return "{" +
            "title='" + title + '\'' +
            ", subTitle='" + subTitle + '\'' +
            ", cover='" + cover + '\'' +
            ", score='" + score + '\'' +
            ", count='" + count + '\'' +
            ", payment='" + payment + '\'' +
            ", episode='" + episode + '\'' +
            ", type='" + type + '\'' +
            ", area='" + area + '\'' +
            ", date='" + date + '\'' +
            ", plays='" + plays + '\'' +
            ", danmakus='" + danmakus + '\'' +
            ", favorites='" + favorites + '\'' +
            ", tag=" + tag +
            ", cv='" + cv + '\'' +
            ", staff='" + staff + '\'' +
            ", desc='" + desc + '\'' +
            '}';
    }
}
