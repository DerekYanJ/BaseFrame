package com.yqy.baseframe.frame;

import android.os.Bundle;

import butterknife.ButterKnife;

/**
 * @author derekyan
 * @desc 没有状态栏
 * @date 2016/12/6
 */

public abstract class BaseActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(preView());
        mContext = this;
        unbinder = ButterKnife.bind(this);
        initView();
        addListener();
        initData();
    }
}
