package com.hy.library.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created time : 2018/4/3 11:42.
 *
 * @author HY
 */
@SuppressWarnings("unchecked")
public class AppTool {

    private static long lastClickTime;

    /**
     * 防止多次点击，造成重复操作
     */
    public static boolean isFastDoubleClick() {

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;


        if (0 < timeD && timeD < 500) {
            ToastWrapper.show("点击太快了");
            return true;
        }
        lastClickTime = time;

        return false;
    }

    public static boolean isContain(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);//获取所有已安装程序的包信息
//从packages中将包名字逐一取出
        if (packages != null) {
            for (PackageInfo packageInfo : packages) {
                if (ObjectsUtils.equals(packageName, packageInfo.packageName))
                    return true;
            }

        }
        return false;//判断pName中是否有目标程序的包名，有TRUE，没有FALSE
    }

    /**
     * 视图点击事件的封装，防止点击过快
     *
     * @param view     视图
     * @param listener 点击事件
     */
    public static void setViewClick(View view, final View.OnClickListener listener) {
        view.setOnClickListener(v -> {
            if (isFastDoubleClick()) return;
            listener.onClick(v);
        });
    }

    /**
     * 改变状态栏字体颜色为黑色, 要求MIUI6以上
     *
     * @param lightStatusBar 为真时表示黑色字体
     */
    @SuppressWarnings("JavaReflectionMemberAccess")
    public static void processMIUI(Activity activity, boolean lightStatusBar) {

        Window window = activity.getWindow();
        //针对安卓6.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && lightStatusBar) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, android.R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        String brand = getDeviceBrand();
        if ("Xiaomi".equalsIgnoreCase(brand)) {
            //针对小米
            Class clazz = window.getClass();
            try {
                int darkModeFlag;

                @SuppressLint("PrivateApi")
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");

                darkModeFlag = field.getInt(layoutParams);

                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);

                extraFlagField.invoke(window, lightStatusBar ? darkModeFlag : 0, darkModeFlag);

            } catch (Exception e) {
                Logger.d(e.getMessage(), e);
            }
        } else if ("Meizu".equalsIgnoreCase(brand)) {
            //针对魅族
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                value = lightStatusBar ? value | bit : value & bit;
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception e) {
                Logger.d(e.getMessage(), e);
            }

        }
    }

    /**
     * 获取状态栏高度
     *
     * @return 通知栏高度
     */
    public static int getStatusBarHeight(@Nullable Context context) {
        int statusBarHeight = 0;
        if (null == context) return 0;
        try {
            @SuppressLint("PrivateApi")
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object obj = clazz.newInstance();
            Field field = clazz.getField("status_bar_height");
            int temp = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(temp);
        } catch (Exception e) {
            Logger.e("Exception", e);
        }

        return statusBarHeight;
    }

    public static int getDividerCount(RecyclerView recyclerView) {
        int count;
        try {
            Field fields = RecyclerView.class.getDeclaredField("mItemDecorations");//获得对象所有属性
            fields.setAccessible(true);
            ArrayList<RecyclerView.ItemDecoration> dividers = (ArrayList<RecyclerView.ItemDecoration>) fields.get(recyclerView);
            fields.setAccessible(false);
            count = dividers.size();
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
            count = 0;
        }
        return count;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return Build.BRAND;
    }


    /**
     * 显示键盘
     *
     * @param context 上下文
     * @param view    视图
     */
    public static void showInputMethod(Context context, View view) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
            im.showSoftInput(view, 0);
        }
    }

    //隐藏虚拟键盘
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }


    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    public static void postDelay(Runnable action, long delay) {
        MAIN_HANDLER.postDelayed(action, delay);
    }

    public static void post(Runnable action) {
        postDelay(action, 0);
    }

    //判断是否存在NavigationBar
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            //反射获取SystemProperties类，并调用它的get方法
            @SuppressLint("PrivateApi")
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9

    ------------------------------------------------
    13(老)号段：130、131、132、133、134、135、136、137、138、139
    14(新)号段：145、147
    15(新)号段：150、151、152、153、154、155、156、157、158、159
    17(新)号段：170、171、173、175、176、177、178
    18(3G)号段：180、181、182、183、184、185、186、187、188、189
    */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return !TextUtils.isEmpty(mobiles) && mobiles.matches(telRegex);
    }

    public static String formatTime(int number) {
        if (number <= 9) {
            return "0" + number;
        } else return "" + number;
    }

    public static String formatMobile(String mobile) {
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    public static String formatNumber(int number) {
        if (number >= 10000) {
            BigDecimal bd = new BigDecimal(number / 10000f);
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
            return bd + "万";
        } else {
            return "" + number;
        }
    }

    public static ArrayList<String> list2StringList(Collection<?> collection) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Object datum : collection) {
            arrayList.add(datum.toString());
        }
        return arrayList;
    }

    /**
     * 点击复制到剪贴板
     *
     * @param context 获取剪贴板管理类对象
     * @param str     复制的文字
     */
    public static boolean clickCopy(Context context, String str) {
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null) {
            cmb.setPrimaryClip(ClipData.newPlainText(null, str));
        }
        return str.equals(getCMBStr(cmb));
    }


    @Nullable
    private static String getCMBStr(@Nullable ClipboardManager cmb) {
        if (null == cmb) return null;
        if (cmb.hasPrimaryClip()) {
            return cmb.getPrimaryClip().getItemAt(0).
                    getText().toString().trim();
        }
        return null;
    }

}
