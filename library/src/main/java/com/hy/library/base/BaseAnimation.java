package com.hy.library.base;

import android.animation.Animator;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created time : 2018/1/30 9:42.
 *
 * @author HY
 */
public interface BaseAnimation {
    @NonNull
    Animator[] getAnimators(@NonNull View view);
}
