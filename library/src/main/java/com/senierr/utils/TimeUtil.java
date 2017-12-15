package com.senierr.utils;

import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间工具类
 *
 *                                             HH:mm    15:44
 *                                            h:mm a    3:44 下午
 *                                           HH:mm z    15:44 CST
 *                                           HH:mm Z    15:44 +0800
 *                                        HH:mm zzzz    15:44 中国标准时间
 *                                          HH:mm:ss    15:44:40
 *                                        yyyy-MM-dd    2016-08-12
 *                                  yyyy-MM-dd HH:mm    2016-08-12 15:44
 *                               yyyy-MM-dd HH:mm:ss    2016-08-12 15:44:40
 *                          yyyy-MM-dd HH:mm:ss zzzz    2016-08-12 15:44:40 中国标准时间
 *                     EEEE yyyy-MM-dd HH:mm:ss zzzz    星期五 2016-08-12 15:44:40 中国标准时间
 *                          yyyy-MM-dd HH:mm:ss.SSSZ    2016-08-12 15:44:40.461+0800
 *                        yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
 *                      yyyy.MM.dd G 'at' HH:mm:ss z    2016.08.12 公元 at 15:44:40 CST
 *                                            K:mm a    3:44 下午
 *                                  EEE, MMM d, ''yy    星期五, 八月 12, '16
 *                             hh 'o''clock' a, zzzz    03 o'clock 下午, 中国标准时间
 *                      yyyyy.MMMMM.dd GGG hh:mm aaa    02016.八月.12 公元 03:44 下午
 *                        EEE, d MMM yyyy HH:mm:ss Z    星期五, 12 八月 2016 15:44:40 +0800
 *                                     yyMMddHHmmssZ    160812154440+0800
 *                        yyyy-MM-dd'T'HH:mm:ss.SSSZ    2016-08-12T15:44:40.461+0800
 * EEEE 'DATE('yyyy-MM-dd')' 'TIME('HH:mm:ss')' zzzz    星期五 DATE(2016-08-12) TIME(15:44:40) 中国标准时间
 *
 * @author zhouchunjie
 * @date 2017/10/26
 */

public class TimeUtil {

    /**
     * 格式化日期
     *
     * @param millis 毫秒
     * @param pattern 格式
     * @return 日期字符串
     */
    public String format(long millis, @NonNull String pattern) {
        return format(new Date(millis), pattern, Locale.getDefault());
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @return 日期字符串
     */
    public String format(@NonNull Date date, @NonNull String pattern) {
        return format(date, pattern, Locale.getDefault());
    }

    /**
     * 格式化日期
     *
     * @param date 日期
     * @param pattern 格式
     * @param locale 区域
     * @return 日期字符串
     */
    public String format(@NonNull Date date, @NonNull String pattern, @NonNull Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        return simpleDateFormat.format(date);
    }

    /**
     * 解析时间
     *
     * @param time 时间
     * @param pattern 格式
     * @return 毫秒
     */
    public long parse(@NonNull String time, @NonNull String pattern) {
        return parse(time, pattern, Locale.getDefault());
    }

    /**
     * 解析时间
     *
     * @param time 时间
     * @param pattern 格式
     * @param locale 区域
     * @return 毫秒
     */
    public long parse(@NonNull String time, @NonNull String pattern, @NonNull Locale locale) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, locale);
        try {
            return simpleDateFormat.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取星期
     * 1~7: 星期日~星期六
     *
     * @param date 日期
     * @return 星期
     */
    private int getWeek(@NonNull Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
