package com.wanderlust.bilibilisearcher.tools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.wanderlust.bilibilisearcher.R;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Objects;

public class Tool {

    /** 使Android7.0以上的状态栏半透明蒙版失效，达到状态栏全透明效果 */
    public static void setSemiStatusBarDisable(Window window) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(window.getDecorView(), Color.TRANSPARENT); //改为透明
            } catch (Exception ignore) {}
        }
    }

    /** 单位转换，将dp转成px **/
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            dpVal, context.getResources().getDisplayMetrics());
    }

    /** 获得系统状态栏高度 */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceID > 0) {
            result = context.getResources().getDimensionPixelSize(resourceID);
        }
        return result;
    }

    /** 创建一个圆形进度条窗口，不显示文字 */
    public static AlertDialog processingDialog(Context context) {
        View dialogView = View.inflate(context, R.layout.dialog_processing, null);
        return new AlertDialog.Builder(context)
            .setView(dialogView)
            .create();
    }

    /** 创建一个文字对话框 */
    public static AlertDialog messageDialog(Context context, String msg) {
        return new AlertDialog.Builder(context)
            .setTitle(null)
            .setMessage(msg)
            .create();
    }

    /** 收起键盘 */
    public static void closeKeyboard(Context context, View view) {
        InputMethodManager manager = ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null && manager.isActive()) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static RecyclerView.ItemDecoration getGridDecoration(Context context, int column) {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int space = dp2px(context, 8);
                int spanCount = column;
                int position = parent.getChildAdapterPosition(view); // item position
                int column = (position - 1) % spanCount; // item的列数，除去第一项
                int count = Objects.requireNonNull(parent.getAdapter()).getItemCount(); //item总数
                if (position != 0 && position != count - 1) { //除去第一项和最后一项
                    outRect.left = space - column * space / spanCount;
                    outRect.right = (column + 1) * space / spanCount;
                    if (position < spanCount + 1) { // top edge
                        outRect.top = space;
                    }
                    outRect.bottom = space; // item bottom
                } else {
                    outRect.top = space;
                    outRect.left = space;
                    outRect.right = space;
                    if (position == count - 1) {
                        outRect.bottom = space; // item bottom
                    }
                }
            }
        };
    }

    public static RecyclerView.ItemDecoration getFilterDecoration(Context context) {
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                       @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    outRect.top = dp2px(context, 8); //添加顶部边距
                }
            }
        };
    }

    public static String numFormat(String text, String reg) {
        int i = Integer.parseInt(text);
        if (i > 99999999) {
            return new DecimalFormat("#.00").format(((double) i) / 100000000) + "亿" + reg;
        } else if (i > 9999) {
            return new DecimalFormat("#.00").format(((double) i) / 10000) + "万" + reg;
        } else {
            return i + reg;
        }
    }

}
