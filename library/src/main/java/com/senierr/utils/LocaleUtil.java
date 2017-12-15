package com.senierr.utils;

import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * 多语言工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */

public class LocaleUtil {

    /**
     * 获取真实系统首选语言
     *
     * @return 语言
     */
    public static Locale getLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 判断是否是中文
     *
     * @return {@code true}: 中文<br>{@code false}: 非中文
     */
    public static boolean isZh() {
        Locale locale = getLocale();
        String language = locale.getLanguage().trim();
        return language.equalsIgnoreCase("zh");
    }

    /**
     * 判断是否是中国大陆
     *
     * @return {@code true}: 中国大陆<br>{@code false}: 非中国大陆
     */
    public static boolean isZhCN() {
        Locale locale = getLocale();
        String language = locale.getLanguage().trim();
        String country = locale.getCountry().toLowerCase().trim();
        return language.equalsIgnoreCase("zh") && country.equalsIgnoreCase("cn");
    }
}
