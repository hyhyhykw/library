package com.hy.library.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.hy.library.BaseApp;
import com.hy.library.R;
import com.hy.library.base.WeakHandler;
import com.hy.library.utils.AppTool;
import com.hy.library.utils.PicassoLoader;
import com.hy.library.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created time : 2018/4/13 8:35.
 * 公用banner
 * <p>
 * 使用 图片指示器
 * <com.snhccm.touch.view.CommonCircleBanner
 * android:id="@+id/recommend_banner"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:layout_margin="@dimen/dimen_15"
 * android:background="@drawable/bg_banner"
 * app:ccb_banner_height="@dimen/dimen_100" banner的高度 不设置的话就是正方形的
 * app:ccb_indicator_bottom_margin="10dp" 指示器与底部的距离
 * app:ccb_indicator_drawable="@drawable/banner_indicator_normal" 未选中的指示器图片
 * app:ccb_indicator_margin="4dp" 指示器距离
 * app:ccb_indicator_size="5dp" 指示器大小
 * app:ccb_select_indicator_drawable="@drawable/banner_indicator_selected" 选中的指示器图片
 * />
 *
 * @author HY
 * 透明度指示器 设置此项后就不能使用图片指示器 颜色和透明度要一起用
 * @see R.styleable#CommonImageCircleBanner_ccb_indicator_alpha 未选中的透明度
 * @see R.styleable#CommonImageCircleBanner_ccb_indicator_color 指示器的颜色
 */
@SuppressWarnings("unused")
public class CommonImageCircleBanner extends FrameLayout {

    private final ArrayList<CircleImageView> dots = new ArrayList<>();

    private final ArrayList<ImageView> ivs = new ArrayList<>();

    private final ArrayList<BannerBeanWrapper> mData = new ArrayList<>();

    private final ScrollViewPager mVpgBanner;

    private final LinearLayout mLytIndicators;

    private float indicator_size;
    private Drawable indicator_drawable;
    private Drawable select_indicator_drawable;
    private final float select_alpha;
    private static final long DURATION = 4000;

    private static final Drawable DEFAULT_DRAWABLE = new ColorDrawable(Color.WHITE);
    private Timer mTimer;//定时器
    private TimerTask mCycleTask;//定时任务
    private boolean isAlpha = true;
    //触摸到的时间
    private long mResumeCycle;
    private final BannerHandler mHandler;
    private int indicator_margin;
    private NestedScrollView mNestedScrollView;
    private ScrollView mScrollView;
    private Drawable mDefaultImage;

    public CommonImageCircleBanner(@NonNull Context context) {
        this(context, null);
    }

    public CommonImageCircleBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private int type = -1;

    private int dp1;

    public CommonImageCircleBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHandler = new BannerHandler(this);
        setBackgroundColor(Color.WHITE);

