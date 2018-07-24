package com.hy.library.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created time : 2018/5/21 17:44.
 *
 * @author HY
 */
public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnMyScrollChangedListener {
        void onScrollChanged(int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }

    private OnMyScrollChangedListener mOnMyScrollChangedListener;

    public void setOnMyScrollChangedListener(OnMyScrollChangedListener onMyScrollChangedListener) {
        mOnMyScrollChangedListener = onMyScrollChangedListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnMyScrollChangedListener) {
            mOnMyScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }
}
