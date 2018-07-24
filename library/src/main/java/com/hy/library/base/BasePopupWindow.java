package com.hy.library.base;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import butterknife.ButterKnife;

/**
 * Created time : 2018/4/16 9:29.
 *
 * @author HY
 */
public abstract class BasePopupWindow extends PopupWindow implements PopupWindow.OnDismissListener {
    protected final ViewGroup mParent;
    protected final View mView;

    private final Activity mActivity;

    public BasePopupWindow(Activity activity) {
        super(activity);
        mActivity = activity;
        mParent = (ViewGroup) activity.getWindow().getDecorView();
        mView = LayoutInflater.from(activity).inflate(layout(), mParent, false);
        ButterKnife.bind(this, mView);
        init();

        setContentView(mView);
    }

    protected abstract void init();

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
    }

    public void show() {
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        backgroundAlpha(0.5f);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        backgroundAlpha(0.5f);
    }

    //设置窗体透明度的方法
    private void backgroundAlpha(float alpha) {
        //改变窗体属性,更改窗体透明度的方法
        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
        params.alpha = alpha;
        mActivity.getWindow().setAttributes(params);
    }

    @LayoutRes
    protected abstract int layout();


}
