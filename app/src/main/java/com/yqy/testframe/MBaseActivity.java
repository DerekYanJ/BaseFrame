package com.yqy.testframe;

import com.yqy.frame.frame.BaseActivity;

/**
 * Created by DerekYan on 2017/9/19.
 */

public abstract class MBaseActivity extends BaseActivity {
    @Override
    protected void setContentViewWithActionBar(int layoutResID) {
        super.setContentViewWithActionBar(layoutResID);
        setToolbarBackIcon(R.drawable.ic_up);
    }
}
