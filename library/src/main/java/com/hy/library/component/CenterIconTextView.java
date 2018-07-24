package com.hy.library.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CenterIconTextView extends AppCompatTextView {
    public CenterIconTextView(Context context) {
        super(context);
    }

    public CenterIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[0];
        float width = getPaint().measureText(getText().toString())
                + (2 * drawable.getIntrinsicWidth()) + getCompoundDrawablePadding() + getPaddingLeft() + getPaddingRight();
        setPadding((int) (getPaddingLeft() + (getMeasuredWidth() - width) / 2F),
                getPaddingTop(),
                (int) (getPaddingRight() + (getMeasuredWidth() - width) / 2F),
                getPaddingBottom());
    }
}