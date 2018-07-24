package com.hy.library.base.mvp;

import android.support.annotation.NonNull;

import com.hy.library.base.BaseActivity;

/**
 * Created time : 2018/5/30 17:18.
 * <p>
 * Mvp框架中的View层，负责UI的绘制及处理
 * the view of mvp framework,responsible for UI drawing and processing
 * </p>
 *
 * @param <V> model
 * @param <P> presenter
 * @author HY
 * @see MvpPresenter
 * @see MvpView
 */
public abstract class MvpActivity<V extends MvpView, P extends MvpPresenter<V>> extends BaseActivity implements MvpView {
    protected P presenter;

    private V getMvpView() {
        return (V) this;
    }

    public void initMvp() {
        this.presenter = createPresenter();
        this.presenter.attachView(getMvpView());
        super.initMvp();
    }

    /**
     * 创建presenter
     * create presenter
     */
    @NonNull
    protected abstract P createPresenter();

    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
