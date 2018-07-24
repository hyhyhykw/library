package com.hy.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hy.library.BaseApp;
import com.hy.library.utils.AppTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created time : 2018/4/3 10:46.
 *
 * @author HY
 */
public abstract class BaseListAdapter<T, V extends BaseListAdapter.BaseViewHolder> extends BaseAdapter {

    protected final List<T> mData = new ArrayList<>();

    private final ArrayList<Unbinder> mUnbinders = new ArrayList<>();

    protected Context mContext;

    protected final void toActivity(@NonNull Class<? extends Activity> clazz) {
        toActivity(clazz, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle) {
        toActivity(clazz, bundle, null);
    }

    protected final void toActivity(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, @Nullable Uri data) {
        Intent intent = new Intent(mContext, clazz);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }

        if (null != data) {
            intent.setData(data);
        }

        mContext.startActivity(intent);
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆
    protected final void toLogin(@NonNull Class<? extends Activity> clazz, String... args) {
        toLogin(clazz, null, args);
    }

    //如果已经登陆，跳转指定Activity 否则跳转到登陆 携带参数
    protected final void toLogin(@NonNull Class<? extends Activity> clazz, @Nullable Bundle bundle, String... args) {
        toLogin(() -> toActivity(clazz, bundle), args);
    }

    protected final void toLogin(@NonNull Runnable action, String... args) {
        if (BaseApp.getBaseApp().isLogin()) {
            AppTool.post(action);
        } else {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isLogin", true);
            toActivity(BaseApp.getBaseApp().getLoginActivity(), bundle);
//            String operate;
//            if (args == null || args.length == 0) {
//                operate = "操作";
//            } else {
//                operate = args[0];
//            }
//            AlertDialog.newBuilder(mContext)
//                    .setCancelable(false)
//                    .setTitle(R.string.title_dialog)
//                    .setMessage("登录后才可以" + operate + ",是否继续?")
//                    .setPositiveButton(R.string.yes, (dialog, which) -> {
//                        toActivity(LoginActivity.class);
//                        dialog.cancel();
//                    })
//                    .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel())
//                    .show();

        }
    }

    public void reset(@NonNull List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void reset(@NonNull Collection<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void reset(@NonNull T[] data) {
        reset(Arrays.asList(data));
    }

    public void addData(@NonNull List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull Collection<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }


    public void addData(@NonNull T[] data) {
        addData(Arrays.asList(data));
    }

    public void addItem(T t) {
        mData.add(t);
        notifyDataSetChanged();
    }

    public void addItem(T t, int position) {
        mData.add(position, t);
        notifyDataSetChanged();
    }


    public T getFirst() {
        return mData.get(0);
    }

    public T getLast() {
        return mData.get(mData.size() - 1);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mContext) mContext = parent.getContext();
        V holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(layout(), parent, false);

            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            //noinspection SingleStatementInBlock,unchecked
            holder = (V) convertView.getTag();
        }

        holder.bind(position);
        return convertView;
    }


    @NonNull
    protected abstract V createViewHolder(View itemView);

    @LayoutRes
    protected abstract int layout();


    public abstract class BaseViewHolder {
        public final View itemView;

        public BaseViewHolder(View itemView) {
            this.itemView = itemView;
            mUnbinders.add(ButterKnife.bind(this, itemView));
        }

        protected abstract void bind(int position);
    }


    public final void unbind() {
        for (Unbinder unbinder : mUnbinders) {
            unbinder.unbind();
        }

    }

}
