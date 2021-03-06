package com.senierr.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 缓存工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */

public class CacheUtil {

    /**
     * 清除所有缓存
     *
     * @param context 上下文
     */
    public static void clearAllCache(Context context) {
        clearInternalCache(context);
        clearExternalCache(context);
    }

    /**
     * 清除内部缓存
     *
     * @param context 上下文
     */
    public static void clearInternalCache(Context context) {
        FileUtil.deleteAllInDir(getInternalCacheDirectory(context, null));
    }

    /**
     * 清除外部缓存
     *
     * @param context 上下文
     */
    public static void clearExternalCache(Context context) {
        FileUtil.deleteAllInDir(getExternalCacheDirectory(context, null));
    }

    /**
     * 获取所有缓存大小
     *
     * @param context 上下文
     * @return 缓存大小
     */
    public static long getTotalCacheSize(Context context) {
        long cacheSize = FileUtil.getFileSize(getInternalCacheDirectory(context, null));
        cacheSize += FileUtil.getFileSize(getExternalCacheDirectory(context, null));
        return cacheSize;
    }

    /**
     * 获取应用专属缓存目录
     * android 4.4及以上系统不需要申请SD卡读写权限
     * 因此也不用考虑6.0系统动态申请SD卡读写权限问题，切随应用被卸载后自动清空 不会污染用户存储空间
     * @param context 上下文
     * @param type 文件夹类型 可以为空，为空则返回API得到的一级目录
     * @return 缓存文件夹 如果没有SD卡或SD卡有问题则返回内存缓存目录，否则优先返回SD卡缓存目录
     */
    public static File getCacheDirectory(Context context, String type) {
        File appCacheDir = getExternalCacheDirectory(context,type);
        if (appCacheDir == null) {
            appCacheDir = getInternalCacheDirectory(context,type);
        }

        if (appCacheDir == null) {
            LogUtil.logE(CacheUtil.class, "getCacheDirectory fail ,the reason is mobile phone unknown exception!");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                LogUtil.logE(CacheUtil.class, "getCacheDirectory fail ,the reason is make directory fail!");
            }
        }
        return appCacheDir;
    }

    /**
     * 获取SD卡缓存目录
     * @param context 上下文
     * @param type 文件夹类型 如果为空则返回 /storage/emulated/0/Android/data/app_package_name/cache
     *             否则返回对应类型的文件夹如Environment.DIRECTORY_PICTURES 对应的文件夹为 .../data/app_package_name/files/Pictures
     * {@link Environment#DIRECTORY_MUSIC},
     * {@link Environment#DIRECTORY_PODCASTS},
     * {@link Environment#DIRECTORY_RINGTONES},
     * {@link Environment#DIRECTORY_ALARMS},
     * {@link Environment#DIRECTORY_NOTIFICATIONS},
     * {@link Environment#DIRECTORY_PICTURES}, or
     * {@link Environment#DIRECTORY_MOVIES}.or 自定义文件夹名称
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalCacheDirectory(Context context, String type) {
        File appCacheDir = null;
        if( Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = context.getExternalCacheDir();
            }else {
                appCacheDir = context.getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/"+context.getPackageName()+"/cache/"+type);
            }

            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                LogUtil.logE(CacheUtil.class, "getExternalDirectory fail ,the reason is make directory fail!");
            }
        }else {
            LogUtil.logE(CacheUtil.class, "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail!");
        }
        return appCacheDir;
    }

    /**
     * 获取内存缓存目录
     * @param type 子目录，可以为空，为空直接返回一级目录
     * @return 缓存目录文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getInternalCacheDirectory(Context context, String type) {
        File appCacheDir;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = context.getCacheDir();// /data/data/app_package_name/cache
        }else {
            appCacheDir = new File(context.getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            LogUtil.logE(CacheUtil.class, "getInternalDirectory fail ,the reason is make directory fail!");
        }
        return appCacheDir;
    }
}
