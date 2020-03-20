package com.wanderlust.bilibilisearcher.tools;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 解决重复点击弹出Toast长时间不消失的问题
 */
public class ToastUtils {

    private static Toast toast;

    public static void make(Context context, String message) {
        if (toast == null) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            //原先存在toast，则更改该toast的文字
            toast.cancel();
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void make(Context context, @StringRes int id) {
        make(context, context.getResources().getString(id));
    }

}