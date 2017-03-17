package com.huwenmin.hellomvp.presenter;

import android.content.Intent;

import com.huwenmin.hellomvp.listener.BaseListener;

/**
 * 作者：Administrator on 2017/3/16 17:36
 * <p>
 * 功能：MVP中的P
 */

public interface BasePresenter {
    void onCreate();
    void onStart();
    void onStop();
    void pause();
    void setListener(BaseListener listener);
    void attachIncomingIntent(Intent intent);
}
