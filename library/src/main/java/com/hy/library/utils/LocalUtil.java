package com.hy.library.utils;

import android.content.Context;

import java.io.File;

/**
 * Created time : 2018/4/13 16:50.
 *
 * @author HY
 */
public class LocalUtil {
    private final Context context;

    public LocalUtil(Context context) {
        this.context = context;
    }

    /**
     * 计算缓存的大小,
     *
     * @return 获取缓存大小
     */
    public String getCacheSize() {
        long fileSize = 0;
        String cacheSize = "0KB";
        File filesDir = context.getApplicationContext().getFilesDir();// /data/data/package_name/files
        File cacheDir = context.getCacheDir();// /data/data/package_name/cache
        fileSize += getDirSize(filesDir);
        fileSize += getDirSize(cacheDir);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        File externalCacheDir = getExternalCacheDir(context);// "<sdcard>/Android/data/<package_name>/cache/"
        fileSize += getDirSize(externalCacheDir);
        if (fileSize > 0)
            cacheSize = formatFileSize(fileSize);
        return cacheSize;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        // 清除数据缓存
        clearCacheFolder(context.getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(context.getCacheDir(), System.currentTimeMillis());
        // 2.2版本才有将应用缓存转移到sd卡的功能
        clearCacheFolder(getExternalCacheDir(context),
                System.currentTimeMillis());
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return 清理缓存目录是否成功
     */
    private int clearCacheFolder(File dir, long curTime) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }


    /**
     * 获取目录文件大小
     *
     * @param dir 文件路径
     * @return 文件夹大小
     */
    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }

    /**
     * 将二进制长度转换成文件大小
     *
     * @param length 大小
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(long length) {
        String result;
        int sub_string;
        if (length >= 1073741824) {
            sub_string = String.valueOf((float) length / 1073741824).indexOf(
                    ".");
            result = ((float) length / 1073741824 + "000").substring(0,
                    sub_string + 3) + "GB";
        } else if (length >= 1048576) {
            sub_string = String.valueOf((float) length / 1048576).indexOf(".");
            result = ((float) length / 1048576 + "000").substring(0,
                    sub_string + 3) + "MB";
        } else if (length >= 1024) {
            sub_string = String.valueOf((float) length / 1024).indexOf(".");
            result = ((float) length / 1024 + "000").substring(0,
                    sub_string + 3) + "KB";
        } else result = Long.toString(length) + "B";
        return result;
    }


    public static File getExternalCacheDir(Context context) {
        return context.getExternalCacheDir();
    }
}
