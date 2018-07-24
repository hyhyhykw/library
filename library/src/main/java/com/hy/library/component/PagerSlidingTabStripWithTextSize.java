package com.hy.library.component;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hy.library.R;
import com.hy.library.utils.SizeUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created time : 2018/6/5 11:56.
 * 指示器宽度与文字宽度相等的TabLayout
 *
 * @author HY
 * @see R.styleable#PagerSlidingTabStripWithTextSize_shouldExpand 设置tab是否占满
 * @see R.styleable#PagerSlidingTabStripWithTextSize_indicatorColor 设置指示器颜色
 * @see R.styleable#PagerSlidingTabStripWithTextSize_indicatorHeight 设置指示器高度
 * @see R.styleable#PagerSlidingTabStripWithTextSize_indicatorRadius 设置指示器圆角
 * @see R.styleable#PagerSlidingTabStripWithTextSize_lineBottomPadding 设置指示器与底部的距离
 * @see R.styleable#PagerSlidingTabStripWithTextSize_tabTextSize 设置文字大小
 * @see R.styleable#PagerSlidingTabStripWithTextSize_tabTextColor 设置文字颜色
 * @see R.styleable#PagerSlidingTabStripWithTextSize_tabTextSelectedColor 设置选中的文字颜色
 */
public class PagerSlidingTabStripWithTextSize extends HorizontalScrollView {
    private int underLineHeight;
    private int dividerPadding;
    private int tabPaddingLeftRight;
    private int indicatorWidth;
    private int textSize;
    private int textColor;
    private int selectedTextColor;
    private boolean isSelectScale;
    private boolean tabSingleLine;
    private int lastScrollX;
    private int tabBackground;
    private int tabSelectedBackground;
    private int lineBottomPadding;
    private Rect textRect;
    private TabClickListener mTabClickListener;
    public OnPageChangeListener pageListener;
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private final TabPageChangeListener mTabPageChangeListener;
    private LinearLayout tabContainer;
    private ViewPager viewPager;
    private int tabCount;
    private int pagerPosition;
    private int selectedPosition;
    private float pagerPositionOffset;
    private Paint mIndicatorPaint;
    private boolean n;
    private boolean isHasRadius;
    private float indicatorRadius;
    private RectF indicatorRectF;
    private float lineIndicatorRadius;
    private int indicatorColor;
    private int underlineColor;
    private boolean shouldExpand;
    private boolean isTextAllCaps;
    private int scrollOffset;
    private int indicatorPadding;
    private int indicatorHeight;
    private static final int DEFAULT_SELECT_COLOR = 0xFF666666;
    private static final int DEFAULT_INDICATOR_COLOR = 0xFF81D4FA;
    private static final int DEFAULT_UNDERLINE_COLOR = 0x1A000000;
    private boolean isRedDotMode;

    public PagerSlidingTabStripWithTextSize(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStripWithTextSize(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStripWithTextSize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTabPageChangeListener = new TabPageChangeListener();
        pagerPosition = 0;
        selectedPosition = 0;
        pagerPositionOffset = 0F;
        this.n = false;
        indicatorRectF = new RectF();
        indicatorColor = DEFAULT_INDICATOR_COLOR;
        underlineColor = DEFAULT_UNDERLINE_COLOR;
        scrollOffset = 52;
        indicatorPadding = 8;
        indicatorHeight = 8;
        underLineHeight = 2;
        dividerPadding = 12;
        tabPaddingLeftRight = 24;
        indicatorWidth = 0;
        isSelectScale = false;
        tabSingleLine = true;
        lastScrollX = 0;
        tabBackground = R.drawable.background_tabs;
        lineBottomPadding = 0;
        textRect = new Rect();
        setFillViewport(true);
        setWillNotDraw(false);
        tabContainer = new LinearLayout(context);
        tabContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabContainer.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(tabContainer);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(1, scrollOffset, displayMetrics);
        indicatorHeight = (int) TypedValue.applyDimension(1, indicatorHeight, displayMetrics);
        underLineHeight = (int) TypedValue.applyDimension(1, underLineHeight, displayMetrics);
        dividerPadding = (int) TypedValue.applyDimension(1, dividerPadding, displayMetrics);
        tabPaddingLeftRight = (int) TypedValue.applyDimension(1, tabPaddingLeftRight, displayMetrics);
        textSize = SizeUtils.sp2px(context, 14);

        TypedArray tabTypedArray = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStripWithTextSize);

        textSize = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_tabTextSize, textSize);
        textColor = tabTypedArray.getColor(R.styleable.PagerSlidingTabStripWithTextSize_tabTextColor, textColor);
        selectedTextColor = tabTypedArray.getColor(R.styleable.PagerSlidingTabStripWithTextSize_tabTextSelectedColor, DEFAULT_SELECT_COLOR);

