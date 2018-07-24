package com.hy.library.base.mvp;

import android.support.annotation.NonNull;

import com.hy.library.base.BaseFragment;

/**
 * Created time : 2018/5/30 17:28.
 * <p>
 * Mvp框架中的View层，负责UI的绘制及处理
 * the view of mvp framework,responsible for UI drawing and processing
 * </p>
 *
 * @param <V> model
 * @param <P> presenter
 * @author HY
 * @author HY
 * @see MvpPresenter
 * @see MvpView
 */
public abstract class BaseMvpFragment<V extends MvpView, P extends MvpPresenter<V>> extends BaseFragment implements MvpView {

    protected P presenter;

    /**
     * 创建presenter
     * create presenter
     */
    @NonNull
    protected abstract P createPresenter();

    private V getMvpView() {
        return (V) this;
    }

    //
//
    public void initMvp() {
        this.presenter = createPresenter();
        this.presenter.attachView(getMvpView());
    }

    //
    public void onDestroyView() {
        this.presenter.detachView();
        super.onDestroyView();
    }
}
