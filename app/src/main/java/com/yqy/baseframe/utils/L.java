package com.yqy.baseframe.utils;

import android.util.Log;

/**
 * Log
 * Created by yanqy on 2017/4/18.
 */

public class L {
    /**
     * 是否展示，在不需要的时候设置为false
     */
    public static boolean isShow = false;
    public static String TAG = "YQY";

    /**
     *
     * @param tag
     *            设置tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isShow)
            Log.e(tag, msg);
    }

    /**
     *
     * @param tag
     *            设置tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isShow)
            Log.d(tag, msg);
    }

    /**
     * 使用默认tag
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isShow)
            Log.e(TAG, msg);
    }
}
