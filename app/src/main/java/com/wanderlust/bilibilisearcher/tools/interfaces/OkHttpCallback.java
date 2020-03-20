package com.wanderlust.bilibilisearcher.tools.interfaces;

import com.wanderlust.bilibilisearcher.tools.OkHttpEngine;

/**
 * OkHttp请求的回调接口
 * @see OkHttpEngine
 * @author Wanderlust 2020.1.27
 */
public interface OkHttpCallback {

    /**
     * 当请求成功时的回调
     * @param text 请求返回的文本
     */
    void onResponse(String text) throws Exception;

}