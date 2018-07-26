package com.hy.library.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hy.library.BaseApp;
import com.hy.library.R;
import com.hy.library.utils.PicassoLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    private int mMaxHeight;

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
    private DisplayImageOptions displayImageOptions;

    public MultiImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiImageView);
        mDefaultImage = a.getDrawable(R.styleable.MultiImageView_miv_default_image);
        if (null == mDefaultImage) {
            mDefaultImage = ContextCompat.getDrawable(context, R.drawable.icon_error_default);

        }
        isRound = a.getBoolean(R.styleable.MultiImageView_miv_is_round, false);
        borderRadius = a.getDimensionPixelOffset(R.styleable.MultiImageView_miv_border_radius, BaseApp.getBaseApp().dp1() * 3);
        mSpacing = a.getDimensionPixelOffset(R.styleable.MultiImageView_miv_spacing, BaseApp.getBaseApp().dp1() * 5);
        mMaxHeight = a.getDimensionPixelOffset(R.styleable.MultiImageView_miv_max_height, BaseApp.getBaseApp().dp1() * 212);

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
        imageSize = (int) ((getRight() - getLeft() - mSpacing * 2f) / 3);
    }


    //    private boolean mIsFirst = true;
//    private int mTotalWidth;
//
//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        mTotalWidth = right - left;
//        imageSize = (int) ((mTotalWidth - mSpacing * 2f) / 3);
//        super.onLayout(changed, left, top, right, bottom);
//    }

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

        if (size == 1) {
            mLayout1.setVisibility(VISIBLE);
            mLayout2.setVisibility(GONE);
            mLayout3.setVisibility(GONE);

            RoundImageView roundImageView = new RoundImageView(getContext());
//            if (isRound) {
//                roundImageView.setType(RoundImageView.TYPE_ROUND);
//                roundImageView.setBorderRadius(borderRadius);
//            }
            if (displayImageOptions == null) {
                displayImageOptions = new DisplayImageOptions.Builder()
                        .cacheOnDisk(false)
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.ARGB_8888)
                        .showImageOnFail(mDefaultImage)
                        .showImageOnLoading(mDefaultImage)
                        .showImageForEmptyUri(mDefaultImage)
                        .build();
            }
            LayoutParams layoutParams1 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mLayout1.addView(roundImageView, layoutParams1);

            roundImageView.setOnClickListener(v -> {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.onClick(0);
                }
            });

            ImageLoader.getInstance().displayImage(mImages.get(0), roundImageView, displayImageOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    int w = bitmap.getWidth();
                    int h = bitmap.getHeight();

                    int oneHeight;
                    int oneWidth;
                    if (h > mMaxHeight) {
                        oneHeight = mMaxHeight;
                        oneWidth = (int) (w * (mMaxHeight * 1.0f / h));
                        if (oneWidth <= 0) {
                            oneWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
                        }
                    } else {
                        oneWidth = w;
                        if (oneWidth > BaseApp.getBaseApp().getScreenWidth() * 2 / 3f) {
                            oneWidth = (int) (BaseApp.getBaseApp().getScreenWidth() * 2 / 3f);
                        }

                        oneHeight = (int) (h * (oneWidth * 1f / w));
                    }
                    ViewGroup.LayoutParams layoutParams = roundImageView.getLayoutParams();
                    layoutParams.height = oneHeight;
                    layoutParams.width = oneWidth;
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });


        } else {
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

            for (int i = 0; i < size; i++) {

                SquareLayout squareLayout = new SquareLayout(getContext());
                RoundImageView roundImageView = new RoundImageView(getContext());
                if (isRound) {
                    roundImageView.setType(RoundImageView.TYPE_ROUND);
                    roundImageView.setBorderRadius(borderRadius);
                }
                PicassoLoader.load(mImages.get(i))
                        .error(mDefaultImage)
                        .placeholder(mDefaultImage)
                        .resize(imageSize, imageSize)
                        .into(roundImageView);
//                GlideLoader.load(getContext(), mImages.get(i))
//                        .apply(new RequestOptions()
//                                .error(mDefaultImage)
//                                .placeholder(mDefaultImage))
//                        .thumbnail(0.4f)
//                        .into(roundImageView);
                roundImageView.setTag(i);
                roundImageView.setOnClickListener(v -> {
                    if (null != mOnItemClickListener) {
                        int position = (int) roundImageView.getTag();
                        mOnItemClickListener.onClick(position);
                    }
                });
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                squareLayout.addView(roundImageView, layoutParams);

                LayoutParams layoutParams1 = new LayoutParams(imageSize, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams1.weight = 1;

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


    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener;


    public interface OnItemClickListener {
        void onClick(int position);
    }

}
