package com.hy.picker.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by felix on 2017/12/21 下午10:58.
 */

public class IMGStickerImageView extends IMGStickerView {

    private ImageView mImageView;

    public IMGStickerImageView(Context context) {
        super(context);
    }

    public IMGStickerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IMGStickerImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Bitmap mBitmap;

    public void setImageBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mImageView.setImageBitmap(bitmap);
    }

    public void destroy(){
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @Override
    public void onRemove() {
        super.onRemove();
        if (null != mBitmap) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public void setImageResource(@DrawableRes int resId) {
        mImageView.setImageResource(resId);
    }

    @Override
    public View onCreateContentView(Context context) {
        mImageView = new ImageView(context);
        return mImageView;
    }
}
