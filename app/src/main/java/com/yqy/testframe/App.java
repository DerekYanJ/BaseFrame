package com.yqy.testframe;

import android.app.Application;

import com.yqy.frame.utils.L;

/**
 * Created by DerekYan on 2017/9/19.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //设置是否打印Log
        L.setIsShow(getResources().getBoolean(R.bool.is_show_log));
    }
}

