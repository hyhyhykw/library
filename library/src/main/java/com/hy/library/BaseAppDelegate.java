package com.hy.library;

import android.app.Activity;
import android.content.Context;

/**
 * Created time : 2018/7/24 15:37.
 *
 * @author HY
 */
public interface BaseAppDelegate {
    //
    boolean isLogin();

    Class<? extends Activity> getLoginActivity();

    //启动页
    Class<? extends Activity> getLaunchActivity();

    //
    Context getContext();

    //获取基础接口地址
    String getBaseUrl();

    //初始化
    void init();

    //Oss
    String getEndPoint();

    String getDomain();

    String getBucket();

    String getAliyunAccessKey();

    String getAliyunAccessKeySecret();

}
