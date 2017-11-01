package com.yqy.testframe;

import android.os.Bundle;

import com.yqy.frame.frame.BaseActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by DerekYan on 2017/9/19.
 */

public abstract class MBaseActivity extends BaseActivity {
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void setContentViewWithActionBar(int layoutResID) {
        super.setContentViewWithActionBar(layoutResID);
        setToolbarBackIcon(R.drawable.ic_up);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
