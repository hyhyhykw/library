package com.hy.library.network;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hy.library.BaseApp;
import com.hy.library.receiver.NetChangeReceiver;
import com.hy.library.utils.DateTimeUtils;
import com.hy.library.utils.Logger;
import com.hy.library.utils.ToastWrapper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created time : 2017/11/7 17:21.
 *
 * @author HY
 */
public final class NetworwClient {

    private static NetworwClient sMovieClient;

    private final OkHttpClient mClient;

    private NetworwClient() {
        Interceptor interceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        mClient = new OkHttpClient.Builder()
//                .addInterceptor(interceptor)
                .connectTimeout(1200000, TimeUnit.MILLISECONDS)
                .build();
    }

    public synchronized static NetworwClient getInstance() {
        if (null == sMovieClient) {
            sMovieClient = new NetworwClient();
        }
        return sMovieClient;
    }

    /**
     * 异步的post请求
     *
     * @param params   请求数据
     * @param url      url
     * @param callback http请求回调
     */
    public final void postAsync(String url, String params, UICallBack callback) {
        if (NetChangeReceiver.netType == NetChangeReceiver.NetType.NONE) {
            HttpResponseBean bean = new HttpCacheDao(BaseApp.getBaseApp().getContext()).query(url, params);
            if (null != bean) {
                callback.onResponse(bean.getResponse());
            } else {
                callback.onFailedInUI();
            }
            return;
        }

        FormBody formBody = new FormBody.Builder()
                .add("params", params)
                .build();
        Logger.d(params);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Logger.d(request);

        request(url, params, request, callback);
    }

    private void request(String url, String params, Request request, UICallBack callback) {
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody body = response.body();
                if (null == body) {
                    callback.mHandler.post(() -> {
                        ToastWrapper.show("服务器响应失败");

                        callback.onFailedInUI();
                    });

                    return;
                }
                final String json = body.string();
                Logger.d("response====" + json);

                if (callback.isSave()) {
                    HttpResponseBean responseBean = new HttpResponseBean();
                    responseBean.setUrl(url);
                    responseBean.setParams(params);
                    responseBean.setStamp(DateTimeUtils.getCurrentDate());
                    responseBean.setResponse(json);

                    new HttpCacheDao(BaseApp.getBaseApp().getContext()).add(responseBean);
                }


                if (TextUtils.isEmpty(json)) {
                    callback.mHandler.post(() -> {
                        ToastWrapper.show("服务器响应失败");

                        callback.onFailedInUI();
                    });
                    return;
                }

                callback.onResponse(json);
            }
        });
    }

    /**
     * 异步的post请求 不需要回调
     *
     * @param params 请求数据
     * @param url    url
     */
    public final void postAsync(String url, String params) {

        FormBody formBody = new FormBody.Builder()
                .add("params", params)
                .build();
        Logger.d(params);

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        Logger.d(request);
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Logger.d("请求失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                Logger.d("请求成功");
            }
        });

    }


    /**
     * 异步的get请求
     *
     * @param url      url
     * @param callback http请求回调
     */
    public final void postAsync(String url, UICallBack callback) {
        if (NetChangeReceiver.netType == NetChangeReceiver.NetType.NONE) {
            HttpResponseBean bean = new HttpCacheDao(BaseApp.getBaseApp().getContext())
                    .query(url, "");
            if (null != bean) {
                callback.onResponse(bean.getResponse());
            } else {
                callback.onFailedInUI();
            }
            return;
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        Logger.d(request);
        request(url, "", request, callback);
    }

//    /**
//     * 多图上传
//     */
//    public void publish(String url, String params, ArrayList<String> images, Callback callback) {
//
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("params", params);
//
//        File file;
//        for (String image : images) {
//            file = new File(image);
//            if (image.endsWith("png")||image.endsWith("PNG")){
//                builder.addFormDataPart("image", file.getName(),
//                        RequestBody.create(MediaType.parse("image/png"), file));
//            }else if (image.endsWith("jpg")||image.endsWith("JPG")||image.endsWith("jpeg")||image.endsWith("JPEG")){
//                builder.addFormDataPart("image", file.getName(),
//                        RequestBody.create(MediaType.parse("image/jpeg"), file));
//            }
//        }
//
//        Request request = new Request.Builder()
//                .post(builder.build())
//                .url(url)
//                .build();
//
//        mClient.newCall(request).enqueue(callback);
//    }
//
//
//    /**
//     * 单图上传
//     */
//    public void upload(String url, String params, String image, Callback callback) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("params", params);
//
//        File file = new File(image);
//        if (image.endsWith("png")||image.endsWith("PNG")){
//            builder.addFormDataPart("image", file.getName(),
//                    RequestBody.create(MediaType.parse("image/png"), file));
//        }else if (image.endsWith("jpg")||image.endsWith("JPG")||image.endsWith("jpeg")||image.endsWith("JPEG")){
//            builder.addFormDataPart("image", file.getName(),
//                    RequestBody.create(MediaType.parse("image/jpeg"), file));
//        }
//
//        Request request = new Request.Builder()
//                .post(builder.build())
//                .url(url)
//                .build();
//        mClient.newCall(request).enqueue(callback);
//    }
}