        indicatorColor = tabTypedArray.getColor(R.styleable.PagerSlidingTabStripWithTextSize_indicatorColor, indicatorColor);
        underlineColor = tabTypedArray.getColor(R.styleable.PagerSlidingTabStripWithTextSize_underlineColor, underlineColor);
        indicatorHeight = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_indicatorHeight, indicatorHeight);
        underLineHeight = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_underlineHeight, underLineHeight);
        lineBottomPadding = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_lineBottomPadding, 0);

        dividerPadding = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_dividerPadding2, dividerPadding);
        tabPaddingLeftRight = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_tabPaddingLeftRight, tabPaddingLeftRight);
        tabBackground = tabTypedArray.getResourceId(R.styleable.PagerSlidingTabStripWithTextSize_tabBackgroundStrip, tabBackground);
        shouldExpand = tabTypedArray.getBoolean(R.styleable.PagerSlidingTabStripWithTextSize_shouldExpand, false);
        scrollOffset = tabTypedArray.getDimensionPixelSize(R.styleable.PagerSlidingTabStripWithTextSize_scrollOffset, scrollOffset);
        isTextAllCaps = tabTypedArray.getBoolean(R.styleable.PagerSlidingTabStripWithTextSize_textAllCaps2, true);

        indicatorRadius = tabTypedArray.getDimension(R.styleable.PagerSlidingTabStripWithTextSize_indicatorRadius, 0);
        isHasRadius = indicatorRadius > 0;

        tabTypedArray.recycle();
        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
        mIndicatorPaint.setStyle(Style.FILL);
        defaultTabLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

    }

    private void scrollToChild(int position, int offset) {
        if (tabCount != 0) {
            int newScrollX = offset + tabContainer.getChildAt(position).getLeft();
            if (position > 0 || offset > 0) {
                newScrollX -= scrollOffset;
            }

            if (newScrollX != lastScrollX) {
                lastScrollX = newScrollX;
                scrollTo(newScrollX, 0);
            }
        }

    }

    private void addTextTab(int position, CharSequence title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setFocusable(true);
        tab.setGravity(Gravity.CENTER);

        if (tabSingleLine) {
            tab.setSingleLine();
        }

        tab.setTag(position);
        tab.setOnClickListener(v -> {
            if (mTabClickListener != null) {
                mTabClickListener.onTabClick(position, v);
            }
            viewPager.setCurrentItem(position);
        });
        tabContainer.addView(tab);
    }

    private void addTab(int position, String tabText, int rightDrawable) {
        TextView tabView;
        if (isRedDotMode) {
            tabView = new CenterIconTextView(getContext());
            tabView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_shape_alpha_red_dot, 0, rightDrawable, 0);
            Drawable[] drawables = tabView.getCompoundDrawables();
            if (drawables[0] != null) {
                drawables[0].setAlpha(0);
            }
        } else {
            tabView = new RightIconTextView(this.getContext());
            tabView.setCompoundDrawablesWithIntrinsicBounds(0, 0, rightDrawable, 0);
        }

        tabView.setFocusable(true);
        tabView.setText(tabText);
        tabView.setCompoundDrawablePadding(0);
        tabView.setGravity(Gravity.CENTER);
        if (tabSingleLine) {
            tabView.setSingleLine();
        }

        tabView.setTag(position);
        tabView.setOnClickListener(v -> {

            if (mTabClickListener != null) {
                mTabClickListener.onTabClick(position, v);
            }

            viewPager.setCurrentItem(position);
        });
        tabContainer.addView(tabView);
    }

    private void updateTabStyles() {
        for (int i = 0; i < tabCount; ++i) {
            View child = tabContainer.getChildAt(i);
            child.setBackgroundResource(tabBackground);

            if (shouldExpand) {
                child.setLayoutParams(expandedTabLayoutParams);
                child.setPadding(0, 0, 0, 0);
            } else {
                child.setLayoutParams(defaultTabLayoutParams);
                child.setPadding(tabPaddingLeftRight, 0, tabPaddingLeftRight, 0);
            }

            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                textView.setTypeface(null, Typeface.NORMAL);
                textView.setTextColor(textColor);
                if (isTextAllCaps) {
                    textView.setAllCaps(true);
                }

                if (i == selectedPosition) {
                    textView.setTextColor(selectedTextColor);
                    child.setBackgroundResource(tabSelectedBackground);
                    if (isSelectScale) {
                        ViewHelper.setScaleX(textView, 1.15F);
                        ViewHelper.setScaleY(textView, 1.15F);
                    }
                }
            }
        }

    }

    private void selectTab() {
        for (int i = 0; i < tabCount; ++i) {
            View child = tabContainer.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                if (i == selectedPosition) {
                    textView.setTextColor(selectedTextColor);
                    textView.setBackgroundResource(tabSelectedBackground);
                    if (isSelectScale) {
                        ViewHelper.setScaleX(textView, 1.15F);
                        ViewHelper.setScaleY(textView, 1.15F);
                    }
                } else {
                    textView.setTextColor(textColor);
                    textView.setBackgroundResource(tabBackground);
                    if (isSelectScale) {
                        ViewHelper.setScaleX(textView, 1.15F);
                        ViewHelper.setScaleY(textView, 1.15F);
                    }
                }
            }
        }

    }

    public void notifyDataSetChanged() {
        tabContainer.removeAllViews();
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        tabCount = adapter.getCount();

        for (int i = 0; i < tabCount; ++i) {
            if (adapter instanceof IconTabProvider && ((IconTabProvider) adapter).getPageIconResId(i) != 0) {
                addTab(i, String.valueOf(adapter.getPageTitle(i)), ((IconTabProvider) adapter).getPageIconResId(i));
            } else {
                addTextTab(i, adapter.getPageTitle(i));
            }
        }

        updateTabStyles();
        n = false;
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                if (VERSION.SDK_INT < 16) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                pagerPosition = viewPager.getCurrentItem();
                scrollToChild(pagerPosition, 0);
            }
        });
    }


    public int getDividerPadding() {
        return dividerPadding;
    }

    public int getIndicatorColor() {
        return indicatorColor;
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public int getTabBackground() {
        return tabBackground;
    }

    public int getTabPaddingLeftRight() {
        return tabPaddingLeftRight;
    }

    public LinearLayout getTabsContainer() {
        return tabContainer;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public int getUnderlineHeight() {
        return underLineHeight;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }

        int height = getHeight();
        mIndicatorPaint.setColor(indicatorColor);
        TextView currentTab = (TextView) tabContainer.getChildAt(pagerPosition);
        if (currentTab != null) {
            float currentLeft = currentTab.getLeft() + getPaddingLeft();
            float currentRight = currentLeft + currentTab.getWidth();
            currentTab.getPaint().getTextBounds(currentTab.getText().toString(), 0, currentTab.getText().length(), textRect);
            float tabLeftRightPadding = (currentRight - currentLeft - textRect.width()) / 2F;
            float currentTextLeft = currentLeft + tabLeftRightPadding;
            float currentTextRight = currentRight - tabLeftRightPadding;
            float tabMoveRight;
            float tabMoveLeft;
            if (pagerPositionOffset > 0F && pagerPosition < -1 + tabCount) {
                TextView nextTab = (TextView) tabContainer.getChildAt(1 + pagerPosition);
                float nextLeft = nextTab.getLeft() + getPaddingLeft();
                float nextRight = nextLeft + nextTab.getWidth();
                nextTab.getPaint().getTextBounds(nextTab.getText().toString(), 0, nextTab.getText().length(), textRect);
                float nextLeftRightPadding = (nextRight - nextLeft - textRect.width()) / 2F;
                float nextTextLeft = nextLeft + nextLeftRightPadding;
                float nextTextRight = nextRight - nextLeftRightPadding;
                float var25 = nextTextLeft * pagerPositionOffset + currentTextLeft * (1F - pagerPositionOffset);
                tabMoveRight = nextTextRight * pagerPositionOffset + currentTextRight * (1F - pagerPositionOffset);
                tabMoveLeft = var25;
            } else {
                tabMoveRight = currentTextRight;
                tabMoveLeft = currentTextLeft;
            }

            float right;
            float left;
            if (indicatorWidth > 0) {
                float var16 = tabMoveRight - tabMoveLeft;
                float var17 = ((float) indicatorWidth - var16) / 2F;
                float var18 = tabMoveLeft - var17;
                right = tabMoveRight + var17;
                left = var18;
            } else {
                right = tabMoveRight;
                left = tabMoveLeft;
            }

            if (isHasRadius) {
                indicatorRectF.set(left - indicatorPadding,
                        height - indicatorHeight - lineBottomPadding,
                        right + indicatorPadding,
                        height - lineBottomPadding);
                canvas.drawRoundRect(indicatorRectF, indicatorRadius, indicatorRadius, mIndicatorPaint);
            } else if (lineIndicatorRadius > 0F) {
                indicatorRectF.set(left - indicatorPadding,
                        height - indicatorHeight - lineBottomPadding,
                        right + indicatorPadding,
                        height - lineBottomPadding);
                canvas.drawRoundRect(indicatorRectF, lineIndicatorRadius, lineIndicatorRadius, mIndicatorPaint);
            } else {
                canvas.drawRect(left - indicatorPadding,
                        height - indicatorHeight - lineBottomPadding,
                        right + indicatorPadding,
                        height - lineBottomPadding, mIndicatorPaint);
            }

            float top = height - indicatorHeight - lineBottomPadding;
            float bottom = height - lineBottomPadding;
            canvas.drawRect(left, top, right, bottom, mIndicatorPaint);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (shouldExpand && MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            int measuredWidth = getMeasuredWidth();
            int tabIndex = 0;

            int tabSumWidth;
            for (tabSumWidth = 0; tabIndex < tabCount; ++tabIndex) {
                tabSumWidth += tabContainer.getChildAt(tabIndex).getMeasuredWidth();
            }

            if (!this.n && tabSumWidth > 0 && measuredWidth > 0) {
                int index = 0;
                if (tabSumWidth <= measuredWidth) {
                    while (index < tabCount) {
                        tabContainer.getChildAt(index).setLayoutParams(expandedTabLayoutParams);
                        ++index;
                    }
                }

                this.n = true;
            }
        }

    }

    public void onRestoreInstanceState(Parcelable var1) {
        PagerSlidingTabStripWithTextSize.SavedState savedState = (PagerSlidingTabStripWithTextSize.SavedState) var1;
        super.onRestoreInstanceState(savedState.getSuperState());
        pagerPosition = savedState.pagerPosition;
        requestLayout();
    }

    public Parcelable onSaveInstanceState() {
        PagerSlidingTabStripWithTextSize.SavedState savedState = new PagerSlidingTabStripWithTextSize.SavedState(super.onSaveInstanceState());
        savedState.pagerPosition = pagerPosition;
        return savedState;
    }

    public void setAllCaps(boolean isTextAllCaps) {
        this.isTextAllCaps = isTextAllCaps;
    }

    public void setDividerPadding(int dividerPadding) {
        this.dividerPadding = dividerPadding;
        invalidate();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int colorRes) {
        indicatorColor = getResources().getColor(colorRes);
        invalidate();
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        invalidate();
    }

    public void setIndicatorPadding(int indicatorPadding) {
        this.indicatorPadding = indicatorPadding;
        invalidate();
    }

    public void setIndicatorRadius(int indicatorRadius) {
        this.indicatorRadius = indicatorRadius;
        isHasRadius = indicatorRadius > 0;
        invalidate();
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public void setIsRedDotMode(boolean isRedDotMode) {
        this.isRedDotMode = isRedDotMode;
    }

    public void setIsSelectScale(boolean isSelectScale) {
        this.isSelectScale = isSelectScale;
    }

    public void setLineBottomPadding(int lineBottomPadding) {
        this.lineBottomPadding = lineBottomPadding;
        updateTabStyles();
    }

    public void setLineIndicatorRadius(int lineIndicatorRadius) {
        this.lineIndicatorRadius = lineIndicatorRadius;
    }

    public void setOnPageChangeListener(OnPageChangeListener pageChangeListener) {
        pageListener = pageChangeListener;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
        invalidate();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        invalidate();
    }

    public void setSelectedTextColor(int selectedTextColor) {
        this.selectedTextColor = selectedTextColor;
        updateTabStyles();
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public void setTabBackground(int var1) {
        tabBackground = var1;
    }

    public void setTabClickListener(TabClickListener tabClickListener) {
        mTabClickListener = tabClickListener;
    }

    public void setTabPaddingLeftRight(int tabPaddingLeftRight) {
        this.tabPaddingLeftRight = tabPaddingLeftRight;
        updateTabStyles();
    }

    public void setTabSelectedBackground(int tabSelectedBackground) {
        this.tabSelectedBackground = tabSelectedBackground;
    }

    public void setTabSingleLine(boolean tabSingleLine) {
        this.tabSingleLine = tabSingleLine;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int var1) {
        textColor = this.getResources().getColor(var1);
        updateTabStyles();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        updateTabStyles();
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(@ColorRes int colorRes) {
        underlineColor = getResources().getColor(colorRes);
        invalidate();
    }

    public void setUnderlineHeight(int underLineHeight) {
        this.underLineHeight = underLineHeight;
        invalidate();
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        } else {
            viewPager.addOnPageChangeListener(mTabPageChangeListener);
            notifyDataSetChanged();
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        int pagerPosition;

        private SavedState(Parcel source) {
            super(source);
            pagerPosition = source.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(pagerPosition);
        }
    }

    public interface IconTabProvider {
        int getPageIconResId(int index);
    }

    public interface TabClickListener {
        void onTabClick(int position, View view);
    }

    private class TabPageChangeListener implements OnPageChangeListener {
        private TabPageChangeListener() {
        }

        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(viewPager.getCurrentItem(), 0);
            }

            if (pageListener != null) {
                pageListener.onPageScrollStateChanged(state);
            }

        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            pagerPosition = position;
            pagerPositionOffset = positionOffset;
            if (tabContainer.getChildAt(position) != null) {
                scrollToChild(position, (int) (positionOffset * (float) tabContainer.getChildAt(position).getWidth()));
            }

            invalidate();
            if (pageListener != null) {
                pageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        public void onPageSelected(int position) {

            selectedPosition = position;
            selectTab();
            if (pageListener != null) {
                pageListener.onPageSelected(position);
            }

        }
    }
}
