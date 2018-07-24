package cn.finalteam.rxgalleryfinal.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;

import cn.finalteam.rxgalleryfinal.imageloader.rotate.RotateTransformation;

/**
 * Created by pengjianbo  Dujinyang on 2016/8/13 0013.
 */
public class GlideImageLoader implements AbsImageLoader {

    @Override
    public void displayImage(Context context, String path, PhotoView imageView, Drawable defaultDrawable, Bitmap.Config config, boolean resize, boolean isGif, int width, int height, int rotate) {
//        DrawableRequestBuilder builder;
//        if (path != null) {
//            builder = Glide.with(context)
//                    .load(new File(path)).
//                    .placeholder(defaultDrawable);
//
//        } else {
//            builder = Glide.with(context)
//                    .load(new File("/sdcard"))
//                    .placeholder(defaultDrawable);
//        }
//        if (resize) {
//            builder = builder.override(width, height);
//        }
//        builder
//                .crossFade()
//                .transform(new RotateTransformation(context, rotate))
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageView);
        RequestOptions options = new RequestOptions()
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .override(width, height)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .transform(new RotateTransformation(rotate));
        if (isGif) {
//            Glide.with(context)
//                    .load(path)
//                    .placeholder(defaultDrawable)
//                    .error(defaultDrawable)
//                    .override(width, height)
//                    .crossFade()
//                    .transform(new RotateTransformation(context, rotate))
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(imageView);

            Glide.with(context)
                    .load(path)
                    .apply(options)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(imageView);

        } else {
//            Glide.with(context)
//                    .load(path)
//                    .asBitmap()
//                    .placeholder(defaultDrawable)
//                    .error(defaultDrawable)
//                    .override(width, height)
//                    .transform(new RotateTransformation(context, rotate))
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(imageView);

            Glide.with(context)
                    .asBitmap()
                    .load(path)
                    .apply(options)
                    .into(imageView);
        }
    }
}
