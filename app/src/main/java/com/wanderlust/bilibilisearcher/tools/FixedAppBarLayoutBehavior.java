package com.wanderlust.bilibilisearcher.tools;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * 修复AppBarLayout.Behavior的以下bug：
 * 1. 快速滑动RecycleView（惯性滑动）至最顶端或最低端后，立即去点击item往往没有反应，
 *    第二次点击或者等待片刻后点击可以生效。
 * 2. 缓慢滑动时，立即点击item，大多数情况下有反应。
 * 【使用】在AppBarLayout中使用（替换为）这个behavior
 */
public class FixedAppBarLayoutBehavior extends AppBarLayout.Behavior {

    public FixedAppBarLayoutBehavior() {
        super();
    }

    public FixedAppBarLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed,
            dxUnconsumed, dyUnconsumed, type);
        stopNestedScrollIfNeeded(dyUnconsumed, child, target, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child,
                                  View target, int dx, int dy, int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        stopNestedScrollIfNeeded(dy, child, target, type);
    }

    private void stopNestedScrollIfNeeded(int dy, AppBarLayout child, View target, int type) {
        if (type == ViewCompat.TYPE_NON_TOUCH) {
            final int currOffset = getTopAndBottomOffset();
            if ((dy < 0 && currOffset == 0)
                || (dy > 0 && currOffset == -child.getTotalScrollRange())) {
                ViewCompat.stopNestedScroll(target, ViewCompat.TYPE_NON_TOUCH);
            }
        }
    }

}
