package com.hy.library.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.hy.library.R;
import com.hy.library.utils.Logger;


/**
 * Created time : 2018/4/13 11:41.
 *
 * @author HY
 * @ explain:这个 ViewPager是用来解决ScrollView里面嵌套ViewPager的 内部解决法的
 */
public class ScrollViewPager extends ViewPager {
    public ScrollViewPager(@NonNull Context context) {
        super(context);
    }

    private boolean isSquare = false;

    public ScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollViewPager);

        isSquare = a.getBoolean(R.styleable.ScrollViewPager_isSquare, false);

        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (isSquare) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, width);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    int lastX = -1;
    int lastY = -1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dealtX = 0;
                dealtY = 0;
                // 保证子View能够接收到Action_move事件
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX += Math.abs(x - lastX);
                dealtY += Math.abs(y - lastY);
                Logger.i("dealtX:=" + dealtX);
                Logger.i("dealtY:=" + dealtY);
                // 这里是否拦截的判断依据是左右滑动，读者可根据自己的逻辑进行是否拦截
                if (dealtX >= dealtY) { // 左右滑动请求父 View 不要拦截
//                    getParent().requestDisallowInterceptTouchEvent(true);
                    ViewParent parent = getParent();
                    while (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                        parent = parent.getParent();
                    }
                } else {
//                    getParent().requestDisallowInterceptTouchEvent(false);
                    ViewParent parent = getParent();
                    while (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(false);
                        parent = parent.getParent();
                    }
                }
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return super.dispatchTouchEvent(ev);
    }
}