package com.yqy.baseframe.listener;

/**
 * 对话弹框点击事件
 * Created by DerekYan on 2017/4/24.
 */

public interface OnAlertDialogListener {

    /**
     * 右侧按钮
     */
    void onPositive();
    /**
     * 左侧按钮
     */
    void onNegative();
}
