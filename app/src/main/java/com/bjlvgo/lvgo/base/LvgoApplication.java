package com.bjlvgo.lvgo.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by 旅购 on 2016/3/1.
 */
public class LvgoApplication extends Application implements Thread.UncaughtExceptionHandler{

    private static Context context = null;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

       //错误日志上传
    }
}
