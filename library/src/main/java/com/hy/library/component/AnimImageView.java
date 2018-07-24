package com.hy.library.component;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created time : 2018/6/14 16:02.
 *
 * @author HY
 */
public class AnimImageView extends AppCompatImageView {
    AnimationDrawable mAnimationDrawable;
    boolean isAttach;

    public AnimImageView(Context context) {
        super(context);
    }

    public AnimImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        Drawable drawable = this.getDrawable();
        if (isAttach && mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }

        if (drawable instanceof AnimationDrawable) {
            mAnimationDrawable = (AnimationDrawable) drawable;
            if (isShown()) {
                mAnimationDrawable.start();
            }

        } else {
            mAnimationDrawable = null;
        }
    }

    private void switchStatus() {
        if (mAnimationDrawable != null) {
            if (!isShown()) {
                mAnimationDrawable.stop();
                return;
            }

            if (!mAnimationDrawable.isRunning()) {
                mAnimationDrawable.start();
            }
        }

    }

    public boolean isRunning() {
        return isAttach && mAnimationDrawable != null && mAnimationDrawable.isRunning();
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttach = true;
        switchStatus();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }

        isAttach = false;
    }

    protected void onVisibilityChanged(@NonNull View view, int visibility) {
        super.onVisibilityChanged(view, visibility);
        switchStatus();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        init();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        init();
    }
}
