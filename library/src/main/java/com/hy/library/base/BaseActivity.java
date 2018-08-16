package com.hy.library.base;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import com.hy.library.utils.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created time : 2018/4/3 9:39.
 *
 * @author HY
 */
public abstract class BaseActivity extends CommonBaseActivity {

    private Unbinder mUnbinder;

    public void hideBar() {
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isImmersiveModeEnabled = ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        }

        if (!isImmersiveModeEnabled) {
            Logger.i("Turning immersive mode mode on. ");
            if (Build.VERSION.SDK_INT >= 14) {
                newUiOptions |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
//            if (Build.VERSION.SDK_INT >= 16) {
//                newUiOptions |= View.SYSTEM_UI_FLAG_FULLSCREEN;
//            }
            if (Build.VERSION.SDK_INT >= 19) {
                newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            }
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d(getClass().getSimpleName() + ":onCreate()");
        beforeSetContentView(savedInstanceState);
        setContentView(layout());
        mUnbinder = ButterKnife.bind(this);
        initMvp();

        Looper.myQueue().addIdleHandler(() -> {
            onCreateDelay(savedInstanceState);
            return false;
        });
//        initView();
    }

    private void onCreateDelay(@Nullable Bundle savedInstanceState) {
        postDelayed(this::initView, 0);
    }

    public void initMvp() {

    }

    protected void beforeSetContentView(@Nullable Bundle savedInstanceState) {
    }

    @LayoutRes
    protected abstract int layout();

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mUnbinder) {
            mUnbinder.unbind();
        }
    }

}