package demo.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ClassName：DateTimeUtil
 * 类描述：   日期工具类
 * 操作人：wangshuang
 * 操作时间：2015-5-26 下午05:52:42
 *
 * @version 1.0
 */
public class DateTimeUtil {

    public static final String PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String getDateTimeByHour(long time) {
        DateFormat formatHour = new SimpleDateFormat("yyyyMMddHH");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return formatHour.format(c.getTime());
    }

    public static String getDateTimeByDay(long time) {
        DateFormat formatHour = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return formatHour.format(c.getTime());
    }

    public static String getDateTime(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return format.format(c.getTime());
    }

    public static String getDateTime(long time, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        return format.format(c.getTime());
    }

    public static long add(long time, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.add(field, amount);

        return c.getTimeInMillis();
    }

    public static long parse(String dateTime, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        try {
            Date date = format.parse(dateTime);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;

    }

    public static long currentTimeByHour() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long currentTimeByDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }

    public static long getDateByDay(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }

}
