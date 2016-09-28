package wistcat.overtime.util;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.text.DateFormat;
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
    public static final String FORMAT_DATE_TEMPLATE_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TEMPLATE_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TEMPLATE_TIME = "HH:mm:ss";
    public static final String FORMAT_DATE_TEMPLATE_CHN  = "yyyy年MM月dd日";
    /*
     * 1) String->Date: SimpleDateFormat.parse("1970-01-01 00:00:00").getTime() 结果为-28800000
     *    输入本地时间，返回本地时间（距离UTC之差）
     * 2) new Date(0) 结果为 1970-01-01 08:00:00
     *    输入本地时间，返回本地时间
     * 总结：Date与SimpleDateFormat本身只和系统时间相关，也依赖于系统所设时区，比如这里为GMT+8,
     *       因此，在时间表现形式转换时，不必担心对齐问题
     */
    public static final int FORMATE_DATE_OFFSET_MILLIS = 28800000;
    public static final String FORMAT_TIME_TEMPLATE_SUM  = "%d小时%d分%d秒";
    public static final String FORMAT_TIME_TEMPLATE_ACCUMULATE = "%02d:%02d:%02d";

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
        /* 获取的是系统时间 */
        return new Date(System.currentTimeMillis());
    }

    /**
     * 将字符串类型日期转换为Date类型日期
     */
    public static Date parseStringDate(@NonNull String template, @NonNull String date) throws ParseException {
        DateFormat format = new SimpleDateFormat(template, Locale.getDefault());
        return format.parse(date);
    }

    public static long parseTime(@NonNull String template, @NonNull String date) throws ParseException {
        return parseStringDate(template, date).getTime();
    }

    public static long parseTime(@NonNull String data) throws ParseException {
        return parseStringDate(FORMAT_DATE_TEMPLATE_FULL, data).getTime();
    }

    /**
     * 计算两个日期之差，过零点即为第二天<br/>
     * getTime()返回的是本地时间下 GMT的当前时间，
     * 比如在本地日期"1970-01-01 00:00:00"的时候，返回-28800000，
     * 因此需要将本地零点对齐到GMT零点时间，才能通过除法正确划分一天时间
     *
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
        return getTemplateTime(FORMAT_TIME_TEMPLATE_SUM, accumulated);
    }

    public static String getAccumulateTime(long accumulated) {
        return getTemplateTime(FORMAT_TIME_TEMPLATE_ACCUMULATE, accumulated);
    }

    public static String getTemplateTime(String template, long accumulated) {
        if (accumulated < 0) {
            throw new IllegalArgumentException();
        }
        accumulated /= 1000; // seconds
        final int hours = (int) (accumulated / 3600);
        accumulated %= 3600;
        final int minutes = (int) (accumulated / 60);
        accumulated %= 60;
        return String.format(template, hours, minutes, accumulated);
    }

    public static boolean isCursorEmpty(Cursor c) {
        return c == null || c.getCount() == 0;
    }

}
