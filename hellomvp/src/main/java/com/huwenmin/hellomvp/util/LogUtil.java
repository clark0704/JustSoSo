package com.huwenmin.hellomvp.util;

import android.util.Log;

/**
 * 作者：Administrator on 2017/3/16 17:24
 * <p>
 * 功能：
 */

public class LogUtil {
    public static boolean isDebug = true;


    public static void v(String var0) {
        if(isDebug) {
            Log.v("wasu_model_log", var0);
        }

    }

    public static void v(String var0, String var1) {
        if(isDebug) {
            Log.v(var0, var1);
        }

    }

    public static void v(String var0, String var1, Throwable var2) {
        if(isDebug) {
            Log.v(var0, var1, var2);
        }

    }

    public static void d(String var0) {
        if(isDebug) {
            Log.d("wasu_model_log", var0);
        }

    }

    public static void d(String var0, String var1) {
        if(isDebug) {
            Log.d(var0, var1);
        }

    }

    public static void d(String var0, String var1, Throwable var2) {
        if(isDebug) {
            Log.d(var0, var1, var2);
        }

    }

    public static void i(String var0) {
        if(isDebug) {
            Log.i("wasu_model_log", var0);
        }

    }

    public static void i(String var0, String var1) {
        if(isDebug) {
            Log.i(var0, var1);
        }

    }

    public static void i(String var0, String var1, Throwable var2) {
        if(isDebug) {
            Log.i(var0, var1, var2);
        }

    }

    public static void w(String var0) {
        if(isDebug) {
            Log.w("wasu_model_log", var0);
        }

    }

    public static void w(String var0, String var1) {
        if(isDebug) {
            Log.w(var0, var1);
        }

    }

    public static void w(String var0, String var1, Throwable var2) {
        if(isDebug) {
            Log.w(var0, var1, var2);
        }

    }

    public static void e(String var0) {
        if(isDebug) {
            Log.e("wasu_model_log", var0);
        }

    }

    public static void e(String var0, String var1) {
        if(isDebug) {
            Log.e(var0, var1);
        }

    }

    public static void e(String var0, String var1, Throwable var2) {
        if(isDebug) {
            Log.e(var0, var1, var2);
        }

    }
}
