package com.hy.library.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * 倒计时
 *
 * @author HY
 */
public class CountTimerUtil extends CountDownTimer {

    public interface OnFinishListener {
        void onFinish();
    }

    private OnFinishListener mOnFinishListener;

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        mOnFinishListener = onFinishListener;
    }

    private WeakReference<TextView> mReference;

    /**
     * @param millisInFuture    总时长
     * @param countDownInterval 倒计时间隔时间
     * @param clicked           点击按钮触发事件
     */
    public CountTimerUtil(long millisInFuture, long countDownInterval, TextView clicked) {
        super(millisInFuture, countDownInterval);

        mReference = new WeakReference<>(clicked);
    }

    @Override
    public void onFinish() {
        if (null != mOnFinishListener) {
            mOnFinishListener.onFinish();
        }

        if (null == mReference) return;
        TextView btn = mReference.get();
        if (null == btn) return;
        btn.setClickable(true);
        btn.setEnabled(true);
        btn.setText("获取验证码");
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if (null == mReference) return;
        TextView btn = mReference.get();
        if (null == btn) return;

        btn.setClickable(false);
        btn.setEnabled(false);
        btn.setText(String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000));
    }

}
