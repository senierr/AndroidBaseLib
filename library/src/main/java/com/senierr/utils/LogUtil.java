package com.senierr.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * 日志工具类
 *
 * @author zhouchunjie
 * @date 2017/10/26
 */

public class LogUtil {

    private static boolean isDebug = true;

    public static void debug(boolean isDebug) {
        LogUtil.isDebug = isDebug;
    }

    public static void logV(Object tagObj, String msg) {
        if (isDebug) {
            if (tagObj == null || TextUtils.isEmpty(msg)) {
                return;
            }
            Log.v(getTag(tagObj), msg);
        }
    }

    public static void logD(Object tagObj, String msg) {
        if (isDebug) {
            Log.d(getTag(tagObj), msg);
        }
    }

    public static void logI(Object tagObj, String msg) {
        if (isDebug) {
            Log.i(getTag(tagObj), msg);
        }
    }

    public static void logW(Object tagObj, String msg) {
        if (isDebug) {
            Log.w(getTag(tagObj), msg);
        }
    }

    public static void logE(Object tagObj, String msg) {
        if (isDebug) {
            Log.e(getTag(tagObj), msg);
        }
    }

    private static String getTag(Object tagObj) {
        String tag;
        if (tagObj instanceof String) {
            tag = (String) tagObj;
        } else if (tagObj instanceof Class) {
            tag = ((Class) tagObj).getSimpleName();
        } else {
            tag = tagObj.getClass().getName();
            String[] split = tag.split("\\.");
            tag = split[split.length - 1].split("\\$")[0];
        }
        return tag;
    }
}
