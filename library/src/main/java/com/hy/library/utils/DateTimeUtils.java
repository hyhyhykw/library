package com.hy.library.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created time : 2018/5/28 18:09.
 *
 * @author HY
 */
public class DateTimeUtils {
    private static final Calendar CALENDAR = Calendar.getInstance();

    public static int getCurrentHour() {
        return CALENDAR.get(Calendar.HOUR_OF_DAY);
    }


    @NonNull
    public static String getCurrentDate() {
        return getCurrentYear() + "-"
                + AppTool.formatTime(getMonthInt() + 1)
                + "-" + AppTool.formatTime(getDay());
    }

    public static int getCurrentYear() {
        return CALENDAR.get(Calendar.YEAR);
    }

    public static int getMonthInt() {
        return CALENDAR.get(Calendar.MONTH);
    }

    public static int getDay() {
        return CALENDAR.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据日期获取星座
     *
     * @param month 月
     * @param day   日
     * @return 星座
     */
    public static String getConstellation(int month, int day) {
        String[] astro = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座",
                "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
        int[] arr = new int[]{20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};// 两个星座分割日
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return astro[index];
    }

    public static int[] getDateArray(String str) {
        int[] arr = new int[3];
        try {
            String[] split = str.split("-", 3);
            arr[0] = Integer.parseInt(split[0]);
            if (split[1].startsWith("0") && split[1].length() > 1) {
                split[1] = split[1].substring(1);
            }
            if (split[2].startsWith("0") && split[2].length() > 1) {
                split[2] = split[2].substring(1);
            }
            arr[1] = Integer.parseInt(split[1]);
            arr[2] = Integer.parseInt(split[2]);
        } catch (Exception e) {
            Logger.e(e.getMessage(), e);
        }
        return arr;
    }

    public static String stamp2date() {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("M-dd");//这个是你要转成后的时间的格式
        return sdf.format(new Date());
    }

    public static String datetime2time(String datetime) {
        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyy-MM-dd HH:mm");
        try {
            Date parse = sdf.parse(datetime);
            sdf.applyPattern("HH:mm");
            return sdf.format(parse);
        } catch (ParseException e) {
            return "";
        }
    }

    public static long stamp() {

        return System.currentTimeMillis() / 1000;
    }

    /**
     * 将生日字符串转为年月日数组
     *
     * @param birthday 生日字符串
     * @return 年月日数组
     */
    public static int[] birthday2date(String birthday) {
        int[] date = new int[3];

        SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getInstance();
        sdf.applyPattern("yyyy-MM-dd");
        try {
            Date parse = sdf.parse(birthday);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parse);
            int year = calendar.get(Calendar.YEAR);
            int monthInt = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            date[0] = year;
            date[1] = monthInt + 1;
            date[2] = day;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }
}
