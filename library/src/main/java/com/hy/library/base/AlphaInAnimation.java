package com.hy.library.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created time : 2018/1/30 9:44.
 *
 * @author HY
 */
public class AlphaInAnimation implements BaseAnimation {
    @NonNull
    @Override
    public Animator[] getAnimators(@NonNull View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);

        return new Animator[]{
            set
        };
    }
}
