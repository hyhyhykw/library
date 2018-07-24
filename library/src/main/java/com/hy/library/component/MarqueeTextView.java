package com.hy.library.component;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;


/**
 * Created time : 2018/5/25 9:54.
 * 走马灯
 *
 * @author HY
 */
public class MarqueeTextView extends AppCompatTextView {


    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isFocused() {
        return true;
    }
}