package com.senierr.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import java.util.List;

/**
 * 应用工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
public final class AppUtil {

    /**
     * 获取打开App的意图
     *
     * @param context 上下文
     * @param packageName 包名
     * @return intent
     */
    public static Intent getLaunchAppIntent(Context context, final String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 判断App是否安装
     *
     * @param context 上下文
     * @param action   action
     * @param category category
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, final String action, final String category) {
        Intent intent = new Intent(action);
        intent.addCategory(category);
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, 0);
        return info != null;
    }

    /**
     * 判断App是否安装
     *
     * @param context 上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, final String packageName) {
        return !StringUtil.isSpace(packageName) && getLaunchAppIntent(context, packageName) != null;
    }

    /**
     * 打开App
     *
     * @param context 上下文
     * @param packageName 包名
     */
    public static void launchApp(Context context, final String packageName) {
        if (StringUtil.isSpace(packageName)) return;
        context.startActivity(getLaunchAppIntent(context, packageName));
    }

    /**
     * 打开App
     *
     * @param activity    activity
     * @param packageName 包名
     * @param requestCode 请求值
     */
    public static void launchApp(final Activity activity, final String packageName, final int requestCode) {
        if (StringUtil.isSpace(packageName)) return;
        activity.startActivityForResult(getLaunchAppIntent(activity, packageName), requestCode);
    }

    /**
     * 打开设置
     *
     * @param context 上下文
     * @param packageName 包名
     */
    public static void openAppDetailsSettings(Context context, final String packageName) {
        if (StringUtil.isSpace(packageName)) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 获取App版本号
     *
     * @param context 上下文
     * @param packageName 包名
     */
    public static String getVersionName(Context context, final String packageName) {
        if (StringUtil.isSpace(packageName)) return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取VersionCode
     *
     * @param context 上下文
     * @param packageName 包名
     */
    public static int getVersionCode(Context context, final String packageName) {
        if (StringUtil.isSpace(packageName)) return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 判断App是否是系统应用
     *
     * @param context 上下文
     * @param packageName 包名
     */
    public static boolean isSystemApp(Context context, final String packageName) {
        if (StringUtil.isSpace(packageName)) return false;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断App是否处于前台
     *
     * @param context 上下文
     */
    public static boolean isAppForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> info = manager.getRunningAppProcesses();
            if (info == null || info.size() == 0) return false;
            for (ActivityManager.RunningAppProcessInfo aInfo : info) {
                if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return aInfo.processName.equals(context.getPackageName());
                }
            }
        }
        return false;
    }

    /**
     * 从Application中获取meta-data中String值
     *
     * @param context 上下文
     * @param key meta-data的name
     * @param defValue 默认值
     * @return meta-data的value
     */
    public static String getStringMetaDataFromApplication(Context context, String key, String defValue) {
        Bundle bundle = getMetaDataFromApplication(context.getPackageManager(), context.getPackageName());
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return defValue;
    }

    /**
     * 获取Application中的meta-data.
     *
     * @param packageManager 应用管理
     * @param packageName 包名
     * @return Bundle
     */
    public static Bundle getMetaDataFromApplication(PackageManager packageManager, String packageName) {
        Bundle bundle = null;
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.logE(AppUtil.class, Log.getStackTraceString(e));
        }
        return bundle;
    }

    /**
     * 根据key从Activity中返回的Bundle中获取value
     *
     * @param context 上下文
     * @param component 组件
     * @param key meta-data的name
     * @param defValue 默认值
     * @return meta-data的value
     */
    public static String getStringMetaDataFromActivity(Context context, ComponentName component, String key, String defValue) {
        Bundle bundle = getActivityMetaDataBundle(context.getPackageManager(), component);
        if (bundle != null && bundle.containsKey(key)) {
            return bundle.getString(key);
        }
        return defValue;
    }

    /**
     * 获取Activity中的meta-data.
     *
     * @param packageManager 应用管理
     * @param component 组件
     * @return
     */
    public static Bundle getActivityMetaDataBundle(PackageManager packageManager, ComponentName component) {
        Bundle bundle = null;
        try {
            ActivityInfo ai = packageManager.getActivityInfo(component,
                    PackageManager.GET_META_DATA);
            bundle = ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.logE(AppUtil.class, Log.getStackTraceString(e));
        }
        return bundle;
    }
}