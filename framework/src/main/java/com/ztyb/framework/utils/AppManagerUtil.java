package com.ztyb.framework.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.util.List;
import java.util.Stack;

import static android.content.Context.ACTIVITY_SERVICE;


/**
 * ============================================================
 * 作者：游扬波
 * activity管理类
 * ============================================================
 */
public class AppManagerUtil {

    private static Stack<Activity> activityStack;
    private static AppManagerUtil instance;

    private AppManagerUtil() {

    }

    /**
     * A single instance
     */
    public static AppManagerUtil instance() {
        if (instance == null) {
            instance = new AppManagerUtil();
        }
        return instance;
    }

    /**
     * 绑定 Activity 添加到集合统一管理
     *
     * @param activity
     */
    public void attachActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        if (activityStack.contains(activity)) {
            return;
        }
        activityStack.add(activity);
    }

    /**
     * 解除 Activity 绑定，移除统一管理
     *
     * @param activity
     */
    public void detachActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            int size = activityStack.size();
            for (int i = 0; i < size; i++) {
                // 遍历所有的 subscriptions ，逐一移除
                Activity equalActivity = activityStack.get(i);
                if (equalActivity.getClass().getName() == activity.getClass().getName()) {
                    activityStack.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    /**
     * Gets the current Activity (stack last pressed into)
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }


    /**
     * 获取当前界面的上一个界面
     */
    public Activity previousActivity() {
        Activity activity = null;
        if (activityStack != null && activityStack.size() >= 2) {
            activity = activityStack.get(activityStack.size() - 2);
        }
        return activity;
    }

    /**
     * The finish of the specified Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null && activityStack.size() > 0) {
            int size = activityStack.size();
            for (int i = 0; i < size; i++) {
                // 遍历所有的 subscriptions ，逐一移除
                Activity equalActivity = activityStack.get(i);
                if (equalActivity.getClass().getName() == activity.getClass().getName()) {
                    activityStack.remove(i);
                    equalActivity.finish();
                    i--;
                    size--;
                }
            }
        }
    }

    /**
     * End of the specified class name of the Activity
     */
    public void finishActivity(Class<? extends Activity> cls) {
        if (cls != null && activityStack != null && activityStack.size() > 0) {
            int size = activityStack.size();
            for (int i = 0; i < size; i++) {
                // 遍历所有的 subscriptions ，逐一移除
                Activity equalActivity = activityStack.get(i);
                if (equalActivity.getClass().getName() == cls.getName()) {
                    activityStack.remove(i);
                    equalActivity.finish();
                    i--;
                    size--;
                }
            }
        }
    }

    /**
     * Finish all the Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * Exit the application
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {

        }
    }

    /**
     * 判定程序是否处于后台
     *
     * @param context
     * @return true if in backgroud
     */
    public static boolean isAppInBackgroud(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
//                Logger.d(TAG, "在后台");
                return true;
            }
        }
//        Logger.d(TAG, "在前台");
        return false;
    }

    /**
     * 判断应用是否存活
     *
     * @param context
     * @param str
     * @return
     */
    public static boolean isAppaLive(Context context, String str) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        boolean isAppRunning = false;
        //String MY_PKG_NAME = "你的包名";
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(str)//如果想要手动输入的话可以str换成<span style="font-family: Arial, Helvetica, sans-serif;">MY_PKG_NAME，下面相同</span>
                    || info.baseActivity.getPackageName().equals(str)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }


    /**
     * @param appPackageName
     */
    public static void openAPP(Context context, String appPackageName) {
        try {
            //如果有，直接打开
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(appPackageName);
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    /**
     * 把app 调入前台
     * @param context
     */
    public static void backgroud2Front(Context context, String luachClassName) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(new ComponentName(context.getApplicationContext().getPackageName(), luachClassName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
    }



}