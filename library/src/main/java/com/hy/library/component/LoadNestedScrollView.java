package com.hy.library.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created time : 2018/5/24 14:56.
 *
 * @author HY
 */
public class LoadNestedScrollView extends NestedScrollView {
    private boolean isLoadMordData;

    public LoadNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public LoadNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadComplete() {
        isLoadMordData = false;
    }


    /**
     * 以屏幕的左上角为（0,0）点，
     *
     * @param l    l表示滑动后的x值，
     * @param t    t表示滑动后的y值，
     * @param oldl oldl表示滑动前的x位置,
     * @param oldt oldt表示滑动前的y位置。
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        View view = getChildAt(getChildCount() - 1);
        if (null == mOnLoadMoreListener) return;
        if (null == view) return;

        int d = view.getBottom();
        d -= getHeight() + getScrollY();
        if (d <= 300) {
            //you are at the end of the list in scrollview
            //do what you wanna do here
            if (!isLoadMordData) {
                isLoadMordData = true;
                mOnLoadMoreListener.onLoad();
            }
        }

    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }
}
