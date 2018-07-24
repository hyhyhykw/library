package com.hy.library.utils;

import android.text.TextUtils;

import com.hy.library.BaseApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created time : 2018/6/9 10:39.
 *
 * @author HY
 */
public class PicassoLoader {
    public static RequestCreator load(String image) {
        if (TextUtils.isEmpty(image) || (!image.startsWith("http://") && !image.startsWith("https://"))) {
            image = BaseApp.getBaseApp().getBaseUrl() + image;
        }
        return Picasso.get().load(image);
    }
}
