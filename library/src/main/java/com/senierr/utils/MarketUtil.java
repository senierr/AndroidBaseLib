package com.senierr.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * 应用市场工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */

public class MarketUtil {

    /**
     * 跳转到指定应用市场
     *
     * @param context 上下文
     * @param packageName 需要跳转的应用包名
     * @param storePackageName 应用市场包名
     * @return {@code true}: 跳转成功<br>{@code false}: 跳转失败
     */
    public static boolean goToMarket(Context context, String packageName, String storePackageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if (!TextUtils.isEmpty(storePackageName)) {
                goToMarket.setPackage(storePackageName);
            }
            context.startActivity(goToMarket);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
