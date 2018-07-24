package com.hy.library.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created time : 2018/5/24 14:40.
 *
 * @author HY
 */
public class LoadMoreRecyclerView extends RecyclerView {
    private boolean isLoadingMore = false;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);
        LayoutManager layoutManager = getLayoutManager();
        int spanCount;
        int lastVisibleItemPosition;
        if (layoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastVisibleItemPosition = last(into);
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else {
            spanCount = 1;
            lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }

        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(lastVisibleItemPosition, spanCount);
        }
        if (null != mOnLoadMoreListener) {
            Adapter adapter = getAdapter();

            if (dy > 0 && lastVisibleItemPosition >= adapter.getItemCount()-1 && !isLoadingMore) {
                mOnLoadMoreListener.onLoad();
                isLoadingMore = true;
            }
        }
    }
    private boolean canLoad=true;

    private OnScrollListener mOnScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public interface OnScrollListener {
        void onScroll(int lastVisibleItemPosition, int spanCount);
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoad();
    }

    public void loadComplete() {
        isLoadingMore = false;
    }

    //取到最后的一个节点
    private int last(int[] lastPositions) {
        if (lastPositions.length == 0) return 0;
        int max = lastPositions[0];
        for (int lastPosition : lastPositions) {
            if (max < lastPosition) max = lastPosition;
        }
        return max;
    }

}
