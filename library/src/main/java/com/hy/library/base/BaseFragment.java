package com.hy.library.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.hy.library.BaseApp;
import com.hy.library.R;
import com.hy.library.utils.Logger;
import com.hy.library.utils.ToastWrapper;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.hy.library.utils.DefaultRationale;
import com.hy.library.utils.PermissionSetting;

/**
 * Created time : 2018/4/3 11:28.
 *
 * @author HY
 */
public abstract class BaseFragment extends Fragment {
    private View mView = null;

    private Unbinder unbinder;
    protected static final String KEYWORD = "keyword";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + ":onCreateView()");
        if (null == mView)
            mView = inflater.inflate(layout(), container, false);
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (null != parent) parent.removeView(mView);
        beforeViewBind(savedInstanceState);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @NonNull
    @Override
    public Context getContext() {
        Context context = super.getContext();
        if (null != context)
            return context;
        return BaseApp.getBaseApp().getContext();
    }

    private static final Handler myHandler = new Handler(Looper.getMainLooper());

    protected void postDelayed(Runnable runnable, long delayMillis) {
        myHandler.postDelayed(runnable, delayMillis);
    }

    //隐藏键盘
    protected void hideInput() {
        FragmentActivity activity = getActivity();
        View currentFocus = activity != null ? activity.getCurrentFocus() : null;
        if (null != currentFocus) {
            Context context = getContext();
            InputMethodManager manager = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(currentFocus.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    protected void beforeViewBind(@Nullable Bundle savedInstanceState) {
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toActivity(clazz, bundle, null);
    }

    protected final void toActivityForResult(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, int requestCode) {
        Intent intent = new Intent(getContext(), clazz);
        if (null != bundle) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    private static final String BUNDLE_KEY = "bundle";

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, @Nullable Uri data) {
        Intent intent = new Intent(getContext(), clazz);
        if (null != bundle) {
            intent.putExtra(BUNDLE_KEY, bundle);
        }

        if (null != data) {
            intent.setData(data);
        }

        startActivity(intent);
    }

    protected void toNewActivity(@NonNull Class<? extends Activity> clazz) {
        Intent intent = new Intent(getContext(), clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Logger.d(getClass().getSimpleName() + ":onActivityCreated()");
        super.onActivityCreated(savedInstanceState);
        initMvp();
    }

    protected void initMvp() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRationale = new DefaultRationale();
        mSetting = new PermissionSetting(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d(getClass().getSimpleName() + ":onAttach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(getClass().getSimpleName() + ":onDestroy()");
    }

    protected abstract void initView();

    @LayoutRes
    protected abstract int layout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private boolean isInit = false;

    @Override
    public void onResume() {
        Logger.d(getClass().getSimpleName() + ":onResume()");
        super.onResume();

        if (!isInit) {
            isInit = true;
            postDelayed(this::initView, 0);
        }
    }

    @Override
    public void onPause() {
        Logger.d(getClass().getSimpleName() + ":onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        Logger.d(getClass().getSimpleName() + ":onDestroy()");
        super.onStop();
    }

    @Override
    public void onStart() {
        Logger.d(getClass().getSimpleName() + ":onStart()");
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Logger.d(getClass().getSimpleName() + ":onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

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
                    if (AndPermission.hasAlwaysDeniedPermission(getContext(), permission)) {
                        mSetting.showSetting(permission);
                    }
                })
                .start();
    }

    public void onSucceed(int requestCode) {

    }
}
