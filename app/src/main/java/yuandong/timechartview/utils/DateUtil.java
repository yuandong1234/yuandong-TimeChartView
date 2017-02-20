package yuandong.timechartview.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * 日期操作工具类
 * Created by yuandong on 2017/2/18.
 */

public class DateUtil {
    private static  String TAG=DateUtil.class.getSimpleName();

        // 一小时的ms数
        public static final long HOUR = 3600 * 1000;
        // 一天的ms数
        public static final long DAY = HOUR * 24;

        public static final String DATE = "yyyy-MM-dd";
        public static final String DATE1 = "yyyy年MM月dd日";
        public static final String DATE2 = "MM月dd日 E HH:mm";
        public static final String DATE3 = "MM月dd日";
        public static final String DATE5 = "MM月dd日 E";
        public static final String DATE4 = "MM.dd";
        public static final String DATE6 = "MM/dd";
        public static final String DATE_YM = "yyyy-MM";
        public static final String DATE_YM2= "yyyy.MM";
        public static final String DATE_DOT = "yyyy.MM.dd";
        public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_HOUR_MINUTE = "HH:mm";
        public static final String DATE_YEAR = "yyyy";
        public static final String DATE_MONTH = "MM";
        public static final String DATE_MONTH2 = "MM月";
        public static final String DATE_DAY = "dd";
        public static final String DATE_HOUR = "HH";
        public static final String DATE_MINUTE = "mm";
        public static final String DATE_SECOND = "ss";
        public static final String DATE_MILLISECOND = "ms";
        public static final String DATE_HOUR_MINUTE_A = "hh:mm a"; //12小时制带AM和PM
        public static final String DATE_HOUR_MINUTE_NO = "hh:mm";
        public static final String DATE_HOUR_MINUTE_NO1 = "HH:mm";
        public static final String DATE_A_HOUR_MINUTE = "a hh:mm"; //12小时制带AM和PM
        public static Calendar calendar = Calendar.getInstance();

