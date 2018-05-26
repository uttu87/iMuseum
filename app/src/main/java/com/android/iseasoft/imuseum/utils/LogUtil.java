package com.android.iseasoft.imuseum.utils;

import android.util.Log;

public class LogUtil {
    public static boolean bIsEnable = true;

    public static void d(String tag, String msg){
        if(bIsEnable)
            Log.d(tag, msg);
    }

    public static void v(String tag, String msg){
        if(bIsEnable)
            Log.v(tag, msg);
    }

    public static void e(String tag, String msg){
        if(bIsEnable)
            Log.e(tag, msg);
    }
}
