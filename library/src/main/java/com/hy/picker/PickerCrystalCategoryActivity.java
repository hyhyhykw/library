package com.hy.picker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.hy.picker.adapter.CrystalCategoryAdapter;
import com.hy.picker.core.CrystalCategory;
import com.hy.picker.utils.NetworkUtils;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

/**
 * Created time : 2018/8/27 16:42.
 *
 * @author HY
 */
public class PickerCrystalCategoryActivity extends BaseListActivity implements CrystalCategoryAdapter.OnItemClickListener, Constants {

    private CrystalCategoryAdapter mCrystalCategoryAdapter = new CrystalCategoryAdapter();

    private boolean isOther;

    @Override
    protected void initView() {
        rvCrystal.addItemDecoration(new DefaultItemDecoration(Color.parseColor("#f5f5f5")));
        mCrystalCategoryAdapter.setOnItemClickListener(this);
        rvCrystal.setAdapter(mCrystalCategoryAdapter);
        rvCrystal.setLayoutManager(new LinearLayoutManager(this));

        isOther = getIntent().getBooleanExtra("other", false);
        Looper.myQueue().addIdleHandler(() -> {
            initData();
            return false;
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void initData() {
        String url;
        String language = getResources().getConfiguration().locale.getLanguage();
        if (isOther) {
            if (language.contains("zh")) {
                url = CHILD_CATEGORY;
            } else {
                url = CHILD_CATEGORY_EN;
            }
        } else {
            if (language.contains("zh")) {
                url = CATEGORY;
            } else {
                url = CATEGORY_EN;
            }
        }
        NetworkUtils.getInstance()
                .url(url)
                .start(new NetworkUtils.TaskListener() {
                    @Override
                    public void onSuccess(String json) {
                        loadSuccess();
                        CrystalCategory category = new Gson().fromJson(json, CrystalCategory.class);
                        mCrystalCategoryAdapter.reset(category.getCategory());
                    }

                    @Override
                    public void onFailed() {
                        loadFailed();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onClick(CrystalCategory.Category item) {
        if (item.getId() == 11) {
            startActivityForResult(new Intent(this, PickerCrystalCategoryActivity.class)
                    .putExtra("other", true), 666);
        } else {
            startActivityForResult(new Intent(this, PickerCrystalActivity.class)
                    .putExtra("id", item.getId()), 666);
        }
    }
}
