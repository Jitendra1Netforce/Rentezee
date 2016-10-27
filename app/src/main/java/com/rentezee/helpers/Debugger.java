package com.rentezee.helpers;

import android.util.Log;

public class Debugger {

    public static boolean isActive = true;

    public static void e(String tag, String msg) {
        if (isActive)
            Log.e(tag, msg!=null?msg:"null");
    }

    public static void d(String tag, String msg) {
        if (isActive)
            Log.d(tag,  msg!=null?msg:"null");
    }

    public static void i(String tag, String msg) {
        if (isActive)
            Log.i(tag,  msg!=null?msg:"null");
    }

    public static void v(String tag, String msg) {
        if (isActive)
            Log.v(tag,  msg!=null?msg:"null");
    }

    public static void w(String tag, String msg) {
        if (isActive)
            Log.w(tag,  msg!=null?msg:"null");
    }
}
