package com.hy.library.base;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hy.library.BaseApp;
import com.hy.library.R;
import com.hy.library.utils.AppTool;
import com.hy.library.utils.DefaultRationale;
import com.hy.library.utils.Logger;
import com.hy.library.utils.PermissionSetting;
import com.hy.library.utils.ToastWrapper;
import com.hy.picker.utils.AttrsUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Created time : 2018/8/16 16:22.
 *
 * @author HY
 */
public abstract class CommonBaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {
    protected BGASwipeBackHelper mSwipeBackHelper;
    private static final String BUNDLE_KEY = "bundle";
    private static final Handler myHandler = new Handler(Looper.getMainLooper());

    protected static final int REQUEST_STORAGE = 123;
    protected static final int REQUEST_CAMERA = 345;
    private Rationale mRationale;
    private PermissionSetting mSetting;

    protected final void requestStorage() {
        requestPermission(REQUEST_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected final void requestCamera() {
        requestPermission(REQUEST_CAMERA, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    protected final void requestPermission(int requestCode, String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(mRationale)
                .onGranted(permission -> {
                    //权限请求成功
                    onSucceed(requestCode);
                })
                .onDenied(permission -> {
                    ToastWrapper.show(R.string.failure);
                    if (AndPermission.hasAlwaysDeniedPermission(CommonBaseActivity.this, permission)) {
                        mSetting.showSetting(permission);
                    }
                })
                .start();
    }

    public void onSucceed(int requestCode) {

    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toActivity(clazz, bundle, null);
    }

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, @Nullable Uri data) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }

        if (null != data) {
            intent.setData(data);
        }

        startActivity(intent);
    }

    protected final void toNewActivity(@NonNull Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initSwipeBackFinish();
        beforeOnCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        boolean isStatusBlack = AttrsUtils.getTypeValueBoolean(this, R.attr.picker_status_black);
        AppTool.processMIUI(this, isStatusBlack);
        Logger.d(getClass().getSimpleName() + ":onCreate()");
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(this);
    }

    protected void beforeOnCreate(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        mSwipeBackHelper = new BGASwipeBackHelper(this, this)
                // 下面几项可以不配置，这里只是为了讲述接口用法。
                .setSwipeBackEnable(true) // 设置滑动返回是否可用。默认值为 true
                .setIsOnlyTrackingLeftEdge(true)// 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
                .setIsWeChatStyle(true) // 设置是否是微信滑动返回样式。默认值为 true
                .setShadowResId(R.drawable.bga_sbl_shadow) // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
                .setIsNeedShowShadow(true) // 设置是否显示滑动返回的阴影效果。默认值为 true
                .setIsShadowAlphaGradient(true) // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
                .setSwipeBackThreshold(0.3f) // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
                .setIsNavigationBarOverlap(false);// 设置底部导航条是否悬浮在内容上，默认值为 false
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆
    protected final void toLogin(@NonNull Class<? extends Activity> clazz) {
        toLogin(clazz, null);
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆 携带参数
    protected final void toLogin(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toLogin(() -> toActivity(clazz, bundle));
    }

    protected final void toLogin(@NonNull Runnable action) {
        if (BaseApp.getBaseApp().isLogin()) {
            postDelayed(action, 0);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLogin", true);
            toActivity(BaseApp.getBaseApp().getLoginActivity(), bundle);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() + ":onDestroy()");

    }

    protected void postDelayed(Runnable runnable, long delayMillis) {
        myHandler.postDelayed(runnable, delayMillis);
    }

    protected void post(Runnable runnable) {
        postDelayed(runnable, 0);
    }

    protected final Bundle getBundle() {
        return getIntent().getBundleExtra(BUNDLE_KEY);
    }

    //隐藏键盘
    protected void hideInput() {
        View currentFocus = getCurrentFocus();

        InputMethodManager systemService = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (null != systemService && null != currentFocus) {
            systemService.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    @Override
    protected void onRestart() {
        Logger.d(getClass().getSimpleName() + ":onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Logger.d(getClass().getSimpleName() + ":onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Logger.d(getClass().getSimpleName() + ":onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Logger.d(getClass().getSimpleName() + ":onDestroy()");
        super.onStop();
    }

    @Override
    protected void onStart() {
        Logger.d(getClass().getSimpleName() + ":onStart()");
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Logger.d(getClass().getSimpleName() + ":onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }


    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return 是否支持滑动返回
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
        Logger.d("页面滑动中，滑动距离：" + slideOffset);
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
