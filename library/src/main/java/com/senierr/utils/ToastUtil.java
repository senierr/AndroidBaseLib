package com.senierr.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * 吐司工具类
 *
 * @author zhouchunjie
 * @date 2017/5/19
 */

public class ToastUtil {

    private static Toast toast = null;

    /**
     * 显示短时吐司
     *
     * @param context 上下文
     * @param resId 字符串ID
     */
    public static void showShort(@NonNull Context context, @StringRes int resId) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT);
        }
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示短时吐司
     *
     * @param context 上下文
     * @param message 文本
     */
    public static void showShort(@NonNull Context context, @NonNull String message) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 显示长时吐司
     *
     * @param context 上下文
     * @param resId 字符串ID
     */
    public static void showLong(@NonNull Context context, @StringRes int resId) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_LONG);
        }
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * 显示长时吐司
     *
     * @param context 上下文
     * @param message 文本
     */
    public static void showLong(@NonNull Context context, @NonNull String message) {
        if (toast == null) {
            toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);
        }
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
