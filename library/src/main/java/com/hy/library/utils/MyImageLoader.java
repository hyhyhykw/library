package com.hy.library.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.hy.library.BaseApp;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created time : 2018/8/17 17:27.
 *
 * @author HY
 */
public class MyImageLoader {
    public static void displayImage(String image, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        if (TextUtils.isEmpty(image) || (!image.startsWith("http://") && !image.startsWith("https://"))) {
            image = BaseApp.getBaseApp().getBaseUrl() + image;
        }
        ImageLoader.getInstance().displayImage(image, imageView, options, listener);

    }
}
