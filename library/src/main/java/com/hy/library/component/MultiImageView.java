package com.hy.library.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestOptions;
import com.hy.library.BaseApp;
import com.hy.library.R;
import com.hy.library.utils.GlideLoader;
import com.hy.library.utils.SizeUtils;

import java.util.ArrayList;

/**
 * Created time : 2018/7/18 16:09.
 *
 * @author HY
 */
public class MultiImageView extends LinearLayout {

    private boolean isRound;
    private int borderRadius;
    private int mSpacing;

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
    private Drawable mDefaultImage;

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiImageView);
        mDefaultImage = a.getDrawable(R.styleable.MultiImageView_miv_default_image);
        if (null == mDefaultImage) {
            mDefaultImage = new ColorDrawable(Color.parseColor("#fff1f1f1"));
        }
        isRound = a.getBoolean(R.styleable.MultiImageView_miv_is_round, false);
        borderRadius = a.getDimensionPixelOffset(R.styleable.MultiImageView_miv_border_radius, BaseApp.getBaseApp().dp1() * 3);
        mSpacing = a.getDimensionPixelOffset(R.styleable.MultiImageView_miv_spacing, BaseApp.getBaseApp().dp1() * 5);

        a.recycle();

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

        int size = BaseApp.getBaseApp().getScreenWidth() - SizeUtils.dp2px(context, 42);
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
            if (isRound) {
                roundImageView.setType(RoundImageView.TYPE_ROUND);
                roundImageView.setBorderRadius(borderRadius);
            }
            GlideLoader.load(getContext(), mImages.get(i))
                    .apply(new RequestOptions()
                            .error(mDefaultImage)
                            .placeholder(mDefaultImage))
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
                    layoutParams1.leftMargin = mSpacing;
                }
                layoutParams1.topMargin = 0;
                mLayout1.addView(squareLayout, layoutParams1);
            } else if (i < 6) {
                if (i == 3) {
                    layoutParams1.leftMargin = 0;
                } else {
                    layoutParams1.leftMargin = mSpacing;
                }
                layoutParams1.topMargin = mSpacing;
                mLayout2.addView(squareLayout, layoutParams1);
            } else {
                if (i == 6) {
                    layoutParams1.leftMargin = 0;
                } else {
                    layoutParams1.leftMargin = mSpacing;
                }
                layoutParams1.topMargin = mSpacing;
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
