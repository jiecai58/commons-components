package com.util.date;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  时间处理
 * @author Jie.cai58@gmail.com
 * @date 2019/9/19 14:16
 */

public class DateUtil {

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<SimpleDateFormat>();

    private static final Object OBJECT = new Object();

    private static final String D = "d";
    private static final String H = "h";
    private static final String M = "m";


    /**
     * 获取SimpleDateFormat
     *
     * @param pattern
     *            日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException
     *             异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String pattern)
            throws RuntimeException {
        SimpleDateFormat dateFormat = THREAD_LOCAL.get();
        if (dateFormat == null) {
            synchronized (OBJECT) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    THREAD_LOCAL.set(dateFormat);
                }
            }
        }
        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    /**
     * 获取日期中的某数值。如获取月份
     *
     * @param date
     *            日期
     * @param dateType
     *            日期格式
     * @return 数值
     */
    private static int getInteger(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }
        return num;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date
     *            日期字符串
     * @param dateType
     *            类型
     * @param amount
     *            数值
     * @return 计算后日期字符串
     */
    private static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = dateToString(myDate, dateStyle);
        }
        return dateString;
    }

    /**
     * 增加日期中某类型的某数值。如增加日期
     *
     * @param date
     *            日期
     * @param dateType
     *            类型
     * @param amount
     *            数值
     * @return 计算后日期
     */
    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }
        return myDate;
    }

    /**
     * 获取精确的日期
     *
     * @param timestamps
     *            时间long集合
     * @return 日期
     */
    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0;
        Map<Long, long[]> map = new HashMap<Long, long[]>(20);
        List<Long> absoluteValues = new ArrayList<Long>();

        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for (int i = 0; i < timestamps.size(); i++) {
                    for (int j = i + 1; j < timestamps.size(); j++) {
                        long absoluteValue = Math.abs(timestamps.get(i)
                                - timestamps.get(j));
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = { timestamps.get(i),
                                timestamps.get(j) };
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的。此时minAbsoluteValue为0
                // 因此不能将minAbsoluteValue取默认值0
                long minAbsoluteValue = -1;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = absoluteValues.get(0);
                    for (int i = 1; i < absoluteValues.size(); i++) {
                        if (minAbsoluteValue > absoluteValues.get(i)) {
                            minAbsoluteValue = absoluteValues.get(i);
                        }
                    }
                }

                if (minAbsoluteValue != -1) {
                    long[] timestampsLastTmp = map.get(minAbsoluteValue);

                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne
                                : dateTwo;
                    }
                }
            } else {
                timestamp = timestamps.get(0);
            }
        }

        if (timestamp != 0) {
            date = new Date(timestamp);
        }
        return date;
    }

    /**
     * 判断字符串是否为日期字符串
     *
     * @param date
     *            日期字符串
     * @return true or false
     */
    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null) {
            if (getDateStyle(date) != null) {
                isDate = true;
            }
        }
        return isDate;
    }

    /**
     * 获取日期字符串的日期风格。失敗返回null。
     *
     * @param date
     *            日期字符串
     * @return 日期风格
     */
    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap<Long, DateStyle>(20);
        List<Long> timestamps = new ArrayList<Long>();
        for (DateStyle style : DateStyle.values()) {
            if (style.isShowOnly()) {
                continue;
            }
            Date dateTmp = null;
            if (date != null) {
                try {
                    ParsePosition pos = new ParsePosition(0);
                    dateTmp = getDateFormat(style.getValue()).parse(date, pos);
                    if (pos.getIndex() != date.length()) {
                        dateTmp = null;
                    }
                } catch (Exception e) {
                }
            }
            if (dateTmp != null) {
                timestamps.add(dateTmp.getTime());
                map.put(dateTmp.getTime(), style);
            }
        }
        Date accurateDate = getAccurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = map.get(accurateDate.getTime());
        }
        return dateStyle;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date
     *            日期字符串
     * @return 日期
     */
    public static Date stringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return stringToDate(date, dateStyle);
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param pattern
     *            日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param dateStyle
     *            日期风格
     * @return 日期
     */
    public static Date stringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle != null) {
            myDate = stringToDate(date, dateStyle.getValue());
        }
        return myDate;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date
     *            日期
     * @param pattern
     *            日期格式
     * @return 日期字符串
     */
    public static String dateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception e) {
            }
        }
        return dateString;
    }

    /**
     * 将日期转化为日期字符串。失败返回null。
     *
     * @param date
     *            日期
     * @param dateStyle
     *            日期风格
     * @return 日期字符串
     */
    public static String dateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = dateToString(date, dateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param newPattern
     *            新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, String newPattern) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param newDateStyle
     *            新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle newDateStyle) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newDateStyle);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param olddPattern
     *            旧日期格式
     * @param newPattern
     *            新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, String olddPattern,
                                        String newPattern) {
        return dateToString(stringToDate(date, olddPattern), newPattern);
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param olddDteStyle
     *            旧日期风格
     * @param newParttern
     *            新日期格式
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle olddDteStyle,
                                        String newParttern) {
        String dateString = null;
        if (olddDteStyle != null) {
            dateString = stringToString(date, olddDteStyle.getValue(),
                    newParttern);
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param olddPattern
     *            旧日期格式
     * @param newDateStyle
     *            新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, String olddPattern,
                                        DateStyle newDateStyle) {
        String dateString = null;
        if (newDateStyle != null) {
            dateString = stringToString(date, olddPattern,
                    newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 将日期字符串转化为另一日期字符串。失败返回null。
     *
     * @param date
     *            旧日期字符串
     * @param olddDteStyle
     *            旧日期风格
     * @param newDateStyle
     *            新日期风格
     * @return 新日期字符串
     */
    public static String stringToString(String date, DateStyle olddDteStyle,
                                        DateStyle newDateStyle) {
        String dateString = null;
        if (olddDteStyle != null && newDateStyle != null) {
            dateString = stringToString(date, olddDteStyle.getValue(),
                    newDateStyle.getValue());
        }
        return dateString;
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param date
     *            日期
     * @param yearAmount
     *            增加数量。可为负数
     * @return 增加年份后的日期字符串
     */
    public static String addYear(String date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的年份。失败返回null。
     *
     * @param date
     *            日期
     * @param yearAmount
     *            增加数量。可为负数
     * @return 增加年份后的日期
     */
    public static Date addYear(Date date, int yearAmount) {
        return addInteger(date, Calendar.YEAR, yearAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param date
     *            日期
     * @param monthAmount
     *            增加数量。可为负数
     * @return 增加月份后的日期字符串
     */
    public static String addMonth(String date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的月份。失败返回null。
     *
     * @param date
     *            日期
     * @param monthAmount
     *            增加数量。可为负数
     * @return 增加月份后的日期
     */
    public static Date addMonth(Date date, int monthAmount) {
        return addInteger(date, Calendar.MONTH, monthAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param dayAmount
     *            增加数量。可为负数
     * @return 增加天数后的日期字符串
     */
    public static String addDay(String date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的天数。失败返回null。
     *
     * @param date
     *            日期
     * @param dayAmount
     *            增加数量。可为负数
     * @return 增加天数后的日期
     */
    public static Date addDay(Date date, int dayAmount) {
        return addInteger(date, Calendar.DATE, dayAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param hourAmount
     *            增加数量。可为负数
     * @return 增加小时后的日期字符串
     */
    public static String addHour(String date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的小时。失败返回null。
     *
     * @param date
     *            日期
     * @param hourAmount
     *            增加数量。可为负数
     * @return 增加小时后的日期
     */
    public static Date addHour(Date date, int hourAmount) {
        return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param minuteAmount
     *            增加数量。可为负数
     * @return 增加分钟后的日期字符串
     */
    public static String addMinute(String date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的分钟。失败返回null。
     *
     * @param date
     *            日期
     * @param minuteAmount
     *            增加数量。可为负数
     * @return 增加分钟后的日期
     */
    public static Date addMinute(Date date, int minuteAmount) {
        return addInteger(date, Calendar.MINUTE, minuteAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param date
     *            日期字符串
     * @param secondAmount
     *            增加数量。可为负数
     * @return 增加秒钟后的日期字符串
     */
    public static String addSecond(String date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 增加日期的秒钟。失败返回null。
     *
     * @param date
     *            日期
     * @param secondAmount
     *            增加数量。可为负数
     * @return 增加秒钟后的日期
     */
    public static Date addSecond(Date date, int secondAmount) {
        return addInteger(date, Calendar.SECOND, secondAmount);
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 年份
     */
    public static int getYear(String date) {
        return getYear(stringToDate(date));
    }

    /**
     * 获取日期的年份。失败返回0。
     *
     * @param date
     *            日期
     * @return 年份
     */
    public static int getYear(Date date) {
        return getInteger(date, Calendar.YEAR);
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 月份
     */
    public static int getMonth(String date) {
        return getMonth(stringToDate(date));
    }

    /**
     * 获取日期的月份。失败返回0。
     *
     * @param date
     *            日期
     * @return 月份
     */
    public static int getMonth(Date date) {
        return getInteger(date, Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 天
     */
    public static int getDay(String date) {
        return getDay(stringToDate(date));
    }

    /**
     * 获取日期的天数。失败返回0。
     *
     * @param date
     *            日期
     * @return 天
     */
    public static int getDay(Date date) {
        return getInteger(date, Calendar.DATE);
    }

    /**
     * 获取日期的小时。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 小时
     */
    public static int getHour(String date) {
        return getHour(stringToDate(date));
    }

    /**
     * 获取日期的小时。失败返回0。
     *
     * @param date
     *            日期
     * @return 小时
     */
    public static int getHour(Date date) {
        return getInteger(date, Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的分钟。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 分钟
     */
    public static int getMinute(String date) {
        return getMinute(stringToDate(date));
    }

    /**
     * 获取日期的分钟。失败返回0。
     *
     * @param date
     *            日期
     * @return 分钟
     */
    public static int getMinute(Date date) {
        return getInteger(date, Calendar.MINUTE);
    }

    /**
     * 获取日期的秒钟。失败返回0。
     *
     * @param date
     *            日期字符串
     * @return 秒钟
     */
    public static int getSecond(String date) {
        return getSecond(stringToDate(date));
    }

    /**
     * 获取日期的秒钟。失败返回0。
     *
     * @param date
     *            日期
     * @return 秒钟
     */
    public static int getSecond(Date date) {
        return getInteger(date, Calendar.SECOND);
    }

    /**
     * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
     *
     * @param date
     *            日期字符串
     * @return 日期
     */
    public static String getDate(String date) {
        return stringToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期。默认yyyy-MM-dd格式。失败返回null。
     *
     * @param date
     *            日期
     * @return 日期
     */
    public static String getDate(Date date) {
        return dateToString(date, DateStyle.YYYY_MM_DD);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     *
     * @param date
     *            日期字符串
     * @return 时间
     */
    public static String getTime(String date) {
        return stringToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
     *
     * @param date
     *            日期
     * @return 时间
     */
    public static String getTime(Date date) {
        return dateToString(date, DateStyle.HH_MM_SS);
    }

    /**
     * 获取日期的时间。默认yyyy-MM-dd HH:mm:ss格式。失败返回null。
     *
     * @param date
     *            日期字符串
     * @return 时间
     */
    public static String getDateTime(String date) {
        return stringToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取日期的时间。默认yyyy-MM-dd HH:mm:ss格式。失败返回null。
     *
     * @param date
     *            日期
     * @return 时间
     */
    public static String getDateTime(Date date) {
        return dateToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date
     *            日期字符串
     * @return 星期
     */
    public static Week getWeek(String date) {
        Week week = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            week = getWeek(myDate);
        }
        return week;
    }

    /**
     * 获取日期的星期。失败返回null。
     *
     * @param date
     *            日期
     * @return 星期
     */
    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (weekNumber) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
                break;
            default:
        }
        return week;
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date
     *            日期字符串
     * @param otherDate
     *            另一个日期字符串
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(stringToDate(date), stringToDate(otherDate));
    }

    /**
     * @param date
     *            日期
     * @param otherDate
     *            另一个日期
     * @return 相差天数。如果失败则返回-1
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        int num = -1;
        Date dateTmp = stringToDate(DateUtil.getDate(date),
                DateStyle.YYYY_MM_DD);
        Date otherDateTmp = stringToDate(DateUtil.getDate(otherDate),
                DateStyle.YYYY_MM_DD);
        if (dateTmp != null && otherDateTmp != null) {
            long time = Math.abs(dateTmp.getTime() - otherDateTmp.getTime());
            num = (int) (time / (24 * 60 * 60 * 1000));
        }
        return num;
    }

    public static Long dateDiff(long startTime, long endTime,String str) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        long ns = 1000;
        long diff;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        diff = endTime - startTime;
        day = diff / nd;
        hour = diff % nd / nh + day * 24;
        min = diff % nd % nh / nm + day * 24 * 60;
        sec = diff % nd % nh % nm / ns;
       /* System.out.println("时间相差：" + day + "天" + (hour - day * 24) + "小时"
                + (min - day * 24 * 60) + "分钟" + sec + "秒。");*/
        if (str.equalsIgnoreCase(D)) {
            return day;
        } else if(str.equalsIgnoreCase(H)){
            return hour;
        }else if(str.equalsIgnoreCase(M)){
            return min;
        }else {
            return sec;
        }
    }

    /**
     * 获取期间的年龄
     *
     * @param date
     * @param otherDate
     * @return
     *
     *         2014-12-2 下午06:45:02 段
     *
     * @return String
     */
    public static String getAge(Date date, Date otherDate) {
        int dis = DateUtil.getIntervalDays(new Date(), otherDate);
        int year = dis / 365;
        int month = dis % 365 / 30;
        int day = dis % 365 % 31;
        String age = (year > 0 ? year + "岁" : "")
                + (month > 0 ? month + "个月" : "") + (day + "天");
        return age;
    }


    /**
     * 获取当前时间之前或之后几小时 hour
     * @param hour
     * @return
     */

    public static final String getTimeByHour(int hour) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + hour);

        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

     public static final String getDateH(Date date) {
         return dateToString(date, DateStyle.YYYY_MM_DD_HH_MM_SS);
     }

    public static final Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    public static final Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    @Override
    protected void finalize() throws Throwable{
        THREAD_LOCAL.remove();
    }

}
