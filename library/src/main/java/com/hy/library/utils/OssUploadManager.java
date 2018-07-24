package com.hy.library.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hy.library.BaseApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created time : 2018/6/14 10:22.
 * 阿里云上传工具类
 *
 * @author HY
 */
public class OssUploadManager {
    public String END_POINT;
    public  String DOMAIN;
    private  String BUCKET;
    public static final int TYPE_IMG_AVATAR = 1;//用户头像
    public static final int TYPE_IMG_USER = 2;//用户封面图片
    public static final int TYPE_VIDEO = 3;//视频
    public static final int TYPE_IMG_FEEDBACK = 4;//反馈图片
    public static final int TYPE_IMG_POST = 5;//帖子图片
    public static final int TYPE_IMG_COMMENT = 6;//帖子图片

    private static final ArrayList<String> mPaths = new ArrayList<>();

    //    private File mFile;
    private OnUploadListener mListener;
    private OnUploadResultListener mOnUploadResultListener;
    private static int type = TYPE_IMG_AVATAR;//上传的文件类型 默认是视频

    private static final ArrayList<String> upload_urls = new ArrayList<>();

    private OSS mOSS;

    private OssUploadManager() {
        END_POINT = BaseApp.getBaseApp().getEndPoint();
        DOMAIN = BaseApp.getBaseApp().getDomain();
        BUCKET = BaseApp.getBaseApp().getBucket();
    }

    private static OssUploadManager sOssManager;

    public synchronized static OssUploadManager getInstance() {
        if (null == sOssManager) {
            sOssManager = new OssUploadManager();
        }
        mPaths.clear();
        upload_urls.clear();
        failedPaths.clear();
        type = TYPE_IMG_AVATAR;
        successCount = 0;
        count = 0;
        return sOssManager;
    }

    public OssUploadManager file(String path) {
        mPaths.add(path);
        return this;
    }

    private static int count;
    private static int successCount;
    private static int failedCount;
    private static final ArrayList<String> failedPaths = new ArrayList<>();

    public void start(Context context) {
        if (mPaths.isEmpty()) {
            Logger.d("请调用file()方法设置至少添加一个上传的文件");
            return;
        }
        count = mPaths.size();
        String gen_key;
        if (type == TYPE_IMG_AVATAR) {
            gen_key = "images/avatar/";
        } else if (type == TYPE_IMG_USER) {
            gen_key = "images/user/";
        } else if (type == TYPE_IMG_FEEDBACK) {
            gen_key = "images/feedback/";
        } else if (type == TYPE_IMG_POST) {
            gen_key = "images/post/";
        } else if (type == TYPE_IMG_COMMENT) {
            gen_key = "images/comments";
        } else {
            gen_key = "videos/";
        }

        String genKey = genKey(gen_key);

        Observable.just(context)
                .map(this::getOSS)
                .map(oss -> {
                    for (int i = 0; i < mPaths.size(); i++) {
                        int position = i;
                        String uploadFilePath = mPaths.get(i);
                        File file = new File(uploadFilePath);

                        String md5 = FileUtils.md5(file.getAbsolutePath());
                        String extension = FileUtils.getExtension(file.getName());
                        String filename = getStamp() + ran(1000, 9999) + md5 + "." + extension;
                        String object = genKey + separator + filename;

                        // 创建上传的对象
                        PutObjectRequest put = new PutObjectRequest(BUCKET, object, uploadFilePath);
                        // 上传的进度回调
                        put.setProgressCallback((request, currentSize, totalSize) -> {
                            if (mListener == null) {
                                return;
                            }
                            int progress = (int) (currentSize * 1.0f / totalSize * 100f);
                            AppTool.post(() -> mListener.onProgress(position, progress));

                        });

                        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                            @Override
                            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                                successCount++;

                                String imageUrl = request.getObjectKey();
                                upload_urls.add(DOMAIN + separator + imageUrl);
                                AppTool.post(() -> {
                                    if (mListener != null) {
                                        mListener.onSuccess(position);
                                    }

                                    onResult();
                                });
                            }

                            @Override
                            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                                failedCount++;
                                serviceException.printStackTrace();
                                clientException.printStackTrace();

                                AppTool.post(() -> {
                                    failedPaths.add(uploadFilePath);
                                    if (mListener != null) {
                                        mListener.onFailure(position);
                                    }

                                    onResult();
                                });
                            }
                        });
                    }
                    return "";
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void onResult() {
        if (successCount + failedCount == count) {
            if (null != mOnUploadResultListener) {
                String video_img;
                if (type == TYPE_VIDEO) {
                    video_img = upload_urls.get(count - 1) + "?x-oss-process=video/snapshot,t_100,f_jpg";
                } else {
                    video_img = "";
                }
                mOnUploadResultListener.onSuccess(upload_urls, video_img, failedPaths);
            }
        }
    }

    /**
     * 创建OSS对象
     */
    private OSS getOSS(Context context) {
        if (mOSS == null) {
            OSSCredentialProvider provider = newCustomSignerCredentialProvider();
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            mOSS = new OSSClient(context, END_POINT, provider, conf);
        }
        return mOSS;
    }

    /**
     * 创建OSS对象的OSSCredentialProvider
     */
    private static OSSCredentialProvider newCustomSignerCredentialProvider() {
        return new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {

                return OSSUtils.sign(BaseApp.getBaseApp().getAliyunAccessKey(), BaseApp.getBaseApp().getAliyunAccessKeySecret(), content);
            }
        };
    }

    public static String genKey(String gen_key) {
        StringBuilder sbl = new StringBuilder();

        int month = getCurrentMonth();
        sbl.append(gen_key)
                .append(getCurrentYear())
                .append("-");

        if (month <= 9) {
            sbl.append(0);
        }

        sbl.append(month);

        int day = getCurrentDay();
        sbl.append(separator);
        if (day <= 9) {
            sbl.append(0);
        }
        sbl.append(day);

        return sbl.toString();
    }

    private static final Calendar CALENDAR = Calendar.getInstance();

    private static final char separator = '/';

    public static int getCurrentYear() {
        return CALENDAR.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        return CALENDAR.get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        return CALENDAR.get(Calendar.DAY_OF_MONTH);
    }

    public static int getStamp() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static int ran(int min, int max) {
        Random random = new Random();

        return random.nextInt(max + 1 - min) + min;
    }

    public OssUploadManager type(int type) {
        OssUploadManager.type = type;
        return this;
    }

    public OssUploadManager listener(OnUploadListener listener) {
        mListener = listener;
        return this;
    }

    public OssUploadManager onResult(OnUploadResultListener listener) {
        mOnUploadResultListener = listener;
        return this;
    }

    public interface OnUploadResultListener {
        void onSuccess(ArrayList<String> upload_urls, String video_img, ArrayList<String> failedPaths);
    }


    public interface OnUploadListener {
        /**
         * 上传的进度
         */
        void onProgress(int position, int progress);

        /**
         * 成功上传
         */
        void onSuccess(int position);

        /**
         * 上传失败
         */
        void onFailure(int position);
    }


}
