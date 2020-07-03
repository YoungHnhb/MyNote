package com.liyy.mynote;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.ViewCompat;

/**
 * Author: 李岳阳
 * Date: 2020/7/2
 * Time: 16:14
 * Description：
 */
public class NestedLayout extends FrameLayout implements NestedScrollingParent3 {

    public NestedLayout(@NonNull Context context) {
        super(context);
    }

    public NestedLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NestedLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        Log.e("TTT", "onNestedScroll: " + target);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("TTT", "onStartNestedScroll: " + target);
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes, int type) {
        Log.e("TTT", "onNestedScrollAccepted: " + target);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        Log.e("TTT", "onStopNestedScroll: " + target);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        Log.e("TTT", "onNestedScroll: " + target);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.e("TTT", "onNestedPreScroll: " + target);
    }

}