        float DEFAULT_INDICATOR_SIZE = SizeUtils.dp2px(context, 5);
        dp1 = SizeUtils.dp2px(context, 1);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        mVpgBanner = new ScrollViewPager(context);
        mLytIndicators = new LinearLayout(context);
        mLytIndicators.setOrientation(LinearLayout.HORIZONTAL);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonImageCircleBanner, defStyleAttr, 0);
        int dimension = (int) ta.getDimension(R.styleable.CommonImageCircleBanner_ccb_banner_height, BaseApp.getBaseApp().getScreenWidth());

        LayoutParams vpgParams = new LayoutParams(LayoutParams.MATCH_PARENT, dimension);

        addView(mVpgBanner, vpgParams);

        int bottom_margin = ta.getDimensionPixelOffset(R.styleable.CommonImageCircleBanner_ccb_indicator_bottom_margin, 0);

        LayoutParams indicatorsContainerParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        indicatorsContainerParams.gravity = Gravity.BOTTOM;
        indicatorsContainerParams.bottomMargin = bottom_margin;
        addView(mLytIndicators, indicatorsContainerParams);

        indicator_drawable = DEFAULT_DRAWABLE;
        select_indicator_drawable = DEFAULT_DRAWABLE;

        int color = ta.getColor(R.styleable.CommonImageCircleBanner_ccb_indicator_color, Color.WHITE);
        indicator_drawable = new ColorDrawable(color);
        select_indicator_drawable = new ColorDrawable(color);
        select_alpha = ta.getFloat(R.styleable.CommonImageCircleBanner_ccb_indicator_alpha, 0);

        mDefaultImage = ta.getDrawable(R.styleable.CommonImageCircleBanner_ccb_default_image);
        if (null == mDefaultImage) {
            mDefaultImage = new ColorDrawable(Color.parseColor("#fff1f1f1"));
        }

        if (select_alpha == 0) {
            indicator_drawable = ta.getDrawable(R.styleable.CommonImageCircleBanner_ccb_indicator_drawable);
            if (indicator_drawable == null)
                throw new UnsupportedOperationException("You need set indicator alpha or indicator drawable");
            select_indicator_drawable = ta.getDrawable(R.styleable.CommonImageCircleBanner_ccb_select_indicator_drawable);
            if (select_indicator_drawable == null)
                throw new UnsupportedOperationException("You need set indicator alpha or selected indicator drawable");
            isAlpha = false;
        }
        indicator_size = ta.getDimension(R.styleable.CommonImageCircleBanner_ccb_indicator_size, DEFAULT_INDICATOR_SIZE);
        indicator_margin = ta.getDimensionPixelOffset(R.styleable.CommonImageCircleBanner_ccb_indicator_margin,
                SizeUtils.dp2px(context, 4));

        type = ta.getInt(R.styleable.CommonImageCircleBanner_ccb_image_type, -1);
        ta.recycle();
    }

    public void setData(Collection<? extends BannerBeanWrapper> data) {
        ArrayList<? extends BannerBeanWrapper> wrappers = new ArrayList<>(data);
        setData(wrappers);
    }

    public void setData(ArrayList<? extends BannerBeanWrapper> data) {
        mData.clear();
        mData.addAll(data);

        initData();

        BannerAdapter adapter = new BannerAdapter();
        mVpgBanner.setAdapter(adapter);
        mVpgBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (positionOffset == 0) {
                    if (position == 0) {
                        mVpgBanner.setCurrentItem(ivs.size() - 2, false);
                    } else if (position == (ivs.size() - 1)) {
                        mVpgBanner.setCurrentItem(1, false);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.size(); i++) {
                    if (isAlpha) {
                        if (position == 0) {
                            dots.get(i).setAlpha(i == dots.size() - 1 ? 1f : select_alpha);
                        } else if (position == ivs.size() - 1) {
                            dots.get(i).setAlpha(i == 0 ? 1f : select_alpha);
                        } else {
                            dots.get(i).setAlpha(i == position - 1 ? 1f : select_alpha);
                        }
                    } else {
                        if (position == 0) {
                            dots.get(i).setImageDrawable(i == dots.size() - 1 ? select_indicator_drawable : indicator_drawable);
                        } else if (position == ivs.size() - 1) {
                            dots.get(i).setImageDrawable(i == 0 ? select_indicator_drawable : indicator_drawable);
                        } else {
                            dots.get(i).setImageDrawable(i == position - 1 ? select_indicator_drawable : indicator_drawable);
                        }
                    }
                }
                if (null != mOnPageChangeListener) mOnPageChangeListener.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mVpgBanner.setCurrentItem(ivs.size() - 2, false);
        mVpgBanner.setVisibility(View.INVISIBLE);
        mVpgBanner.postDelayed(() -> {
            mVpgBanner.setVisibility(View.VISIBLE);
            // 设置初始 position
            mVpgBanner.setCurrentItem(1, false);
        }, 100);
    }


    public void setNestedScrollView(NestedScrollView nestedScrollView) {
        mNestedScrollView = nestedScrollView;
    }

    public void setScrollView(ScrollView scrollView) {
        mScrollView = scrollView;
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }

    private OnPageChangeListener mOnPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    private void initData() {
        dots.clear();
        mLytIndicators.removeAllViews();
        mLytIndicators.setGravity(Gravity.CENTER);
        int size = (int) indicator_size;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);

        ViewPager.LayoutParams imgParams = new ViewPager.LayoutParams();
        ImageView icon0 = new ImageView(getContext());
        icon0.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (type == 1 || type == 0) {
            icon0 = new RoundImageView(getContext());
            ((RoundImageView) icon0).setType(type);
            ((RoundImageView) icon0).setBorderRadius(dp1 * 3);
        }

        icon0.setLayoutParams(imgParams);
        ivs.add(icon0);

        for (int i = 0; i < mData.size(); i++) {
            if (i != 0) {
                params.leftMargin = indicator_margin;
            } else {
                params.leftMargin = 0;
            }
            CircleImageView iv = new CircleImageView(getContext());
            iv.setLayoutParams(params);

            if (isAlpha) {
                iv.setImageDrawable(indicator_drawable);

                iv.setAlpha(i == 0 ? 1f : select_alpha);

            } else {
                iv.setImageDrawable(i == 0 ? select_indicator_drawable : indicator_drawable);
            }

            dots.add(iv);
            mLytIndicators.addView(iv, params);

            ImageView icon = new ImageView(getContext());
            icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            if (type == 1 || type == 0) {
                icon = new RoundImageView(getContext());
                ((RoundImageView) icon).setType(type);
                ((RoundImageView) icon).setBorderRadius(dp1 * 3);
            }


            icon.setLayoutParams(imgParams);
            ivs.add(icon);
        }

        ImageView iconL = new ImageView(getContext());
        iconL.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (type == 1 || type == 0) {
            iconL = new RoundImageView(getContext());
            ((RoundImageView) iconL).setType(type);
            ((RoundImageView) iconL).setBorderRadius(dp1 * 3);
        }

        iconL.setLayoutParams(imgParams);
        ivs.add(iconL);
    }

    private class BannerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return ivs.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            ImageView imageView = ivs.get(position);
            if (position == 0) {
                BannerBeanWrapper beanWrapper = mData.get(mData.size() - 1);
                PicassoLoader
                        .load(beanWrapper.getBannerImage())
                        .placeholder(mDefaultImage)
                        .error(mDefaultImage)
                        .into(imageView);
            } else if (position == ivs.size() - 1) {
                BannerBeanWrapper beanWrapper = mData.get(0);
                PicassoLoader
                        .load(beanWrapper.getBannerImage())
                        .placeholder(mDefaultImage)
                        .error(mDefaultImage)
                        .into(imageView);
            } else {
                BannerBeanWrapper beanWrapper = mData.get(position - 1);
                PicassoLoader
                        .load(beanWrapper.getBannerImage())
                        .placeholder(mDefaultImage)
                        .error(mDefaultImage)
                        .into(imageView);

                AppTool.setViewClick(imageView, v -> {
                    if (null != mOnBannerItemClickListener)
                        mOnBannerItemClickListener.onItemClick(position - 1, beanWrapper);
                });
            }

            container.addView(imageView);

            return imageView;
        }
    }

    public ArrayList<String> getPictures() {
        ArrayList<String> list = new ArrayList<>();
        for (Object datum : mData) {
            list.add(datum.toString());
        }
        return list;
    }

    private OnBannerItemClickListener mOnBannerItemClickListener;

    public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
        mOnBannerItemClickListener = onBannerItemClickListener;
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position, BannerBeanWrapper beanWrapper);
    }


    private static class BannerHandler extends WeakHandler<CommonImageCircleBanner> {
        BannerHandler(@NonNull CommonImageCircleBanner commonBanner) {
            super(commonBanner);
        }

        @Override
        protected void handleMessage(Message msg, @NonNull CommonImageCircleBanner commonBanner) {
            //触摸时间不到4秒，不进行自动切换
            if (System.currentTimeMillis() < commonBanner.mResumeCycle) return;

            //切换到下一页
            commonBanner.moveToNextPosition();
        }
    }

    //切换到下一页
    private void moveToNextPosition() {
        //判断有没有设置适配器
        PagerAdapter adapter = mVpgBanner.getAdapter();
        if (null == adapter) return;
        //判断适配器中是否有数据
        int count = adapter.getCount();
        if (count == 0) return;

        int nextItem = (mVpgBanner.getCurrentItem() + 1) % count;

        mVpgBanner.setCurrentItem(nextItem);
        for (int i = 0; i < dots.size(); i++) {
            if (isAlpha) {
                if (nextItem == 0) {
                    dots.get(i).setAlpha(i == ivs.size() - 1 ? 1 : select_alpha);
                } else if (nextItem == ivs.size() - 1) {
                    dots.get(i).setAlpha(i == 0 ? 1 : select_alpha);
                } else {
                    dots.get(i).setAlpha(i == nextItem - 1 ? 1 : select_alpha);
                }
            } else {
                if (nextItem == 0) {
                    dots.get(i).setImageDrawable(i == ivs.size() - 1 ? select_indicator_drawable : indicator_drawable);
                } else if (nextItem == ivs.size() - 1) {
                    dots.get(i).setImageDrawable(i == 0 ? select_indicator_drawable : indicator_drawable);
                } else {
                    dots.get(i).setImageDrawable(i == nextItem - 1 ? select_indicator_drawable : indicator_drawable);
                }
            }
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //布局展示出来时调用的方法
        mTimer = new Timer();
        //定时的发送一些事件
        mCycleTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        };
        mTimer.schedule(mCycleTask, DURATION, DURATION);
    }

    //获取到触摸的时间
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mResumeCycle = System.currentTimeMillis() + DURATION;
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (null != mScrollView) {
                mScrollView.requestDisallowInterceptTouchEvent(false);
                ViewParent parent = mScrollView.getParent();
                if (null != parent) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            }
            if (null != mNestedScrollView) {
                mNestedScrollView.requestDisallowInterceptTouchEvent(false);
                ViewParent parent = mNestedScrollView.getParent();
                if (null != parent) {
                    parent.requestDisallowInterceptTouchEvent(false);
                }
            }
        } else {
            if (null != mScrollView) {
                mScrollView.requestDisallowInterceptTouchEvent(true);
                ViewParent parent = mScrollView.getParent();
                if (null != parent) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            if (null != mNestedScrollView) {
                mNestedScrollView.requestDisallowInterceptTouchEvent(true);
                ViewParent parent = mNestedScrollView.getParent();
                if (null != parent) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }

        }
        return false;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    //布局从屏幕上消失的时候调用
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //取消开启的及时任务
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        if (null != mCycleTask) {
            mCycleTask.cancel();
            mCycleTask = null;
        }
    }

}
