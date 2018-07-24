package com.hy.library;

import android.app.Application;
import android.util.DisplayMetrics;

import com.hy.library.service.InitializeService;
import com.hy.library.utils.SizeUtils;
import com.hy.library.utils.ToastWrapper;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created time : 2018/7/24 16:07.
 *
 * @author HY
 */
public abstract class BaseApp extends Application implements BaseAppDelegate {

    private static BaseApp sBaseApp;
    private int screenWidth;
    private int screenHeight;
    private int dp1;

    public static BaseApp getBaseApp() {
        return sBaseApp;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int dp1() {
        return dp1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sBaseApp = this;
        BGASwipeBackHelper.init(this, null);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        ToastWrapper.init(getApplicationContext());
        dp1 = SizeUtils.dp2px(this, 1);
        InitializeService.start(getApplicationContext());
    }

}
