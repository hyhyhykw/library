package com.hy.library.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.hy.library.R;

/**
 * Created time : 2018/4/4 13:32.
 *
 * @author HY
 */
public class MyViewPager extends ViewPager {

    public MyViewPager(@NonNull Context context) {
        this(context, null);
    }

    public MyViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyViewPager);

        isCanScroll = a.getBoolean(R.styleable.MyViewPager_isCanScroll, true);

        a.recycle();
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

    public void setCurrentItemSuper(int item) {
        super.setCurrentItem(item);
    }

    /**
     * 设置其是否能滑动换页
     */
    private boolean isCanScroll = true;

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isCanScroll && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return (isCanScroll && super.onTouchEvent(ev)) || performClick();
    }

    @Override
    public boolean performClick() {
        return super.performClick() && isCanScroll;
    }

}
