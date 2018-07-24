package com.hy.library.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestOptions;
import com.hy.library.R;
import com.hy.library.utils.SizeUtils;
import com.snhccm.touch.TouchApp;
import com.snhccm.touch.utils.GlideLoader;

import java.util.ArrayList;

/**
 * Created time : 2018/7/18 16:09.
 *
 * @author HY
 */
public class MultiImageView extends LinearLayout {
    public MultiImageView(Context context) {
        this(context, null);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private LinearLayout mLayout3;

    private int imageSize;

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mLayout1 = new LinearLayout(context);
        mLayout2 = new LinearLayout(context);
        mLayout3 = new LinearLayout(context);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout1.setOrientation(HORIZONTAL);
        mLayout2.setOrientation(HORIZONTAL);
        mLayout3.setOrientation(HORIZONTAL);
        addView(mLayout1, layoutParams);
        addView(mLayout2, layoutParams);
        addView(mLayout3, layoutParams);

        int size = TouchApp.getScreenWidth() - SizeUtils.dp2px(context, 42);
        imageSize = size / 3;
    }

    private ArrayList<String> mImages = new ArrayList<>();

    public ArrayList<String> getData() {
        return mImages;
    }

    public void setData(ArrayList<String> images) {
        mImages.clear();
        mImages.addAll(images);
        mLayout1.removeAllViews();
        mLayout2.removeAllViews();
        mLayout3.removeAllViews();
        int size = images.size();

        if (size < 4) {
            mLayout1.setVisibility(VISIBLE);
            mLayout2.setVisibility(GONE);
            mLayout3.setVisibility(GONE);
        } else if (size < 7) {
            mLayout1.setVisibility(VISIBLE);
            mLayout2.setVisibility(VISIBLE);
            mLayout3.setVisibility(GONE);
        } else {
            mLayout1.setVisibility(VISIBLE);
            mLayout2.setVisibility(VISIBLE);
            mLayout3.setVisibility(VISIBLE);
        }


        for (int i = 0; i < mImages.size(); i++) {

            SquareLayout squareLayout = new SquareLayout(getContext());
            RoundImageView roundImageView = new RoundImageView(getContext());
            roundImageView.setType(RoundImageView.TYPE_ROUND);
            roundImageView.setBorderRadius(TouchApp.dp1() * 3);
            GlideLoader.load(getContext(), mImages.get(i))
                    .apply(new RequestOptions()
                            .error(R.drawable.icon_no_pic)
                            .placeholder(R.drawable.icon_no_pic))
                    .thumbnail(0.4f)
                    .into(roundImageView);

            int position = i;
            roundImageView.setOnClickListener(v -> {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onClick(position);
                }
            });
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            squareLayout.addView(roundImageView, layoutParams);

            LayoutParams layoutParams1 = new LayoutParams(imageSize, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i < 3) {
                if (i == 0) {
                    layoutParams1.leftMargin = 0;
                } else {
                    layoutParams1.leftMargin = TouchApp.dp1() * 5;
                }
                layoutParams1.topMargin = 0;
                mLayout1.addView(squareLayout, layoutParams1);
            } else if (i < 6) {
                if (i == 3) {
                    layoutParams1.leftMargin = 0;
                } else {
                    layoutParams1.leftMargin = TouchApp.dp1() * 5;
                }
                layoutParams1.topMargin = TouchApp.dp1() * 5;
                mLayout2.addView(squareLayout, layoutParams1);
            } else {
                if (i == 6) {
                    layoutParams1.leftMargin = 0;
                } else {
                    layoutParams1.leftMargin = TouchApp.dp1() * 5;
                }
                layoutParams1.topMargin = TouchApp.dp1() * 5;
                mLayout3.addView(squareLayout, layoutParams1);
            }
        }

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onClick(int position);
    }

}
