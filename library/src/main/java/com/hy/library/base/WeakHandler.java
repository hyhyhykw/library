package com.hy.library.base;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created time : 2018/2/13 15:11.
 *
 * @author HY
 */
public abstract class WeakHandler<T> extends Handler {

    protected WeakReference<T> mReference;

    protected WeakHandler(@NonNull T t) {
        mReference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (null == mReference) return;
        T t = mReference.get();

        if (null == t) return;
        handleMessage(msg, t);
    }

    protected abstract void handleMessage(Message msg, @NonNull T t);
}
