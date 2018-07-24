package com.hy.library.base.mvp;


import java.lang.ref.WeakReference;

/**
 * Created time : 2018/5/30 17:20.
 * <p>
 * 中文：MVP中的presenter，作为 View 层 与 Model 层 交互的中间媒介（纽带）负责处理用户交互的复杂逻辑。
 * English:The intermediary (tie), the layer of interaction between the View layer and the Model layer,
 * handles the complex logic of user interaction.
 * </p>
 *
 * @author HY
 */
public abstract class MvpPresenter<V extends MvpView> {
    private WeakReference<V> mReference;

    public void attachView(V view) {
        mReference = new WeakReference<>(view);
    }


    /**
     * 获取视图对象，将数据传递给view并刷新
     * get view object,send data and update ui
     */
    public V getView() {
        if (null == mReference)
            throw new NullPointerException("MvpView reference is null. Have you called attachView()?");
        V v = mReference.get();
        if (null == v) throw new NullPointerException("This view maybe recycled");
        return v;
    }

    protected void detachView() {
        if (null != mReference) {
            mReference.clear();
            mReference = null;
        }
    }
}