        /**
         * 将指定格式的时间转化为另外一种格式的时间串
         *
         * @param oldFormat 需要转化的时间格式
         * @param newFormat 转化后的时间格式
         * @param time      指定格式的时间字符串
         * @return 标准时间格式的字符串
         * <p/>
         * 24H 转 12H 三星手机会报错! 请使用changeTime2(...)
         */
        public static String changeTime(String oldFormat, String newFormat, String time) {
            try {
                if (null == time && "".equals(time)) {
                    return time;
                }
                SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat, Locale.getDefault());
                SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.getDefault());
                Date date = oldDateFormat.parse(time);
                return newDateFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return time;
        }

        /**
         * 使用英语环境的Locale 转换时间格式
         *
         * @param oldFormat
         * @param newFormat
         * @param time
         * @return
         */
        public static String changeTime2(String oldFormat, String newFormat, String time) {
            try {
                if (null == time && "".equals(time)) {
                    return time;
                }
                SimpleDateFormat oldDateFormat = new SimpleDateFormat(oldFormat, Locale.ENGLISH);
                SimpleDateFormat newDateFormat = new SimpleDateFormat(newFormat, Locale.ENGLISH);
                Date date = oldDateFormat.parse(time);
                return newDateFormat.format(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return time;
        }

        /**
         * 获取当前年（yyyy)
         *
         * @return
         */
        public static String getCurrentYear() {
            SimpleDateFormat newFormatYear = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            return newFormatYear.format(new Date());
        }

        /**
         * 获得当前月
         *
         * @return
         */
        public static String getCurrentMonth() {
            SimpleDateFormat newFormatYear = new SimpleDateFormat("MM月", Locale.ENGLISH);
            return newFormatYear.format(new Date());

        }

        /**
         * 不同类型的字符串转换成月份（month）（比如 ：yyyy-MM-dd  2016-10-23  转成 10月）
         *
         * @param oldFormat
         * @param time
         * @return
         */
        public static String changeToMonth(String oldFormat, String time) {
            return convertDateToString(DATE_MONTH2, convertStringToDate(oldFormat, time));
        }

        /**
         * This method generates a string representation of a date/time in the
         * format you specify on input
         *
         * @param aMask   the date pattern the string is in
         * @param strDate a string representation of a date
         * @return a converted Date object
         * @throws ParseException when String doesn't match the expected format
         * @see SimpleDateFormat
         */
        public static Date convertStringToDate(String aMask, String strDate) {
            if (TextUtils.isEmpty(strDate)) {
                return null;
            }
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date date = null;
            try {
                date = df.parse(strDate);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return date;
        }

        /**
         * 将日期转换为字符串
         *
         * @param aMask 格式字符串
         * @param date  日期
         * @return
         * @throws ParseException
         */
        public static String convertDateToString(String aMask, Date date) {
            SimpleDateFormat sdf = new SimpleDateFormat(aMask, Locale.ENGLISH);
            return sdf.format(date);
        }

        /**
         * 得到两个日期间隔时间
         *
         * @param date1 日期1
         * @param date2 日期2
         * @param type  时间间隔：毫秒、DATE_MILLISECOND 秒、DATE_SECOND  分钟、DATE_MINUTE 小时、DATE_HOUR 天、DATE_DAY
         * @return 两个日期之间间隔的时间（ms/s/m/H/d）
         */
        public static long betweenDate(Date date1, Date date2, String type) {
            long day = Math.abs((date1.getTime() - date2.getTime()));
            if (type.equals(DATE_SECOND)) {
                day /= (1000);
            } else if (type.equals(DATE_MINUTE)) {
                day /= (60 * 1000);
            } else if (type.equals(DATE_HOUR)) {
                day /= (60 * 60 * 1000);
            } else if (type.equals(DATE_DAY)) {
                day /= (24 * 60 * 60 * 1000);
            }
            return day;
        }

        /**
         * 得到两个日期间隔时间
         *
         * @param aMask 时间格式字符串
         * @param Date1 指定格式的日期字符串1
         * @param Date2 指定格式的日期字符串2
         * @param type  时间间隔：毫秒、DATE_MILLISECOND 秒、DATE_SECOND  分钟、DATE_MINUTE 小时、DATE_HOUR 天、DATE_DAY
         * @return 两个日期之间间隔的时间（ms/s/m/H/d）
         * @throws ParseException
         */
        public static long betweenDate(String aMask, String Date1, String Date2, String type) {
            return betweenDate(convertStringToDate(aMask, Date1),
                    convertStringToDate(aMask, Date2), type);
        }

        /**
         * 日期增加或者减少秒，分钟，天，月，年
         *
         * @param date   源日期
         * @param type   类型
         * @param offset （整数）
         * @return 增加或者减少之后的日期
         */
        public static Date addDate(Date date, String type, int offset) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(date);
            if (type.equals(DATE_MILLISECOND)) {
                gc.add(GregorianCalendar.MILLISECOND, offset);
            } else if (type.equals(DATE_SECOND)) {
                gc.add(GregorianCalendar.SECOND, offset);
            } else if (type.equals(DATE_MINUTE)) {
                gc.add(GregorianCalendar.MINUTE, offset);
            } else if (type.equals(DATE_HOUR)) {
                gc.add(GregorianCalendar.HOUR, offset);
            } else if (type.equals(DATE_DAY)) {
                gc.add(GregorianCalendar.DATE, offset);
            } else if (type.equals(DATE_MONTH)) {
                gc.add(GregorianCalendar.MONTH, offset);
            } else if (type.equals(DATE_YEAR)) {
                gc.add(GregorianCalendar.YEAR, offset);
            }
            return gc.getTime();
        }

        /**
         * 日期增加或者减少秒，分钟，天，月，年
         *
         * @param aMask   格式字符串
         * @param srcDate 源时间字符串
         * @param type    日期修改类型
         * @param offset  偏移量
         * @return 增加或者减少之后的日期字符串
         * @throws ParseException
         */
        public static String addDate(String aMask, String srcDate, String type,
                                     int offset) {
            if (TextUtils.isEmpty(srcDate)) {
                return null;
            }

            return convertDateToString(aMask,
                    addDate(convertStringToDate(aMask, srcDate), type, offset));
        }

        /**
         * 获取两个日期之间所有的天（yyyy-MM-dd）
         *
         * @param aMask
         * @param date1
         * @param date2
         * @return
         * @throws ParseException
         */
        public static List<String> getBetweenDates(String aMask, String date1,
                                                   String date2) {
            List<String> list = new ArrayList<String>();
            if (!date1.equals(date2)) {
                String tmp;
                if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
                    tmp = date1;
                    date1 = date2;
                    date2 = tmp;
                }
                tmp = addDate(aMask, date1, DATE_DAY, 0);

                while (tmp.compareTo(date2) <= 0) {
                    list.add(tmp);
                    tmp = addDate(aMask, tmp, DATE_DAY, 1);
                }
            } else {
                list.add(date1);
            }
            return list;
        }

        /**
         * 获取一天中所有的整点（yyyy-MM-dd HH:00:00）
         *
         * @param aMask
         * @param date
         * @return
         * @throws ParseException
         */
        public static ArrayList<String> getAllHourOfDay(String aMask, String date) {
            ArrayList<String> list = new ArrayList<String>();
            String tmp = changeTime(aMask, DATE, date) + " 00:00:00";
            list.add(tmp);
            for (int i = 0; i < 23; i++) {
                tmp = addDate(DATE_TIME, tmp, DATE_HOUR, 1);
                if (tmp.compareTo(getNowTime(DATE_TIME)) > 0) {
                    break;
                }
                list.add(tmp);
            }
            return list;
        }

        /**
         * 比较两个日期的大小 author:sdarmy
         *
         * @param date1
         * @param date2
         * @return date1 > date2:1  date1 < date2:-1  date1 = date2:0
         */
        public static int compareDate(Date date1, Date date2) {
            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() < date2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        }

        /**
         * 比较两个日期的大小 author:sdarmy
         *
         * @param aMask
         * @param date1
         * @param date2
         * @return date1 > date2:1  date1 < date2:-1  date1 = date2:0
         */
        public static int compareDate(String aMask, String date1, String date2) {
            if (TextUtils.isEmpty(date1) && TextUtils.isEmpty(date2)) {
                return 0;
            } else if (TextUtils.isEmpty(date1)) {
                return -1;
            } else if (TextUtils.isEmpty(date2)) {
                return 1;
            }
            if (convertStringToDate(aMask, date1).getTime() > convertStringToDate(aMask, date2).getTime()) {
                return 1;
            } else if (convertStringToDate(aMask, date1).getTime() < convertStringToDate(aMask, date2).getTime()) {
                return -1;
            } else {
                return 0;
            }
        }

        /**
         * 取得当前日期所在周的第一天(SUNDAY)
         *
         * @param date 日期
         * @return 当前日期所在周的第一天日期
         */
        public static Date getFirstDayOfWeek(Date date) {
            Calendar c = new GregorianCalendar();
            c.setFirstDayOfWeek(Calendar.SUNDAY);
            c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
            return c.getTime();
        }

        /**
         * 取得当前日期所在周的第一天(SUNDAY)
         *
         * @param aMask 日期格式
         * @param date  格式化的日期字符串
         * @return 当前日期所在周的第一天格式化的日期字符串
         * @throws ParseException
         */
        public static String getFirstDayOfWeek(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            Date firstDay = getFirstDayOfWeek(currentDay);
            return df.format(firstDay);
        }

        /**
         * 取得当前日期所在周的最后一天
         *
         * @param date 日期
         * @return 当前日期所在周的最后一天
         */
        public static Date getLastDayOfWeek(Date date) {
            Calendar c = new GregorianCalendar();
            c.setFirstDayOfWeek(Calendar.SUNDAY);
            c.setTime(date);
            c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
            return c.getTime();
        }

        /**
         * 取得当前日期所在周的最后一天
         *
         * @param aMask 日期格式
         * @param date  格式化的日期字符串
         * @return 当前日期所在周的最后一天格式化的日期字符串
         * @throws ParseException
         */
        public static String getLastDayOfWeek(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            Date lastDay = getLastDayOfWeek(currentDay);
            return df.format(lastDay);
        }

        /**
         * 取得当前日期所在月的第一天
         *
         * @param date
         * @return
         */
        public static Date getFirstDayOfMonth(Date date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            return calendar.getTime();
        }

        /**
         * 取得当前日期所在月的第一天
         *
         * @param aMask
         * @param date
         * @return
         * @throws ParseException
         */
        public static String getFirstDayOfMonth(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            Date firstDay = getFirstDayOfMonth(currentDay);
            return df.format(firstDay);
        }

        /**
         * 取得当前日期所在月的最后一天
         *
         * @param date
         * @return
         */
        public static Date getLastDayOfMonth(Date date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - 1);
            return calendar.getTime();
        }

        /**
         * 取得当前日期所在月的最后一天
         *
         * @param aMask
         * @param date
         * @return
         * @throws ParseException
         */
        public static String getLastDayOfMonth(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            Date lastDay = getLastDayOfMonth(currentDay);
            return df.format(lastDay);
        }

        /**
         * 获取某月的天数
         *
         * @param aMask
         * @param date
         * @return
         * @throws ParseException
         */
        public static int getDaysOfMonth(String aMask, String date) {
            Calendar calendar = new GregorianCalendar();
            try {
                SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
                Date currentDay = df.parse(date);
                calendar.setTime(currentDay);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        /**
         * 取得当前日期是所在周的第几天
         * 日 - 1、一 - 2、二 - 3、三 - 4、四 - 5、五 - 6、六 - 7
         *
         * @param date
         * @return
         */
        public static int getDayOfWeek(Date date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_WEEK);
        }

        /**
         * 取得当前日期是所在周的第几天
         * 日 - 1、一 - 2、二 - 3、三 - 4、四 - 5、五 - 6、六 - 7
         *
         * @param date
         * @return
         */
        public static int getDayOfWeek(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return getDayOfWeek(currentDay);
        }

        /**
         * 取得当前日期是所在月的第几天
         *
         * @param date
         * @return
         */
        public static int getDayOfMonth(Date date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            return calendar.get(Calendar.DAY_OF_MONTH);
        }

        /**
         * 取得当前日期是所在月的第几天
         *
         * @param date
         * @return
         */
        public static int getDayOfMonth(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            Date currentDay = null;
            try {
                currentDay = df.parse(date);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return getDayOfMonth(currentDay);
        }

        /**
         * 取得当前日期所在周的所有天的日期
         *
         * @param aMask 当前日期格式
         * @param date  当前日期
         * @return List<String> 当前日期所在周的所有天的日期集合
         * @throws ParseException
         */
        public static List<String> getAllDaysOfWeek(String aMask, String date) {
            String lastDayOfWeek = getLastDayOfWeek(aMask, date);
            String nowDate = convertDateToString(aMask, new Date());
            if (compareDate(aMask, nowDate, lastDayOfWeek) < 0) {
                lastDayOfWeek = nowDate;
            }
            return getBetweenDates(aMask, getFirstDayOfWeek(aMask, date),
                    lastDayOfWeek);
        }

        /**
         * 取得当前日期所在月的所有天的日期
         *
         * @param aMask 当前日期格式
         * @param date  当前日期
         * @return List<String> 当前日期所在月的所有天的日期集合
         * @throws ParseException
         */
        public static List<String> getAllDaysOfMonth(String aMask, String date) {
            String firstDayOfMonth = getFirstDayOfMonth(aMask, date);
            String lastDayOfMonth = getLastDayOfMonth(aMask, date);
            String nowDate = getNowTime(aMask);
            if (compareDate(aMask, nowDate, lastDayOfMonth) < 0) {
                lastDayOfMonth = nowDate;
            }
            return getBetweenDates(aMask, firstDayOfMonth, lastDayOfMonth);
        }

        /**
         * 处理日期和月 不够10加个0
         */
        public static String change(int data) {
            return data < 10 ? "0" + data : data + "";
        }

        /**
         * 判断当前时间是不是整点
         *
         * @param date
         * @return
         */
        public static boolean isWholePointTime(String aMask, String date) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.ENGLISH);
            try {
                return 0 == df.parse(date).getTime() % (1000 * 60 * 60);
            } catch (ParseException e) {
                Log.e(TAG,e+"");
            }
            return false;
        }

        /**
         * 获取当前系统时间
         *
         * @return
         */
        public static String getNowTime(String aMask) {
            SimpleDateFormat df = new SimpleDateFormat(aMask, Locale.getDefault());
            return df.format(new Date());
        }

        /**
         * 获取当前系统时间数组
         *
         * @return
         */
        public static int[] getNowTimeByte() {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = (c.get(Calendar.MONTH) + 1);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);

            year = Integer.parseInt(String.valueOf(year).substring(2));

            return new int[]{year, month, day, hour, minute, second};
        }

        /**
         * 手机时间格式
         *
         * @param context
         * @return
         */
        public static boolean is24HourFormat(Context context) {
            String timeFormat = android.provider.Settings.System.getString(context.getContentResolver(),
                    android.provider.Settings.System.TIME_12_24);
            if (!TextUtils.isEmpty(timeFormat) && timeFormat.equals("24")) {
                return true;
            } else {//if (timeFormat.equals("12"))
                return false;
            }
        }

        /**
         * 获指定时间毫秒
         *
         * @return
         */
        public static long getTimeMS(String aMask, String time) {
            Date date = convertStringToDate(aMask, time);
            return date.getTime();
        }

        /**
         * 指定时转化字符串
         *
         * @return
         */
        public static String getTime(String aMask, Date date) {
            return new SimpleDateFormat(aMask, Locale.getDefault()).format(date);
        }


}
