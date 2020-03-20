package com.wanderlust.bilibilisearcher.tools;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

public class URLBuilder {

    private final String BASE = "https://api.bilibili.com/pgc/season/index/result?";

    private Map<String, String> paramMap;

    private enum P {
        season_type,    //【必要】查询资源种类：番剧（1）、电影（2）、纪录片（3）、国创（4）、电视剧（5）
        type,           //【必要】未知，值必须为1
        pagesize,       //【必要】单页显示数目
        page,           //【必要】当前显示的页码
        order,          //【必要】排序方式：播放数量（2）、更新时间（0）、上映时间（6）、追番人数（3）、最高评分（4）等
        sort,           //根据order的类型从小到大排序（0）、从大到小排序（1）
        st,             //同season_type
    }

    public URLBuilder() {
        this.paramMap = new HashMap<>();
        this.paramMap.put(P.type.toString(), "1");
    }

    public String build() {
        StringBuilder builder = new StringBuilder(BASE);
        for (Map.Entry<String, String> i : paramMap.entrySet()) {
            builder.append(Uri.encode(i.getKey())).append("=")
                .append(Uri.encode(i.getValue())).append("&");
        }
        return builder.toString();
    }

    public URLBuilder set(Map<String, String> params) {
        this.paramMap = params;
        this.paramMap.put(P.type.toString(), "1");
        return this;
    }

    public URLBuilder page(int value) {
        this.paramMap.put(P.page.toString(), Integer.toString(value));
        return this;
    }

    public URLBuilder pageSize(int value) {
        this.paramMap.put(P.pagesize.toString(), Integer.toString(value));
        return this;
    }

    public URLBuilder order(String value) {
        this.paramMap.put(P.order.toString(), value);
        return this;
    }

    public URLBuilder sort(String value) {
        this.paramMap.put(P.sort.toString(), value);
        return this;
    }

    public URLBuilder type(String value) {
        this.paramMap.put(P.season_type.toString(), value);
        this.paramMap.put(P.st.toString(), value);
        return this;
    }

    /**
     * Bilibili搜索功能API的地址
     * @see com.wanderlust.bilibilisearcher.activity.SearchActivity
     * @param type media_ft（影视）、media_bangumi（番剧）
     * @param page 当前返回的为第几页（一页最多为20条）
     */
    public static String searchAPI(String keyword, String type, int page) {
        return "https://api.bilibili.com/x/web-interface/search/type?"
            + "search_type=" + type
            + "&page=" + page
            + "&keyword=" + keyword;
    }

    /**
     * Bilibili排行榜API的地址
     * @param type 查询资源种类：番剧（1）、电影（2）、纪录片（3）、国创（4）、电视剧（5）
     * @param day  返回几日内的排名数据（可选值为3、7）
     */
    public static String rankAPI(String type, int day) {
        return "https://api.bilibili.com/pgc/web/rank/list?"
            + "day=" + day
            + "&season_type=" + type;
    }

    /**
     * Bilibili番剧详细信息API的地址
     * @param id 番剧ID
     */
    public static String detailAPI(String id) {
        return "https://biliplus.ipcjs.top/api/bangumi?season=" + id;
    }

    /**
     * Bilibili索引筛选条件API的地址
     * @see com.wanderlust.bilibilisearcher.Fragments.BaseFragment
     * @see com.wanderlust.bilibilisearcher.adapters.FilterAdapter
     * @param type 查询资源种类：番剧（1）、电影（2）、纪录片（3）、国创（4）、电视剧（5）
     */
    public static String indexAPI(String type) {
        return "http://api.bilibili.com/pgc/season/index/condition?type=1&season_type=" + type;
    }

}
