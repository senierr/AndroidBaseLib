package com.senierr.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动工具类
 *
 * @author zhouchunjie
 * @date 2017/10/26
 */

public class ActivityUtil {

    private static List<Activity> activityList;

    private ActivityUtil() {
        activityList = new ArrayList<>();
    }

    private static class ActivityUtilHolder {
        private static final ActivityUtil instance = new ActivityUtil();
    }

    /**
     * 获取活动工具类实例
     *
     * @return 获取工具类
     */
    public static ActivityUtil getInstance() {
        return ActivityUtilHolder.instance;
    }

    /**
     * 添加活动
     *
     * @param activity 活动
     */
    public void add(Activity activity) {
        if (activity != null) {
            activityList.add(activity);
        }
    }

    /**
     * 移除活动
     *
     * @param activity 活动
     */
    public void remove(Activity activity) {
        if (activity != null) {
            activityList.remove(activity);
        }
    }

    /**
     * 结束某类活动
     *
     * @param cls 活动类别
     */
    public void finish(Class<?> cls) {
        if (cls != null) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(cls)) {
                    activity.finish();
                    return;
                }
            }
        }
    }

    /**
     * 结束所有活动
     */
    public void finishAll() {
        while (activityList.size() > 0) {
            activityList.remove(0);
            activityList.get(0).finish();
        }
    }
}