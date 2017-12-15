package com.senierr.utils;

import android.Manifest;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;

/**
 * 网络工具类
 *
 * @author zhouchunjie
 * @date 2017/9/14
 */

public class NetworkUtil {

    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return  {@code true}: 可用<br>{@code false}: 不可用
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            return info != null && info.isConnected();
        }
        return false;
    }
}
