package com.hy.library.utils;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.hy.library.BaseApp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

/**
 * Log工具，类似android.util.Log  tag自动产生，格式:
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。
 * http://blog.csdn.net/finddreams
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "unused"})
public final class Logger {

    private static final boolean isSaveLog = false; // 是否把保存日志到SD卡中
    private static final String ROOT = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "Android" + File.separator + "data" + File.separator + BaseApp.getBaseApp().getContext().getPackageName()
            + File.separator + "caches" + File.separator; // SD卡中的根目录
    private static final String PATH_LOG_INFO = ROOT + "info/";

    private Logger() {
    }

    // 容许打印日志的类型，默认是true，设置为false则不打印
    private static final boolean allowD = true;
    private static final boolean allowE = true;
    private static final boolean allowI = true;
    private static final boolean allowV = true;
    private static final boolean allowW = true;
    private static final boolean allowWtf = true;

    @SuppressLint("DefaultLocale")
    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(Line:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName
                .lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(),
                caller.getLineNumber()); // 替换
        String customTagPrefix = "Touch";
        tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
                + tag;
        return tag;
    }


    public static void d(String log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(String log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(int log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(int log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(byte log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(byte log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(short log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(short log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(long log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(long log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(boolean log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(boolean log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(char log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(char log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(Object log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(Object log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(float log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(float log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }

    public static void d(double log) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.d(tag, String.valueOf(log));
    }

    public static void d(double log, Throwable tr) {
        if (!allowD)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        Log.d(tag, String.valueOf(log), tr);
    }
//#########################################################################################################

    public static void e(String log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);


        Log.e(tag, String.valueOf(log));

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, log);
        }
    }

    public static void e(String log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);


        Log.e(tag, String.valueOf(log), tr);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(int log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(int log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(short log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(short log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(byte log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(byte log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(long log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(long log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(float log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(float log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(double log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(double log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(char log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(char log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(boolean log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(boolean log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void e(Object log) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, msg);
        }
    }

    public static void e(Object log, Throwable tr) {
        if (!allowE)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        String msg = String.valueOf(log);
        Log.e(tag, msg);

        if (isSaveLog) {
            point(PATH_LOG_INFO, tag, tr.getMessage());
        }
    }

    public static void i(String log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(String log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(int log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(int log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(short log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(short log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(byte log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(byte log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(long log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(long log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(boolean log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(boolean log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(char log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(char log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(float log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(float log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(double log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(double log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }

    public static void i(Object log) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log));
    }

    public static void i(Object log, Throwable tr) {
        if (!allowI)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.i(tag, String.valueOf(log), tr);
    }


    public static void v(String log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(String log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(int log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(int log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(short log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(short log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(byte log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(byte log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(long log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(long log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(boolean log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(boolean log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(char log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(char log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(float log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(float log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(double log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(double log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void v(Object log) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log));
    }

    public static void v(Object log, Throwable tr) {
        if (!allowV)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.v(tag, String.valueOf(log), tr);
    }

    public static void w(String log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(String log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);

    }

    public static void w(int log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(int log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(short log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(short log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(byte log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(byte log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(long log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(long log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(boolean log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(boolean log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(char log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(char log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(float log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(float log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(double log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log));
    }

    public static void w(double log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void w(Object log) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);
        if (log instanceof Throwable) {
            Throwable tr = (Throwable) log;
            Log.w(tag, tr);
        } else {
            Log.w(tag, String.valueOf(log));
        }
    }

    public static void w(Object log, Throwable tr) {
        if (!allowW)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.w(tag, String.valueOf(log), tr);
    }

    public static void wtf(String log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(String log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(int log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(int log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(short log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(short log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(byte log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(byte log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(long log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(long log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(char log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(char log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(boolean log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(boolean log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(float log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(float log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(double log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log));
    }

    public static void wtf(double log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    public static void wtf(Object log) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        if (log instanceof Throwable) {
            Throwable tr = (Throwable) log;
            Log.wtf(tag, tr);
        } else {
            Log.wtf(tag, String.valueOf(log));
        }
    }

    public static void wtf(Object log, Throwable tr) {
        if (!allowWtf)
            return;
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Log.wtf(tag, String.valueOf(log), tr);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    static void point(String path, String tag, String msg) {
        if (isSDAva()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("",
                    Locale.SIMPLIFIED_CHINESE);
            dateFormat.applyPattern("yyyy");
            path = path + dateFormat.format(date) + "/";
            dateFormat.applyPattern("MM");
            path += dateFormat.format(date) + "/";
            dateFormat.applyPattern("dd");
            path += dateFormat.format(date) + ".log";
            dateFormat.applyPattern("[yyyy-MM-dd HH:mm:ss]");
            String time = dateFormat.format(date);
            File file = new File(path);
            if (!file.exists())
                createDipPath(path);
            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(file, true)));
                out.write(time + " " + tag + " " + msg + "\r\n");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    Log.d("Error", "Save log error");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据文件路径 递归创建文件
     *
     * @param file 文件路径
     */
    private static void createDipPath(String file) {
        String parentFile = file.substring(0, file.lastIndexOf("/"));
        File file1 = new File(file);
        File parent = new File(parentFile);
        if (!file1.exists()) {
            parent.mkdirs();
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A little trick to reuse a formatter in the same thread
     */
    private static class ReusableFormatter {

        private final Formatter formatter;
        private final StringBuilder builder;

        ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        @NonNull
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();

        return formatter.format(msg, args);
    }

    public static boolean isSDAva() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                || Environment.getExternalStorageDirectory().exists();
    }

}
