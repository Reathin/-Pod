package com.rair.pod.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;

import com.rair.pod.utils.AppUtils;
import com.rair.pod.utils.SPUtils;
import com.socks.library.KLog;
import com.wanjian.cockroach.Cockroach;

import java.io.IOException;
import java.util.Stack;

import es.dmoral.toasty.Toasty;


/**
 * BaseApplication主要用来管理全局Activity;
 * <p>
 * Created by Rair on 2017/9/6.
 */

public class BaseApplication extends Application {

    private static BaseApplication sInstance;
    private Stack<Activity> activityStack;
    protected boolean isDebug = true;

    public static BaseApplication getIns() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        AppUtils.init(this);
        SPUtils.getInstance();
        //初始化log日志
        KLog.init(isDebug, "Rair");
        //初始化Toasty
        Toasty.Config.getInstance().setInfoColor(Color.parseColor("#32353a")).apply();
        //全局捕获异常
        Cockroach.install((thread, throwable) -> {
            try {
                KLog.e(AppUtils.getErrorInfo(throwable));
            } catch (IOException e) {
                KLog.e(e.getMessage());
            }
        });
    }

    /**
     * 添加指定Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity == null) {
            return;
        } else {
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定Class的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity != null && activity.getClass().equals(cls)) {
                finishActivity(activity);
                return;
            }
        }
    }

    /**
     * 结束全部的Activity
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
     * 退出应用程序
     */
    public void exitApp(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                activityManager.killBackgroundProcesses(context.getPackageName());
            }
            System.exit(0);
        } catch (Exception e) {
            KLog.e("app exit" + e.getMessage());
        }
    }
}
