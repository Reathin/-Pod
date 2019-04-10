package com.rair.pod;

import com.rair.pod.base.BaseApplication;

/**
 * @author Rair
 * @date 2018/4/19
 * <p>
 * desc:
 */
public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initToast(R.color.colorAccent);
        initLog(true, "Riar");
        initCrashHandler();
    }
}
