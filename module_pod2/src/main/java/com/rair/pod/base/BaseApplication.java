package com.rair.pod.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorRes;

import com.rair.pod.cockroach.Cockroach;
import com.rair.pod.cockroach.ExceptionHandler;
import com.rair.pod.utils.AppUtils;
import com.socks.library.KLog;

import java.util.Stack;

import es.dmoral.toasty.Toasty;


/**
 * BaseApplication主要用来管理全局Activity;
 * <p>
 * Created by Rair on 2017/9/6.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;
    /**
     * Activity堆栈
     */
    private Stack<Activity> activityStack;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        AppUtils.init(this);
        KLog.init(true,"Rair");
    }

    /**
     * 初始化日志
     *
     * @param isDebug isDebug
     * @param tag     tag
     */
    protected void initLog(boolean isDebug, String tag) {
        KLog.init(isDebug, tag);
    }

    /**
     * 初始化toasty
     *
     * @param colorId 颜色
     */
    protected void initToast(@ColorRes int colorId) {
        Toasty.Config.getInstance().setInfoColor(AppUtils.getColor(colorId)).apply();
    }

    /**
     * 初始化 全局异常上报
     * <p>
     * Crash 异常处理
     */
    protected void initCrashHandler() {
        Cockroach.install(new ExceptionHandler() {
            @Override
            protected void onUncaughtExceptionHappened(Thread thread, Throwable throwable) {
                KLog.w("onUncaughtExceptionHappened:" + AppUtils.getErrorInfo(throwable));
            }

            @Override
            protected void onBandageExceptionHappened(Throwable throwable) {
                KLog.w("onBandageExceptionHappened:" + AppUtils.getErrorInfo(throwable));
            }

            @Override
            protected void onEnterSafeMode() {
                KLog.w("onEnterSafeMode");
            }

            @Override
            protected void onMayBeBlackScreen(Throwable e) {
                super.onMayBeBlackScreen(e);
                KLog.w("onMayBeBlackScreen:" + AppUtils.getErrorInfo(e));
            }
        });
    }

    /**
     * 添加指定Activity到堆栈
     *
     * @param activity Activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity
     *
     * @return who extends Activity
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束Activity
     * <p>
     * 栈中最上面那个  弹出栈
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            finishActivity(activity);
        }
    }

    /**
     * 结束指定的Activity
     *
     * @param activity who extends Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
        }
    }

    /**
     * 结束指定Class的Activity
     *
     * @param cls 例如：MainActivity.class
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
     * <p>
     * 将Activity 的所有Activity 进行finish();
     */
    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (null != activity) {
                activity.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 重启应用
     */
    public void restartApplication() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            exitApplication();
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApplication() {
        try {
            finishAllActivity();
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                activityManager.killBackgroundProcesses(getPackageName());
            }
            System.exit(0);
        } catch (Exception e) {
            KLog.e("退出App异常:" + e.getMessage());
        }
    }
}
