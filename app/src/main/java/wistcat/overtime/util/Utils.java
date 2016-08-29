package wistcat.overtime.util;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 工具类
 *
 * @author wistcat 2016/8/29
 */
public class Utils {

    // Date相关
    public static final String FORMAT_DATE_TEMPLATE_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final String FORMAT_DATE_TEMPLATE_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TEMPLATE_TIME = "hh:mm:ss";
    public static final String FORMAT_DATE_TEMPLATE_CHN  = "yyyy年MM月dd日";
    /* 计算日期差时，用于对齐格林尼治时间，默认本地+8时区初始值为-28800000 */
    public static final int FORMATE_DATE_OFFSET_MILLIS = 28800000;
    public static final String FORMAT_TIME_TEMPLATE_SUM  = "{0}小时{1}分{2}秒";

    //
    private Utils(){}

    // Date相关
    /**
     * 获取当前格式化的日期
     *
     * @param template 日期模板
     * @return 字符串类型的日期
     */
    public static String getDate(String template) {
        if (TextUtils.isEmpty(template)) {
            template = FORMAT_DATE_TEMPLATE_CHN;
        }
        DateFormat format = new SimpleDateFormat(template, Locale.getDefault());
        return format.format(getDate());
    }

    /**
     * 获取当前日期
     *
     * @return 返回Date类型
     */
    public static Date getDate() {
        // 1970-01-01 00:00:00 > -28800000 ( 8 * 60 * 60 * 1000)
        return new Date(System.currentTimeMillis());
    }

    /**
     * 将字符串类型日期转换为Date类型日期
     */
    public static Date parseStringDate(@NonNull String template, @NonNull String date) throws ParseException {
        DateFormat format = new SimpleDateFormat(template, Locale.getDefault());
        return format.parse(date);
    }

    /**
     * 计算两个日期之差，过零点即为第二天
     */
    public static int dateDiff(@NonNull Date from, @NonNull Date to) {
        // 1 day = 24 * 60 * 60 * 1000 = 86400000 milliseconds
        final int DAY = 86400000;
        final int fromDay = (int) ((from.getTime() + FORMATE_DATE_OFFSET_MILLIS) / DAY);
        final int toDay = (int) ((to.getTime() + FORMATE_DATE_OFFSET_MILLIS) / DAY);
        return Math.abs(toDay - fromDay);
    }

    /**
     * 将累计时间转换为"时分秒"格式的字符串
     *
     * @param accumulated 累计时间，单位 ms
     * @return 格式化时间
     */
    public static String getSumTime(long accumulated) {
        if (accumulated < 0) {
            throw new IllegalArgumentException();
        }
        accumulated /= 1000; // seconds
        final int hours = (int) (accumulated / 3600);
        accumulated %= 3600;
        final int minutes = (int) (accumulated / 60);
        accumulated %= 60;
        return MessageFormat.format(FORMAT_TIME_TEMPLATE_SUM, hours, minutes, accumulated);
    }

}
