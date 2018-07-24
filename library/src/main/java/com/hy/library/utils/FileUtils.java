package com.hy.library.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.mabeijianxi.smallvideorecord2.DeviceUtils;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created time : 2018/4/11 15:51.
 *
 * @author HY
 */
public final class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("This class can't be instantiate(这个工具类不能被实例化)");
    }


    /**
     * 利用签名辅助类，将字符串转为字节数组
     *
     * @param str 路径
     * @return md5
     */
    @NonNull
    public static String md5(@NonNull String str) {
        byte[] digest;
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            digest = md.digest(str.getBytes());
            return bytes2hex(digest);
        } catch (NoSuchAlgorithmException e) {
            Logger.e(e.getMessage(), e);
        }
        return str;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param digest 字节数组
     * @return md5
     */
    @NonNull
    private static String bytes2hex(byte[] digest) {
        StringBuilder sbl = new StringBuilder();
        String tmp;
        for (byte b : digest) {
            //将每个字符与0xFF进行运算得到10进制，再借助Integer转为16进制
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {//每个字节8位，转换为16进制，两个16进制位
                tmp = "0" + tmp;
            }
            sbl.append(tmp);
        }

        return sbl.toString();
    }

    public static String getMime(String fileName) {
        int index = fileName.lastIndexOf(".");
        String ext;
        if (index == -1) {
            ext = "";
        } else {
            ext = fileName.substring(index);
        }

        switch (ext) {
            case ".3gp":
                return "video/3gpp";
            case ".apk":
                return "application/vnd.android.package-archive";
            case ".asf":
                return "video/x-ms-asf";
            case ".avi":
                return "video/x-msvideo";
            case ".bin":
            case ".class":
            case ".exe":
                return "application/octet-stream";
            case ".c":
            case ".cpp":
            case ".h":
            case ".java":
            case ".log":
            case ".prop":
            case ".rc":
            case ".sh":
            case ".txt":
            case ".xml":
                return "text/plain";
            case ".bmp":
                return "image/bmp";
            case ".gif":
                return "image/gif";
            case ".doc":
                return "application/msword";
            case ".gtar":
                return "application/x-gtar";
            case ".gz":
                return "application/x-gzip";
            case ".htm":
            case ".html":
                return "text/html";
            case ".jar":
                return "application/java-archive";
            case ".jpeg":
            case ".jpg":
                return "image/jpeg";
            case ".js":
                return "application/x-javascript";
            case ".m3u":
                return "audio/x-mpegurl";
            case ".m4a":
            case ".m4b":
            case ".m4p":
                return "audio/mp4a-latm";
            case ".m4u":
                return "video/vnd.mpegurl";
            case ".m4v":
                return "video/x-m4v";
            case ".mov":
                return "video/quicktime";
            case ".mp2":
            case ".mp3":
                return "audio/x-mpeg";
            case ".mp4":
            case ".mpg4":
                return "video/mp4";
            case ".mpe":
            case ".mpeg":
            case ".mpg":
            case ".mpga":
                return "video/mpeg";
            case "mpc":
                return "application/vnd.mpohun.certificate";
            case ".msg":
                return "application/vnd.ms-outlook";
            case ".ogg":
                return "audio/ogg";
            case ".pdf":
                return "application/pdf";
            case ".png":
                return "image/png";
            case ".pps":
            case ".ppt":
                return "application/vnd.ms-powerpoint";
            case ".rar":
                return "application/x-rar-compressed";
            case ".rmvb":
                return "audio/x-pn-realaudio";
            case ".rtf":
                return "application/rtf";
            case ".tar":
                return "application/x-tar";
            case ".tgz":
                return "application/x-compressed";
            case ".wav":
                return "audio/x-wav";
            case ".wma":
                return "audio/x-ms-wma";
            case ".wmv":
                return "audio/x-ms-wmv";
            case ".wps":
                return "application/vnd.ms-works";
            case ".z":
                return "application/x-compress";
            case ".zip":
                return "application/zip";
            default:
                return "*/*";
        }
    }

    public static String getVideoCachePath(){
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String path = dcim.getPath() + "/touch/";
        if (DeviceUtils.isZte()) {
            if (!dcim.exists()) {
                path = dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/touch/";
            }
        }
        return path;
    }

    public static String getExtension(String url) {
        int indexOf = url.lastIndexOf(".");
        try {
            return url.substring(indexOf + 1);
        } catch (Exception e) {
            Logger.d(e.getMessage(), e);
        }
        return "jpg";
    }

    @NonNull
    public static String formatFileSize(long fileSize) {
        if (fileSize > 0 && fileSize < 1023) {
            return fileSize + "B";
        } else if (fileSize >= 1024 && fileSize < 1024 * 1024) {
            return fileSize / 1024 + "KB";
        } else if (fileSize >= 1024 * 1024 && fileSize < 1024 * 1024 * 1024) {
            return fileSize / (1024 * 1024) + "MB";
        } else {
            return fileSize / (1024 * 1024 * 1024) + "GB";
        }
    }

    public static File getNewApkDir(@NonNull Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            String path = cacheDir.getAbsolutePath();
            return new File(path + File.separator + "movie-apk" + File.separator);
        }

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "Android" + File.separator +
                context.getPackageName() + File.separator +
                "cache" + File.separator + "movieApk" + File.separator);
    }

    public static File getPictureDir() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        path = path + File.separator + "mini-video" + File.separator;
        return new File(path);
    }

    public static File getCachePicDir(@NonNull Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            String path = cacheDir.getAbsolutePath();
            return new File(path + File.separator + "image-cache" + File.separator);
        }
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                + "Android" + File.separator +
                context.getPackageName() + File.separator +
                "cache" + File.separator + "image-cache" + File.separator);
    }

    //获取图片的绝对路径
    private String getFilePathByContentResolver(Context context, Uri uri) {
        if (null == uri) {
            return null;
        }
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);
        String filePath = null;
        if (null == c) {
            throw new IllegalArgumentException(
                    "Query on " + uri + " returns null result.");
        }
        try {
            if ((c.getCount() == 1) && c.moveToFirst()) {
                filePath = c.getString(
                        c.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
            }
        } finally {
            c.close();
        }
        return filePath;
    }

    /**
     * 图片插入到系统相册,解决系统图库不能打开图片的问题
     */
    public static void insertImageToSystemGallery(Context context, String filePath) {

        try {
            File file = new File(filePath);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, filePath);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));//path_export是你导出的文件路径
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), filePath, getFileName(filePath), "");
        } catch (Exception e) {
            Logger.d(e.getMessage(), e);
        }
//        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        Uri uri = Uri.fromFile(new File(filePath));
//        intent.setData(uri);
//        context.sendBroadcast(intent);
    }

    public static String getFileName(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        int index = name.lastIndexOf(".");
        if (index == -1) return name;
        return name.substring(0, index);
    }

//    /**
//     * 删除裁剪的图片
//     */
//    public static void deleteCrop() {
//        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        String s = dcim.getAbsolutePath() + File.separator + "IMMQY";
//        delete(new File(s));
//    }

    public static String getImageSavePath(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        return cacheDir.getAbsolutePath() + File.separator + "image" + File.separator + "caches";
    }

    public static String getImageCropPath(Context context) {
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        return cacheDir.getAbsolutePath() + File.separator + "image" + File.separator + "crops";
    }

    public static void delete(File cacheFile) {
        if (!cacheFile.exists()) return;
        if (cacheFile.isDirectory()) {
            File[] files = cacheFile.listFiles();
            for (File file : files) {
                if (file.isDirectory()) delete(file);
                else //noinspection ResultOfMethodCallIgnored
                    file.delete();
            }
        }

        //noinspection ResultOfMethodCallIgnored
        cacheFile.delete();
    }
}
