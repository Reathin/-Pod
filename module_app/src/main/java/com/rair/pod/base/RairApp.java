package com.rair.pod.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.rair.pod.service.MusicService;

/**
 * Created by Rair on 2017/7/21.
 * Email:rairmmd@gmail.com
 * Author:Rair
 */

public class RairApp extends Application {

    private static RairApp app;
    private MusicService mService;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        Intent intent = new Intent(this, MusicService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            //用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
            mService = ((MusicService.LocalBinder) service).getService();
            mService.setContext(getApplicationContext());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    public static RairApp getApp() {
        return app;
    }

    public MusicService getService() {
        return mService;
    }
}
