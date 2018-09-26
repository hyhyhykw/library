package com.hy.library;

import android.app.Activity;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.hy.library.service.InitializeService;
import com.hy.library.utils.SizeUtils;
import com.hy.library.utils.ToastWrapper;
import com.hy.picker.PhotoModule;
import com.hy.picker.PhotoPicker;

import java.util.LinkedList;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created time : 2018/7/24 16:07.
 *
 * @author HY
 */
public abstract class BaseApp extends MultiDexApplication implements BaseAppDelegate, PhotoModule {

    private static BaseApp sBaseApp;
    private int screenWidth;
    private int screenHeight;
    private int dp1;
    //运用list来保存们每一个activity是关键
    private LinkedList<Activity> mList = new LinkedList<>();

    public static BaseApp getBaseApp() {
        return sBaseApp;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void removeActivity(Activity activity) {
        mList.remove(activity);
    }

    public Activity getCurrentActivity() {
        if (mList.isEmpty()) return null;
        return mList.getLast();
    }

    @Override
    public void onTerminate() {
        mList.clear();
        super.onTerminate();
    }

    public int dp1() {
        return dp1;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PhotoPicker.init(this);
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
