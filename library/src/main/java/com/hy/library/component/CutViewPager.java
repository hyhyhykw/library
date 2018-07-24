package com.hy.library.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hy.library.utils.Logger;

import java.util.LinkedHashMap;

/**
 * Created time : 2018/6/1 8:26.
 * ScrollView 嵌套的ViewPager重新设置子View尺寸
 *
 * @author HY
 */
public class CutViewPager extends ViewPager {
    public CutViewPager(@NonNull Context context) {
        this(context, null);
    }

    public CutViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnPageChangeListener(listener);
    }

    /**
     * 保存position与对于的View
     */
    private LinkedHashMap<Integer, Integer> maps = new LinkedHashMap<>();
    private int current = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMeasure;
        int height = 0;
        // 下面遍历所有child的高度
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            // 采用最大的view的高度
            maps.put(i, h);
        }

        if (childCount > 0) {
            height = getChildAt(current).getMeasuredHeight();
        }
        heightMeasure = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasure);
    }

    public void resetHeight(int current) {

        this.current = current;

        if (maps.size() > current) {

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current));
            } else {
                layoutParams.height = maps.get(current);
            }
            setLayoutParams(layoutParams);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            removeOnPageChangeListener(listener);
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
        }

        super.onDetachedFromWindow();
    }

    private OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            resetHeight(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
