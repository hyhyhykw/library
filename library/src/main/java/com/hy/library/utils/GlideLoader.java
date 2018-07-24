package com.hy.library.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.hy.library.BaseApp;

/**
 * Created time : 2018/7/18 15:16.
 *
 * @author HY
 */
public class GlideLoader {
    public static RequestBuilder<Drawable> load(Context context, String image) {
        if (TextUtils.isEmpty(image) || (!image.startsWith("http://") && !image.startsWith("https://"))) {
            image = BaseApp.getBaseApp().getBaseUrl() + image;
        }

        return Glide.with(context)
                .load(image);
    }
}
