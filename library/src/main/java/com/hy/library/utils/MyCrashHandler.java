package com.hy.library.utils;

import android.content.Context;
import android.content.Intent;

import com.hy.library.BaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created time : 2018/1/4 9:31.
 *
 * @author HY
 */
public final class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    private WeakReference<Context> mReference;

    private MyCrashHandler() {
    }

    private static MyCrashHandler sInstance;

    public static MyCrashHandler getInstance() {
        if (null == sInstance) {
            sInstance = new MyCrashHandler();
        }
        return sInstance;
    }


    public void init(Context appContext) {
        mReference = new WeakReference<>(appContext);

        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Logger.e(e.getMessage(), e);

        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        e.printStackTrace(pw);
        Throwable cause = e.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        final String crashMsg = writer.toString();
        saveCrashInfo2File(crashMsg);

        restartApp();
    }

    /**
     * 保存crash信息
     * save crash message
     *
     * @param crashMsg crash msg
     */
    private void saveCrashInfo2File(String crashMsg) {
        final StringBuilder sb = new StringBuilder();

        sb.append(crashMsg);
        // 保存文件
        long timeStamp = System.currentTimeMillis();
        SimpleDateFormat dateFormat =
                (SimpleDateFormat) SimpleDateFormat.getInstance();
        dateFormat.applyPattern("yyyy-MM-dd-HH-mm-ss");// 用于格式化日期,作为日志文件名的一部分

        String time = dateFormat.format(new Date());
        final String fileName = "crash-" + time + "-" + timeStamp + ".log";
        if (null != mReference) {
            Context context = mReference.get();

            if (null != context) {
                File externalCacheDir = context.getExternalCacheDir();
                String dir;
                if (null != externalCacheDir) {
                    dir = externalCacheDir.getAbsolutePath();
                } else {
                    File cacheDir = context.getCacheDir();
                    dir = cacheDir.getAbsolutePath();
                }
                dir = dir + File.separator + "crash";

                File parent = new File(dir);
                if (!parent.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    parent.mkdirs();
                }
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(new File(dir, fileName));
                    fos.write(sb.toString().getBytes());
                    fos.close();
                } catch (Exception e) {
                    Logger.e(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 重启app
     * Restart the app
     */
    private void restartApp() {
        if (mReference == null) return;
        Context context = mReference.get();
        if (null == context) return;
        Intent intent = new Intent(context, BaseApp.getBaseApp().getLaunchActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        //Before the end of the process, can put your application log out or exit code on this piece of code before;
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}