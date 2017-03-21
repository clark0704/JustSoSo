package com.huwenmin.hellomvp;

import android.app.Application;

import com.huwenmin.hellomvp.util.LogUtil;

/**
 * 作者：胡文敏 on 2017/3/21 13:26
 * <p>
 * 功能：application
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        LogUtil.isDebug = BuildConfig.DEBUG;
    }
}
