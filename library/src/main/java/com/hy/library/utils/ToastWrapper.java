package com.hy.library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.hy.library.BaseApp;

/**
 * Created time : 2017/12/26 11:10.
 *
 * @author HY
 */

public final class ToastWrapper {
    private static Toast sToast;
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        if (null != sToast) return;
        sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    private static void show(final String msg) {
        mainHandler.post(() -> {
            sToast.setText(msg);
            sToast.show();
        });
    }

    public static void show(String msg, Object... args) {
        String text = String.format(msg, args);
        show(text);
    }

    public static void show(@StringRes int msg, Object... args) {
        Context context = BaseApp.getBaseApp().getContext();
        String text = context.getString(msg, args);
        show(text);
    }
}
