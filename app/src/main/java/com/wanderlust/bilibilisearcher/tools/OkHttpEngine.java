package com.wanderlust.bilibilisearcher.tools;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpEngine {

    private static volatile OkHttpEngine mInstance;
    private OkHttpClient mOkHttpClient;

    public static OkHttpEngine instance(Context context) {
        if (mInstance == null) {
            synchronized (OkHttpEngine.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpEngine(context);
                }
            }
        }
        return mInstance;
    }

    private OkHttpEngine(Context context) {
        File sdCache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .connectTimeout(8, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .cache(new Cache(Objects.requireNonNull(sdCache).getAbsoluteFile(), cacheSize));
        mOkHttpClient = builder.build();
    }

    /** 异步GET方法（不创建线程） */
    public synchronized String getSync(String url) throws IOException {
        final Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return Objects.requireNonNull(response.body()).string();
    }

}